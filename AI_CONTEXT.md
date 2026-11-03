# AI Context for SimpleWallet (KMP & Gradle)

## Technical Stack & Frameworks
- **Platform:** Kotlin Multiplatform (KMP) structured with stable Gradle Kotlin DSL.
- **UI Framework:** Compose Multiplatform (shared UI for all targets).
- **Architecture:** Clean Architecture pattern separated into standard layers: `data`, `domain`, and `ui`.
- **UI Pattern:** MVVM utilizing the official `androidx.lifecycle.ViewModel`.
- **Core Libraries:**
    - Navigation: Voyager (KMP compatible)
    - Dependency Injection: Koin
    - Database: Room (KMP edition for local storage)
    - Analytics & Charts: Drawn using pure Compose Multiplatform Canvas element (strictly NO heavy external charting libraries).

## App Requirements (Pure Manual Wallet)
The application is a lightweight, local-first finance tracker. There are NO background services, NO automatic SMS/Push parsing, and NO network AI integrations.
1. Manual transaction input (Amount in local currencies like KZT/TRY, category selection, date picker, type: Income/Expense).
2. Local data persistence on the device using Room database.
3. Clean monthly analytical charts (Pie chart for expenses share, bar chart for daily income/expenses breakdown).

## Code Generation Rules for AI
1. All business logic, UI screens, models, and databases MUST be written strictly inside the `shared/src/commonMain/kotlin` directory.
2. DO NOT modify or add code inside `shared/src/androidMain`, `shared/src/iosMain`, or any target-specific folders unless explicitly requested.
3. Use only cross-platform libraries listed above. Do not use Android-specific components (e.g., Toast, Intent) or iOS-specific frameworks directly.
4. Keep the code architecture clean, modular, and performant for an MVP stage.
