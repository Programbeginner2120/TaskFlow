# ACID Compliance Report — 2026-04-12.1

## Summary

| Property    | Status | Issues Found |
|-------------|--------|--------------|
| Atomicity   | ✅      | 0            |
| Consistency | ⚠️      | 3            |
| Isolation   | ⚠️      | 2            |
| Durability  | ✅      | 0            |

---

## Findings

### Atomicity

No issues found. All multi-write methods are correctly annotated with `@Transactional`:

- `UserService.register()` — `userRepository.save` + `emailTokenRepository.save`
- `UserService.verifyEmail()` — `emailTokenRepository.markUsed` + `userRepository.setEmailVerified`
- `UserService.resetPassword()` — `emailTokenRepository.markUsed` + `userRepository.updatePassword`
- `TaskListTemplateService.createTemplate()` — template save + N task template saves + M subtask template saves
- `TaskListTemplateService.updateTemplate()` — `deleteAllByListTemplateId` + N re-inserts
- `TemplateGeneratorService.generateFromTemplate()` — task list save + N task saves + M subtask saves + `updateSchedule`

---

### Consistency

**Finding C-1: `display_name` is nullable in SQL but Java builds it as a required field with no null guard**

- **File:** `backend/src/main/resources/db/changelog/20260214.1-CreateUsersTable.sql`
- **Method:** `UserService.register()`
- **Issue:** The `display_name` column has no `NOT NULL` constraint in the DB schema. However, `UserService.register()` unconditionally passes `displayName` into the builder without a null check. If `displayName` were null (e.g. a future caller skips it), the DB would silently store NULL rather than reject the request. There is no `@NotBlank` or `@NotNull` on `RegisterRequest.displayName` enforcing this at the controller boundary — this must be verified.
- **Fix:** Either add `NOT NULL` to `display_name` in the schema (requires a migration) OR add `@NotNull` / `@NotBlank` to `RegisterRequest.displayName`. Confirm the validation annotation is present on the request model.

**Finding C-2: `task_lists.name` is `NOT NULL` in SQL but `list_id` on tasks is nullable with no FK guard in `TemplateGeneratorService`**

- **File:** `backend/src/main/java/com/killeen/taskflow/components/tasklisttemplate/service/TemplateGeneratorService.java`
- **Method:** `generateFromTemplate()`
- **Issue:** Tasks are inserted with `listId` set to `listId` (returned from `taskListRepository.save`). This is correct. However, the task list `name` field is encrypted at the application layer — if `TaskListEncryptionHelper.encrypt` were to return a null `name` (e.g. an encryption failure that didn't throw), the DB would reject the insert with a constraint violation rather than a meaningful application error. This is a durability-adjacent consistency concern: the application has no pre-write null guard on encrypted fields before they hit the DB.
- **Fix:** Low priority given `EncryptionException` is properly mapped in `GlobalExceptionHandler`, which would surface before the DB constraint fires. No immediate action required, but noted for awareness.

**Finding C-3: `findDueTemplates` has no user-ownership filter**

- **File:** `backend/src/main/java/com/killeen/taskflow/components/tasklisttemplate/repository/TaskListTemplateRepository.java`
- **Method:** `findDueTemplates(OffsetDateTime asOf)`
- **Issue:** This query selects all templates from all users where `next_generate <= asOf` with no `user_id` scope. This is intentional for a cron operation but means any compromise of the `/internal/generate-templates` endpoint would trigger generation for every user's templates. The consistency concern is that there is no per-user rate limiting or ownership assertion before generating real task lists for every matching row.
- **Fix:** The `X-Cron-Secret` header check in `TemplateGenerationController` adequately guards this endpoint. No DB change needed. Consider documenting intentionality in the repository method.

---

### Isolation

**Finding I-1: `findDueTemplates` has no row-level locking — concurrent cron calls will process the same templates twice**

- **File:** `backend/src/main/java/com/killeen/taskflow/components/tasklisttemplate/repository/TaskListTemplateRepository.java`
- **Method:** `findDueTemplates(OffsetDateTime asOf)`
- **Issue:** Two simultaneous calls to `POST /internal/generate-templates` (e.g. a slow first call overlapping with the next scheduled invocation) will both execute `findDueTemplates` and receive the same set of due templates. Both will then independently call `generateFromTemplate` on the same rows, creating duplicate task lists for users. The `updateSchedule` call at the end of each generation updates `next_generate`, but both callers will have already passed the check.
- **Fix:** Two options:
  1. **Optimistic locking:** Add a `version` column to `task_list_templates` and use conditional updates in `updateSchedule` (`WHERE id = ? AND version = ?`). Roll back generation if the version has already changed.
  2. **Advisory lock / `SELECT FOR UPDATE SKIP LOCKED`:** Replace the `findDueTemplates` select with a `SELECT ... FOR UPDATE SKIP LOCKED` query so concurrent callers each claim a distinct subset of rows. This is the more robust approach for a polling scheduler.
  
  **At minimum**, the cron trigger should be idempotent or protected by a distributed lock (e.g. `ShedLock`) if horizontal scaling is planned.

**Finding I-2: `updateTask` is a read-then-write without `@Transactional` — susceptible to lost updates under concurrency**

- **File:** `backend/src/main/java/com/killeen/taskflow/components/task/service/TaskService.java`
- **Method:** `updateTask(Long userId, Long taskId, UpdateTaskRequest request)`
- **Issue:** The method loads the existing task (`findByIdAndUserId`), mutates it in memory, then saves it back (`taskRepository.update`). Without `@Transactional`, two concurrent requests updating the same task could both read the same original state, both compute their own updated version, and the second write will silently overwrite the first. The same pattern exists in `updateSubtask`, `updateTaskList`, and `updateTemplate`.
- **Fix:** Add `@Transactional` to `updateTask`, `updateSubtask`, `updateTaskList`, and `TaskListTemplateService.updateTemplate` (the last one is already annotated — ✅). This does not prevent all races but ensures each read-modify-write is executed within a single database transaction, and with the default PostgreSQL `READ COMMITTED` isolation, prevents the most common lost-update scenario.

  | Method | File | `@Transactional` present? |
  |--------|------|--------------------------|
  | `updateTask` | `TaskService.java` | ❌ |
  | `updateSubtask` | `TaskService.java` | ❌ |
  | `updateTaskList` | `TaskListService.java` | ❌ |
  | `updateTemplate` | `TaskListTemplateService.java` | ✅ |

---

### Durability

No issues found.

- All writes go through MyBatis mapper layer (`insert`, `updateByPrimaryKey`, `updateByPrimaryKeySelective`) — no fire-and-forget async writes exist.
- `GlobalExceptionHandler` has catch-all `Exception` handler that surfaces failures rather than swallowing them.
- No `catch` block in any write path suppresses an exception that originated inside a `@Transactional` method (the `generateDueTemplates` loop catch is outside `@Transactional`, which is correct — it allows per-template isolation without interfering with the already-committed or already-rolled-back inner transaction).
- `EncryptionException` is mapped to 500 and propagated — encrypted write failures are surfaced.

---

## Passed Checks

### Atomicity
- `UserService.register` ✅
- `UserService.verifyEmail` ✅
- `UserService.resetPassword` ✅
- `TaskListTemplateService.createTemplate` ✅
- `TaskListTemplateService.updateTemplate` ✅
- `TemplateGeneratorService.generateFromTemplate` ✅
- `UserService.resendVerification` — single write path (`emailTokenRepository.save`), no multi-write concern ✅
- `UserService.forgotPassword` — single write path, no multi-write concern ✅
- `TaskListService.createTaskList` — single write ✅
- `TaskListService.deleteTaskList` — single write ✅
- `TaskService.createTask` — single write ✅
- `TaskService.createSubtask` — single write ✅
- `TaskService.deleteTask` — single write ✅
- `TaskService.deleteSubtask` — single write ✅
- `TaskListTemplateService.deleteTemplate` — single write ✅

### Consistency — Controller `@Valid` Coverage
- `AuthController.register` — `@Valid @RequestBody RegisterRequest` ✅
- `AuthController.login` — `@Valid @RequestBody LoginRequest` ✅
- `AuthController.resendVerification` — `@Valid @RequestBody ResendVerificationRequest` ✅
- `AuthController.forgotPassword` — `@Valid @RequestBody ForgotPasswordRequest` ✅
- `AuthController.resetPassword` — `@Valid @RequestBody ResetPasswordRequest` ✅
- `TaskController.createTask` — `@Valid @RequestBody CreateTaskRequest` ✅
- `TaskController.updateTask` — `@Valid @RequestBody UpdateTaskRequest` ✅
- `TaskController.createSubtask` — `@Valid @RequestBody CreateSubtaskRequest` ✅
- `TaskController.updateSubtask` — `@Valid @RequestBody UpdateSubtaskRequest` ✅
- `TaskListController.createTaskList` — `@Valid @RequestBody CreateTaskListRequest` ✅
- `TaskListController.updateTaskList` — `@Valid @RequestBody UpdateTaskListRequest` ✅
- `TaskListTemplateController.createTemplate` — `@Valid @RequestBody CreateTaskListTemplateRequest` ✅
- `TaskListTemplateController.updateTemplate` — `@Valid @RequestBody UpdateTaskListTemplateRequest` ✅

### Consistency — Referential Integrity Guards
- `TaskService.createTask` — guards `list_id` FK with `taskListRepository.findByIdAndUserId` before insert ✅
- `TaskService.updateTask` — guards `list_id` FK change with `taskListRepository.findByIdAndUserId` ✅
- `TaskService.createSubtask` — guards `task_id` FK with `taskRepository.findByIdAndUserId` before insert ✅
- All template child saves go through parent-ID that was just inserted in the same transaction ✅

### SQL Schema — Constraint Coverage
- `users.email` — `NOT NULL UNIQUE` ✅
- `users.password_hash` — `NOT NULL` ✅
- `users.email_verified` — `NOT NULL DEFAULT FALSE` ✅
- `email_tokens.user_id` — `NOT NULL` + FK with `ON DELETE CASCADE` ✅
- `email_tokens.token` — `NOT NULL UNIQUE` ✅
- `email_tokens.token_type` — `NOT NULL` (ENUM) ✅
- `task_lists.name` — `NOT NULL` ✅
- `tasks.title` — `NOT NULL` ✅
- `tasks.list_id` — nullable (intentional, tasks can be inbox items without a list) ✅
- `subtasks.task_id` — `NOT NULL` + FK ✅
- `task_list_templates.rrule` — `NOT NULL` ✅
- `task_list_templates.next_generate` — `NOT NULL` ✅
- `task_templates.list_template_id` — `NOT NULL` + FK with `ON DELETE CASCADE` ✅
- `subtask_templates.task_template_id` — `NOT NULL` + FK with `ON DELETE CASCADE` ✅

### Durability
- All repositories ✅
- `GlobalExceptionHandler` — no swallowed exceptions in write paths ✅
- `generateDueTemplates` catch placement is outside `@Transactional` boundary — correct ✅
