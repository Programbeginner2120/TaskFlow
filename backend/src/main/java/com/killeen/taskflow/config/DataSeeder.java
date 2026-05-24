package com.killeen.taskflow.config;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.killeen.taskflow.components.task.model.CreateTaskRequest;
import com.killeen.taskflow.components.task.service.TaskService;
import com.killeen.taskflow.components.tasklist.model.CreateTaskListRequest;
import com.killeen.taskflow.components.tasklist.model.TaskList;
import com.killeen.taskflow.components.tasklist.service.TaskListService;
import com.killeen.taskflow.components.user.model.User;
import com.killeen.taskflow.components.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile("local")
@Component
@Slf4j
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private static final String    SEED_EMAIL    = "seed@local.dev";
    private static final String    SEED_PASSWORD = "SeedPass123!";
    private static final LocalDate SEED_START    = Year.now().atDay(1);

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskListService taskListService;
    private final TaskService     taskService;
    private final JdbcTemplate    jdbcTemplate;

    // -------------------------------------------------------------------------
    // Seed data definition
    // -------------------------------------------------------------------------
    // Fields: title | list ("Work"/"Personal"/"Shopping"/null) | notes
    //         createdDay | completedDay | dueDay
    //
    // All day values are offsets from SEED_START (2026-01-01).
    //   completedDay — must be > createdDay; null = task is still open.
    //   dueDay       — must be >= createdDay; null = no due date.
    // -------------------------------------------------------------------------

    private record SeedEntry(
            String title, String list, String notes,
            int createdDay, Integer completedDay, Integer dueDay) {}

    private static final List<SeedEntry> SEED_TASKS = List.of(

        // ── Work ──────────────────────────────────────────────────────────────

        new SeedEntry("Review Q2 roadmap presentation",          "Work", "Check slides before the all-hands.",          0,    1,   14),
        new SeedEntry("Fix login page bug",                      "Work", null,                                          1,    3,    9),
        new SeedEntry("Write release notes for v1.3",            "Work", null,                                          3,  null,   19),
        new SeedEntry("Schedule 1-on-1 with Alex",               "Work", null,                                          5,    5, null),
        new SeedEntry("Draft Q3 OKRs",                           "Work", "Align with product and design teams.",       10,   11,   35),
        new SeedEntry("Code review for PR #42",                  "Work", "Focus on auth changes.",                     14,   14, null),
        new SeedEntry("Update API documentation",                "Work", null,                                         20, null,   50),
        new SeedEntry("Investigate slow dashboard query",        "Work", "Query takes > 3 s on large datasets.",       25,   27, null),
        new SeedEntry("Prepare demo for stakeholders",           "Work", null,                                         30,   30,   42),
        new SeedEntry("Fix flaky unit tests in auth module",     "Work", null,                                         35, null, null),
        new SeedEntry("Design new onboarding flow",              "Work", "Coordinate with UX before implementation.",  40,   42,   60),
        new SeedEntry("Set up CI/CD pipeline improvements",      "Work", null,                                         45,   47, null),
        new SeedEntry("Migrate database to PostgreSQL 16",       "Work", "Check for deprecated functions first.",      50, null,   90),
        new SeedEntry("Write ADR for caching strategy",          "Work", null,                                         55,   56, null),
        new SeedEntry("Refactor authentication middleware",      "Work", "Remove legacy session handling.",             60,   63,   80),
        new SeedEntry("Performance audit of homepage",           "Work", null,                                         65, null,  100),
        new SeedEntry("Conduct user research sessions",          "Work", "Book meeting room B for 4 sessions.",        70,   72, null),
        new SeedEntry("Update dependencies to latest stable",    "Work", null,                                         75,   76, null),
        new SeedEntry("Resolve security vulnerability in OAuth2","Work", "Reported in security audit 2026-03.",        80,   81,   95),
        new SeedEntry("Write test coverage for payment module",  "Work", null,                                         85, null,  110),
        new SeedEntry("Plan sprint 14 backlog",                  "Work", null,                                         90,   91, null),
        new SeedEntry("Review pull request from Maria",          "Work", null,                                         95,   95, null),
        new SeedEntry("Fix broken staging environment",          "Work", "Check Docker volume mounts first.",         100,  101, null),
        new SeedEntry("Document REST API endpoints",             "Work", null,                                        105, null,  140),
        new SeedEntry("Set up Sentry error tracking",            "Work", null,                                        110,  112, null),
        new SeedEntry("Implement rate limiting on API",          "Work", "Use token bucket algorithm.",               115, null,  160),
        new SeedEntry("Sync with design team on new mockups",    "Work", null,                                        120,  121, null),
        new SeedEntry("Review accessibility of dashboard",       "Work", "Reference WCAG 2.1 AA criteria.",           125, null,  170),
        new SeedEntry("Finalise v2.0 feature list",              "Work", null,                                        130,  131,  180),
        new SeedEntry("Update README with local setup steps",    "Work", null,                                        135,  136, null),
        new SeedEntry("Fix TypeScript strict mode errors",       "Work", null,                                        140, null, null),
        new SeedEntry("Prepare Q2 engineering report",           "Work", "Include velocity and incident metrics.",    142, null,  150),

        // ── Personal ──────────────────────────────────────────────────────────

        new SeedEntry("Morning jog 5 km",                        "Personal", null,                                      2,    2, null),
        new SeedEntry("Book dentist appointment",                 "Personal", "Ask about the crown replacement.",        2,    8, null),
        new SeedEntry("Buy birthday gift for Sarah",              "Personal", null,                                      6,   12,   25),
        new SeedEntry("Renew car registration",                   "Personal", null,                                      7,    7,   40),
        new SeedEntry("Start reading Atomic Habits",              "Personal", null,                                     10,   13, null),
        new SeedEntry("Plan weekend camping trip",                "Personal", "Check weather forecast for April.",      15, null, null),
        new SeedEntry("Call mum",                                 "Personal", null,                                     16,   16, null),
        new SeedEntry("Meditate for 10 minutes",                  "Personal", null,                                     18,   18, null),
        new SeedEntry("Cook new pasta recipe",                    "Personal", null,                                     22,   23, null),
        new SeedEntry("Sign up for gym membership",               "Personal", null,                                     28, null, null),
        new SeedEntry("Fix leaky tap in kitchen",                 "Personal", null,                                     32,   35, null),
        new SeedEntry("Organise photo library",                   "Personal", "Sort by year and event.",                38, null, null),
        new SeedEntry("Cancel unused subscriptions",              "Personal", null,                                     44,   45, null),
        new SeedEntry("Schedule eye test",                        "Personal", null,                                     50, null, null),
        new SeedEntry("Refill prescription",                      "Personal", null,                                     56,   57, null),
        new SeedEntry("Deep clean the apartment",                 "Personal", null,                                     62,   63, null),
        new SeedEntry("Update emergency contact info",            "Personal", null,                                     68, null, null),
        new SeedEntry("Learn 5 new Spanish words",                "Personal", null,                                     72,   73, null),
        new SeedEntry("Backup photos to hard drive",              "Personal", null,                                     78,   78, null),
        new SeedEntry("Register for local 5K race",               "Personal", "Race is on May 10th.",                   83,   84,  120),
        new SeedEntry("Sort out old clothes for donation",        "Personal", null,                                     88,   90, null),
        new SeedEntry("Write gratitude journal",                  "Personal", null,                                     93,   94, null),
        new SeedEntry("Book flight for summer holiday",           "Personal", "Compare prices across carriers.",        98,   99,  200),
        new SeedEntry("Renew passport",                           "Personal", "Allow 6 weeks processing time.",        103, null,  180),
        new SeedEntry("Call insurance company",                   "Personal", null,                                    108,  109, null),
        new SeedEntry("Fix broken drawer in bedroom",             "Personal", null,                                    113, null, null),
        new SeedEntry("Visit grandparents",                       "Personal", null,                                    118,  119, null),
        new SeedEntry("Try meditation app for a week",            "Personal", null,                                    123,  124, null),
        new SeedEntry("Meal prep for the week",                   "Personal", null,                                    128,  128, null),
        new SeedEntry("Research new laptop models",               "Personal", "Budget around £1200.",                  133, null, null),
        new SeedEntry("Cancel gym trial",                         "Personal", null,                                    138,  139, null),
        new SeedEntry("Review savings account rates",             "Personal", null,                                    142, null, null),

        // ── Shopping ──────────────────────────────────────────────────────────

        new SeedEntry("Buy olive oil",                           "Shopping", null,                                     14,   14, null),
        new SeedEntry("Restock coffee beans",                    "Shopping", null,                                     14,   15, null),
        new SeedEntry("New running shoes",                       "Shopping", null,                                     20, null, null),
        new SeedEntry("HDMI cable",                              "Shopping", null,                                     21,   22, null),
        new SeedEntry("Vitamin D supplements",                   "Shopping", null,                                     28, null, null),
        new SeedEntry("New desk lamp",                           "Shopping", "Something warm and dimmable.",           35,   36, null),
        new SeedEntry("Protein powder",                          "Shopping", null,                                     42,   42, null),
        new SeedEntry("Replacement phone case",                  "Shopping", null,                                     50, null, null),
        new SeedEntry("Bluetooth headphones",                    "Shopping", "Check reviews on Rtings first.",         58,   59, null),
        new SeedEntry("Kitchen sponges",                         "Shopping", null,                                     65,   65, null),
        new SeedEntry("New notebook for work",                   "Shopping", null,                                     72, null, null),
        new SeedEntry("USB-C hub",                               "Shopping", "Need at least 3 ports and HDMI out.",    80,   81, null),
        new SeedEntry("Hand cream",                              "Shopping", null,                                     88,   89, null),
        new SeedEntry("Shower gel refill",                       "Shopping", null,                                     95,   95, null),
        new SeedEntry("New door mat",                            "Shopping", null,                                    103, null, null),
        new SeedEntry("AAA batteries",                           "Shopping", null,                                    110,  110, null),
        new SeedEntry("Sunscreen SPF50",                         "Shopping", null,                                    120, null, null),
        new SeedEntry("Birthday card for Dad",                   "Shopping", null,                                    128,  129,  135),
        new SeedEntry("Printer ink cartridges",                  "Shopping", "Check model number before ordering.",   135, null, null),
        new SeedEntry("New water bottle",                        "Shopping", null,                                    140,  141, null),

        // ── No list (floaters) ────────────────────────────────────────────────

        new SeedEntry("Read a chapter of the book",              null, null,                                           4,    4, null),
        new SeedEntry("Tidy up the home office",                 null, null,                                           9,   10, null),
        new SeedEntry("Back up laptop files",                    null, null,                                          23,   23, null),
        new SeedEntry("Write journal entry",                     null, null,                                          32, null, null),
        new SeedEntry("Watch documentary on climate change",     null, null,                                          45,   46, null),
        new SeedEntry("Try a new coffee shop in town",           null, null,                                          60,   61, null),
        new SeedEntry("Listen to new album by favourite artist", null, null,                                          75,   75, null),
        new SeedEntry("Reply to old email from Mike",            null, null,                                          85,   85, null),
        new SeedEntry("Learn IntelliJ keyboard shortcuts",       null, null,                                         100, null, null),
        new SeedEntry("Draft birthday message for team Slack",   null, null,                                         115,  115, null),
        new SeedEntry("Watch tutorial on Docker networking",     null, "Understand bridge vs host vs overlay.",      130, null, null),
        new SeedEntry("Sign up for tech newsletter",             null, null,                                         142, null, null)
    );

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail(SEED_EMAIL).isPresent()) {
            log.info("DataSeeder: seed user already exists — skipping.");
            return;
        }

        User user                 = createSeedUser();
        Map<String, Long> listIds = createSeedLists(user.getId());
        createSeedTasks(user.getId(), listIds);

        log.info("============================================================");
        log.info("DataSeeder: seed data inserted ({} tasks).", SEED_TASKS.size());
        log.info("  email:    {}", SEED_EMAIL);
        log.info("  password: {}", SEED_PASSWORD);
        log.info("============================================================");
    }

    // -------------------------------------------------------------------------
    // User
    // -------------------------------------------------------------------------

    private User createSeedUser() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        User user = User.builder()
                .email(SEED_EMAIL)
                .passwordHash(passwordEncoder.encode(SEED_PASSWORD))
                .displayName("Seed User")
                .emailVerified(true)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return userRepository.save(user);
    }

    // -------------------------------------------------------------------------
    // Task lists
    // -------------------------------------------------------------------------

    private Map<String, Long> createSeedLists(Long userId) {
        TaskList work     = createList(userId, "Work",     "#6366f1");
        TaskList personal = createList(userId, "Personal", "#10b981");
        TaskList shopping = createList(userId, "Shopping", "#f59e0b");

        patchListTimestamp(work.getId(),     day(0,  9,  0));
        patchListTimestamp(personal.getId(), day(0,  9,  5));
        patchListTimestamp(shopping.getId(), day(14, 10, 15));

        return Map.of(
                "Work",     work.getId(),
                "Personal", personal.getId(),
                "Shopping", shopping.getId()
        );
    }

    private TaskList createList(Long userId, String name, String color) {
        CreateTaskListRequest req = new CreateTaskListRequest();
        req.setName(name);
        req.setColor(color);
        return taskListService.createTaskList(userId, req);
    }

    private void patchListTimestamp(Long listId, OffsetDateTime createdAt) {
        jdbcTemplate.update(
                "UPDATE task_lists SET created_at = ?, updated_at = ? WHERE id = ?",
                createdAt, createdAt, listId);
    }

    // -------------------------------------------------------------------------
    // Tasks
    // -------------------------------------------------------------------------

    private void createSeedTasks(Long userId, Map<String, Long> listIds) {
        for (SeedEntry entry : SEED_TASKS) {
            CreateTaskRequest req = new CreateTaskRequest();
            req.setTitle(entry.title());
            req.setNotes(entry.notes());
            req.setListId(entry.list() != null ? listIds.get(entry.list()) : null);
            req.setDueDate(entry.dueDay() != null ? SEED_START.plusDays(entry.dueDay()) : null);

            Long taskId = taskService.createTask(userId, req).getId();
            patchTaskTimestamps(taskId, entry.createdDay(), entry.completedDay());
        }
    }

    private void patchTaskTimestamps(Long taskId, int createdDay, Integer completedDay) {
        OffsetDateTime createdAt   = toDateTime(createdDay);
        OffsetDateTime completedAt = completedDay != null ? completionDateTime(completedDay) : null;
        OffsetDateTime updatedAt   = completedAt != null ? completedAt : createdAt;

        jdbcTemplate.update(
                "UPDATE tasks SET created_at = ?, updated_at = ?, completed_at = ? WHERE id = ?",
                createdAt, updatedAt, completedAt, taskId);
    }

    // -------------------------------------------------------------------------
    // Date helpers
    // -------------------------------------------------------------------------

    /** Returns a timestamp at a specific time on the given day offset. */
    private OffsetDateTime day(int dayOffset, int hour, int minute) {
        return SEED_START.plusDays(dayOffset).atTime(hour, minute).atOffset(ZoneOffset.UTC);
    }

    /**
     * Returns a varied creation timestamp for the given day offset.
     * Hour (08:00–16:00) and minute are derived from the offset so that tasks
     * on the same day land at slightly different times.
     */
    private OffsetDateTime toDateTime(int dayOffset) {
        int hour   = 8 + (dayOffset % 9);   // 08:00 – 16:00
        int minute = (dayOffset * 7) % 60;
        return SEED_START.plusDays(dayOffset).atTime(hour, minute).atOffset(ZoneOffset.UTC);
    }

    /**
     * Returns a fixed end-of-day completion timestamp (18:00 UTC).
     * Using a fixed time — rather than re-using toDateTime — ensures completedAt
     * is always after createdAt, including tasks completed on the same day they
     * were created.
     */
    private OffsetDateTime completionDateTime(int dayOffset) {
        return SEED_START.plusDays(dayOffset).atTime(18, 0).atOffset(ZoneOffset.UTC);
    }
}
