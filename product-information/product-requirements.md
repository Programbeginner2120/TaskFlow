# TaskFlow — Product Requirements Document

## Table of Contents

- [1. Product Overview](#1-product-overview)
- [2. Design System & Theming](#2-design-system--theming)
- [3. Layout Architecture](#3-layout-architecture)
- [4. Feature Requirements](#4-feature-requirements)
- [5. Data Model](#5-data-model)
- [6. Frontend State Management](#6-frontend-state-management)
- [7. Non-Functional Requirements](#7-non-functional-requirements)
- [8. Future Considerations](#8-future-considerations-out-of-scope-for-v1)

---

## 1. Product Overview

TaskFlow is a full-stack personal task management application designed for individuals who need a clean, fast, and intuitive way to organize their daily work and personal tasks. It combines list-based task management with calendar visualization in a single-page application.

**Target Users:** Individual professionals, students, and productivity enthusiasts who want a lightweight alternative to complex project management tools.

### Tech Stack

| Layer | Technology |
|-------|------------|
| **Frontend** | Angular (standalone components, zoneless change detection), TypeScript, SCSS |
| **Backend** | Java, Spring Boot, Spring Security (stateless JWT) |
| **Database** | PostgreSQL (schema managed by Liquibase, queries via MyBatis) |
| **Infrastructure** | Docker / Docker Compose; Nginx reverse proxy |

---

## 2. Design System & Theming

### 2.1 Color & Typography

- **Color palette:** Slate/Zinc neutrals with an Indigo/Violet primary accent
- **Semantic tokens:** All colors must use CSS variables (e.g., `--primary`, `--background`, `--muted`) — never hardcoded values
- **Typography:** System font stack with a clear hierarchy between headings, body, and caption text
- **Accessibility:** All color combinations must meet WCAG 2.1 AA contrast ratios (4.5:1 for text, 3:1 for UI elements)

### 2.2 Theme Support

| Req ID | Requirement |
|--------|-------------|
| TH-1 | Full Dark Mode and Light Mode support |
| TH-2 | Toggle button in header, always visible |
| TH-3 | Smooth CSS transitions between themes (no flash) |
| TH-4 | Persist user preference in `localStorage` |
| TH-5 | Default to OS preference (`prefers-color-scheme`) on first visit |

---

## 3. Layout Architecture

### 3.1 Responsive Breakpoints

| Viewport | Layout Behavior |
|----------|-----------------|
| Desktop (≥ 1024px) | Persistent sidebar (left) + main content + detail drawer (right) |
| Tablet (768–1023px) | Collapsible sidebar overlay + main content |
| Mobile (< 768px) | Hamburger menu sidebar overlay; full-screen detail views |

### 3.2 Header

| Req ID | Requirement |
|--------|-------------|
| HD-1 | Fixed/sticky at top, always visible |
| HD-2 | Global search input with icon — filters tasks by title in real-time |
| HD-3 | "Quick Add" button (`+`) opens a dialog to create a task from anywhere |
| HD-4 | Dark/Light mode toggle button |
| HD-5 | User profile avatar/icon showing the authenticated user's display name; opens a dropdown with a logout option |
| HD-6 | Hamburger menu button visible on mobile/tablet only; toggles sidebar |

### 3.3 Sidebar

| Req ID | Requirement |
|--------|-------------|
| SB-1 | Navigation links: "My Day", "Upcoming", "Calendar View" |
| SB-2 | "My Lists" section showing all user-created task lists |
| SB-3 | Inline input at bottom of the lists section to create a new list |
| SB-4 | Active nav item visually highlighted |
| SB-5 | Task count badge next to each nav item showing incomplete tasks |
| SB-6 | Each list shows its assigned color as a dot/indicator |
| SB-7 | Desktop: persistent, always visible; Mobile: overlay with backdrop, dismissable |

### 3.4 Main Content Area

| Req ID | Requirement |
|--------|-------------|
| MC-1 | Dynamically renders the correct view based on sidebar selection |
| MC-2 | Views: Task List View (for My Day, Upcoming, and custom lists) or Calendar View |
| MC-3 | Scrolls independently from header and sidebar |

---

## 4. Feature Requirements

### 4.1 Task Lists (CRUD)

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| TL-1 | Create: Inline text input in sidebar; submit on Enter | P0 |
| TL-2 | Read: Display all lists in sidebar with name and color dot | P0 |
| TL-3 | Update: Hover/3-dot menu on each list → "Rename" option; inline edit | P1 |
| TL-4 | Delete: Hover/3-dot menu → "Delete" with confirmation dialog | P1 |
| TL-5 | Deleting a list also deletes all tasks assigned to it | P0 |
| TL-6 | Auto-assign a color from a rotating palette on creation | P2 |
| TL-7 | Clicking a list in sidebar filters the main view to only that list's tasks | P0 |
| TL-8 | Default seed lists: "Work", "Personal", "Shopping" | P2 |

### 4.2 Tasks (CRUD)

#### 4.2.1 Create

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| TK-1 | Quick-add input bar at top of task list view | P0 |
| TK-2 | Submit on Enter key | P0 |
| TK-3 | Date-picker icon in the input bar to assign a due date before creation | P1 |
| TK-4 | If in "My Day" view, auto-assign today's date | P1 |
| TK-5 | If in a custom list view, auto-assign that list | P1 |
| TK-6 | "Quick Add" dialog (from header button) with title, date, and list selection | P1 |

#### 4.2.2 Read / Display

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| TK-10 | Display as a vertical list with checkboxes | P0 |
| TK-11 | Show task title, due date (formatted), and list color indicator | P0 |
| TK-12 | Completed tasks: strikethrough title + reduced opacity | P0 |
| TK-13 | Completed tasks sorted to bottom of list | P1 |
| TK-14 | Subtask progress indicator on parent task (e.g., "2/4") | P1 |
| TK-15 | Overdue tasks visually flagged (e.g., red date text) | P1 |
| TK-16 | Smooth animations on task completion toggle | P2 |

#### 4.2.3 Update

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| TK-20 | Clicking a task opens a detail drawer/panel | P0 |
| TK-21 | Detail view: edit title inline | P0 |
| TK-22 | Detail view: change due date via date picker | P0 |
| TK-23 | Detail view: reassign to a different list via dropdown | P1 |
| TK-24 | Detail view: add/edit notes (textarea) | P1 |
| TK-25 | Desktop: side-drawer on the right; Mobile: full-screen panel | P0 |
| TK-26 | Toggle completion via checkbox (instant visual feedback) | P0 |

#### 4.2.4 Delete

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| TK-30 | Delete button/icon visible on hover (desktop) | P0 |
| TK-31 | Delete button in detail view | P0 |
| TK-32 | Confirmation before permanent deletion | P2 |

### 4.3 Subtasks (CRUD)

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| ST-1 | Nested inside the task detail drawer/panel | P0 |
| ST-2 | Input field to add a new subtask; submit on Enter | P0 |
| ST-3 | Display with checkboxes; toggle completion independently | P0 |
| ST-4 | Completed subtasks: strikethrough + reduced opacity | P0 |
| ST-5 | Delete subtask via hover trash icon or button | P1 |
| ST-6 | Subtask count/progress shown on parent task in list view | P1 |
| ST-7 | Inline title editing | P2 |

### 4.4 Smart Views

#### 4.4.1 My Day

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| MD-1 | Shows all tasks with `dueDate` equal to today | P0 |
| MD-2 | New tasks added here are auto-assigned today's date | P1 |
| MD-3 | Header displays "My Day" with the remaining task count | P0 |

#### 4.4.2 Upcoming

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| UP-1 | Shows all tasks due within the next 7 days | P0 |
| UP-2 | Sorted/grouped by date | P2 |

#### 4.4.3 Calendar View

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| CV-1 | Full-page monthly calendar grid | P0 |
| CV-2 | Navigate between months (prev/next arrows) | P0 |
| CV-3 | Today's date visually highlighted | P0 |
| CV-4 | Days with tasks show colored dots or task count badges | P0 |
| CV-5 | Clicking a day opens a modal/popover listing that day's tasks | P0 |
| CV-6 | Tasks in day modal are interactive (toggle completion, click to open detail) | P1 |
| CV-7 | Responsive: readable grid on mobile viewports | P1 |

### 4.5 Search

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| SR-1 | Global search input in header | P0 |
| SR-2 | Filters currently visible tasks by title (case-insensitive substring match) | P0 |
| SR-3 | Real-time filtering as the user types (no submit button needed) | P0 |
| SR-4 | Clear button or clear-on-Escape | P2 |

### 4.6 Authentication

Authentication is fully in scope. The backend issues stateless JWT tokens; the frontend stores them in `localStorage` and attaches them to every API request via an HTTP interceptor.

#### 4.6.1 Registration

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| AU-1 | Registration form: email, display name, password, confirm password | P0 |
| AU-2 | Password requirements: minimum 8 characters, must contain uppercase, lowercase, digit, and special character | P0 |
| AU-3 | Inline validation with clear error messages | P0 |
| AU-4 | On successful registration, send a verification email and redirect to a confirmation screen | P0 |
| AU-5 | Duplicate email returns a clear error (409) | P0 |

#### 4.6.2 Email Verification

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| AU-10 | Verification link in email navigates to `/verify-email?token=` | P0 |
| AU-11 | Valid token: activate account and show success state | P0 |
| AU-12 | Expired/invalid token: show error with option to resend | P0 |
| AU-13 | Resend verification email from the login screen | P1 |

#### 4.6.3 Login

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| AU-20 | Login form: email and password | P0 |
| AU-21 | Invalid credentials return a clear error (401) | P0 |
| AU-22 | Unverified email returns a distinct error with a resend option (403) | P0 |
| AU-23 | Successful login stores JWT and redirects to the main application | P0 |
| AU-24 | `authGuard` redirects unauthenticated users to `/login` | P0 |
| AU-25 | Expired JWT triggers automatic logout and redirect to `/login` (handled by HTTP interceptor on 401) | P0 |

#### 4.6.4 Forgot / Reset Password

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| AU-30 | "Forgot password" link on login screen | P0 |
| AU-31 | Forgot-password form: email input; sends reset link if account exists | P0 |
| AU-32 | Reset link navigates to `/reset-password?token=` | P0 |
| AU-33 | Reset form: new password + confirm password with same strength requirements | P0 |
| AU-34 | Expired/invalid token shows a clear error | P0 |
| AU-35 | Successful reset redirects to login | P0 |

#### 4.6.5 Session & Logout

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| AU-40 | Logout clears JWT from `localStorage` and redirects to `/login` | P0 |
| AU-41 | On application load, validate the stored JWT by calling `/auth/me`; log out if invalid | P0 |

---

## 5. Data Model

All entities are persisted in PostgreSQL. IDs are auto-incrementing `BIGSERIAL` (represented as `number` on the frontend).

### 5.1 User

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `id` | `number` | ✅ | Auto-generated primary key |
| `email` | `string` | ✅ | Unique; used for login |
| `passwordHash` | `string` | ✅ | BCrypt hash; never exposed to frontend |
| `displayName` | `string` | ✅ | Shown in header |
| `emailVerified` | `boolean` | ✅ | Must be `true` before login is permitted |
| `createdAt` | `Date` | ✅ | Creation timestamp |
| `updatedAt` | `Date` | ✅ | Last-updated timestamp |

### 5.2 Task

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `id` | `number` | ✅ | Auto-generated primary key |
| `userId` | `number` | ✅ | FK to `User`; tasks are user-scoped |
| `title` | `string` | ✅ | Task name |
| `completed` | `boolean` | ✅ | Completion state |
| `dueDate` | `Date \| null` | ❌ | Optional due date |
| `listId` | `number` | ✅ | FK to `TaskList` |
| `notes` | `string` | ❌ | Free-text notes |
| `createdAt` | `Date` | ✅ | Creation timestamp |

### 5.3 Subtask

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `id` | `number` | ✅ | Auto-generated primary key |
| `taskId` | `number` | ✅ | FK to parent `Task` |
| `title` | `string` | ✅ | Subtask name |
| `completed` | `boolean` | ✅ | Completion state |

### 5.4 TaskList

| Field | Type | Required | Notes |
|-------|------|----------|-------|
| `id` | `number` | ✅ | Auto-generated primary key |
| `userId` | `number` | ✅ | FK to `User`; lists are user-scoped |
| `name` | `string` | ✅ | List display name |
| `color` | `string` | ✅ | Hex color or token |

---

## 6. Frontend State Management

| Req ID | Requirement |
|--------|-------------|
| SM-1 | Angular services act as the single source of truth for task and list state |
| SM-2 | State exposed as Angular signals; components react automatically via computed signals and `effect()` |
| SM-3 | All mutations go through service methods that call the backend REST API and update local signals on success |
| SM-4 | Authentication state (`isAuthenticated`, current user) held in `AuthService` and hydrated from the backend on application startup |

---

## 7. Non-Functional Requirements

| Req ID | Requirement | Priority |
|--------|-------------|----------|
| NF-1 | First Contentful Paint < 1.5 s | P1 |
| NF-2 | All interactions feel instant (< 100 ms visual feedback) | P0 |
| NF-3 | No layout shift on theme toggle | P0 |
| NF-4 | Keyboard accessible: Tab navigation, Enter to submit, Escape to close modals | P1 |
| NF-5 | Animations respect `prefers-reduced-motion` | P2 |
| NF-6 | Zero runtime errors in the console during normal operation | P0 |

---

## 8. Future Considerations (Out of Scope for V1)

| Feature | Notes |
|---------|-------|
| Drag-and-drop reordering | Reorder tasks and lists |
| Task priorities | Low / Medium / High with color-coded badges |
| Recurring tasks | Daily, weekly, custom recurrence |
| Labels/Tags | Multi-label tagging system |
| Notifications/Reminders | Due date reminders via browser notifications |
| Collaboration | Shared lists, task assignment |
| Undo/Redo | Action history stack |
| Export | CSV/JSON export of tasks |
