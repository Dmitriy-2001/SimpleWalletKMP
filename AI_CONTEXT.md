# AI Context for SimpleWallet (KMP & Gradle)

## 1. Technical Stack & Frameworks
- **Platform:** Kotlin Multiplatform (KMP) structured with stable Gradle Kotlin DSL.
- **UI Framework:** Compose Multiplatform (shared UI for all targets).
- **Architecture:** Clean Architecture pattern separated into standard layers: `data`, `domain`, and `ui`.
- **UI Pattern:** MVVM utilizing the official `androidx.lifecycle.ViewModel`.
- **Core Libraries:**
  - Navigation: Voyager (KMP compatible)
  - Dependency Injection: Koin
  - Database: Room (KMP edition for local storage)
  - Utilities: kotlinx-datetime, kotlinx-coroutines-core
  - Analytics & Charts: Drawn using pure Compose Multiplatform Canvas element (strictly NO heavy external charting libraries).

## 2. App Requirements (Pure Manual Wallet)
The application is a lightweight, local-first finance tracker. There are NO background services, NO automatic SMS/Push parsing, and NO network AI integrations.
1. Manual transaction input (Amount in local currencies like KZT/TRY, category selection, date picker, type: Income/Expense).
2. Local data persistence on the device using Room database.
3. Clean monthly analytical charts (Pie chart for expenses share, bar chart for daily income/expenses breakdown).

## 3. Code Generation Rules for AI
1. All business logic, UI screens, models, and databases MUST be written strictly inside the `shared/src/commonMain/kotlin` directory.
2. DO NOT modify or add code inside `shared/src/androidMain`, `shared/src/iosMain`, or any target-specific folders unless explicitly requested.
3. Use only cross-platform libraries listed above. Do not use Android-specific components (e.g., Toast, Intent) or iOS-specific frameworks directly.
4. Keep the code architecture clean, modular, and performant for an MVP stage.

---

## 4. Master Execution Plan (Step-by-Step Instructions)

### Шаг 1: Настройка зависимостей Gradle
- **Задача:** Интегрировать библиотеки проекта без удаления базовых зависимостей.
- **Инструкция для ИИ:**
  1. Добавь следующие строки в `gradle/libs.versions.toml`:
    - Версии: `koin = "4.0.2"`, `voyager = "1.1.0-beta03"`, `room = "2.7.0-alpha13"`, `sqlite = "2.5.0-alpha13"`, `ksp = "2.0.21-1.0.28"`, `kotlinx-datetime = "0.6.2"`.
    - Библиотеки: `koin-core`, `koin-compose`, `koin-compose-viewmodel` (все от `io.insert-koin`); `voyager-navigator`, `voyager-screenmodel`, `voyager-tab-navigator`, `voyager-koin` (все от `cafe.adriel.voyager`); `androidx-room-runtime`, `androidx-room-compiler` (от `androidx.room`); `sqlite-bundled` (от `androidx.sqlite`); `kotlinx-datetime`.
    - Плагины: `ksp` (`com.google.devtools.ksp`), `androidx-room` (`androidx.room`).
  2. В файле `shared/build.gradle.kts` подключи плагины `ksp` и `androidx.room`. Добавь все библиотеки в блок `commonMain.dependencies`. Настрой блок `room {}` указав `schemaDirectory(project.projectDir.path + "/schemas")`.
  3. Выполни Sync Project.

### Шаг 2: База данных Room и сущности
- **Задача:** Развернуть слой хранения данных на устройстве.
- **Инструкция для ИИ:**
  1. В `shared/src/commonMain/kotlin/.../data/database/entity/` создай `TransactionEntity` (id, amount, categoryId, type, dateLong, note), `CategoryEntity` (id, name, colorHex, type, isCustom).
  2. В папке `dao/` создай интерфейсы `TransactionDao` и `CategoryDao` с базовыми `@Query`, `@Insert`, `@Delete` методами.
  3. Создай абстрактный класс `AppDatabase : RoomDatabase` со встроенным `DatabaseConstructor` через аннотацию `@ConstructedBy`. Пропиши автоматический запуск пре-заполнения (seed) базовыми категориями (Продукты, Кафе, Зарплата и т.д.) при первом старте.

### Шаг 3: Domain-слой и репозитории
- **Задача:** Создать чистые бизнес-модели и абстракции без привязки к Room.
- **Инструкция для ИИ:**
  1. В папке `domain/model/` создай чистые Kotlin дата-классы: `Transaction`, `Category`, `TransactionType` (Enum: INCOME, EXPENSE).
  2. В папке `domain/repository/` создай интерфейсы `TransactionRepository` (возвращает `Flow<List<Transaction>>`) и `CategoryRepository`.
  3. В папке `data/mapper/` напиши функции-расширения для маппинга классов `Entity <-> Domain`.
  4. В папке `data/repository/` напиши реализации `TransactionRepositoryImpl` и `CategoryRepositoryImpl`, которые работают напрямую через созданные ранее Room DAO.

### Шаг 4: Бизнес-сценарии (UseCases)
- **Задача:** Инкапсулировать логику приложения в атомарные UseCase классы.
- **Инструкция для ИИ:**
  1. Создай `AddTransactionUseCase`, `GetTransactionsUseCase` (с параметрами фильтрации по датам и типам), `DeleteTransactionUseCase`.
  2. Создай `AddCustomCategoryUseCase` (с валидацией на пустые поля перед сохранением) и `GetCategoriesUseCase`.
  3. Создай аналитические UseCase-ы: расчет баланса, группировка транзакций по дням для столбчатого графика, и расчет процентных долей категорий расходов для круговой диаграммы.

### Шаг 5: Настройка DI через Koin
- **Задача:** Связать все слои приложения в единый граф зависимостей.
- **Инструкция для ИИ:**
  1. В папке `di/` создай файлы `DatabaseModule.kt`, `RepositoryModule.kt`, `UseCaseModule.kt`, `ViewModelModule.kt`.
  2. Объедини их в общую функцию `fun initKoin(appDeclaration: KoinAppDeclaration = {})` в файле `di/Koin.kt`. Обеспечь корректный проброс фабрик для Room и ViewModels.

### Шаг 6: Дизайн-система и базовые UI компоненты
- **Задача:** Создать визуальный фундамент приложения на Material 3.
- **Инструкция для ИИ:**
  1. Настрой `Color.kt` и `Theme.kt` в `presentation/theme/`.
  2. В `presentation/components/` создай: `AmountInputField` (удобный ввод цифр), `CategoryChip` (чип выбора категории транзакции), `DatePickerModal` (выбор даты), `TransactionItemRow` (строка отображения транзакции в списке).

### Шаг 7: Графики аналитики на Compose Canvas
- **Задача:** Реализовать отрисовку графиков вручную на Canvas без библиотек.
- **Инструкция для ИИ:**
  1. В `presentation/charts/PieChartCanvas.kt` напиши Composable функцию, которая принимает список долей категорий и через `drawArc` внутри `Canvas` рисует круговую диаграмму расходов.
  2. В `BarChartCanvas.kt` напиши отрисовку столбчатого графика доходов и расходов по дням через `drawRect`. Реализуй автоматическое масштабирование высоты столбцов по максимальному значению.
  3. Напиши компонент `ChartLegend` для вывода расшифровки цветов.

### Шаг 8: Навигация Voyager и нижнее меню
- **Задача:** Настроить многовкладочный интерфейс приложения.
- **Инструкция для ИИ:**
  1. Создай `MainTabScreen : Screen`, который инициализирует Voyager `TabNavigator`.
  2. Реализуй три таба как объекты, наследуемые от `Tab`: `TransactionsTab`, `AnalyticsTab`, `CategoriesTab`. Настрой нижнюю панель переключения `NavigationBar` с иконками Material 3.

### Шаг 9: Экран истории и форма создания транзакций
- **Задача:** Реализовать первый боевой экран приложения.
- **Инструкция для ИИ:**
  1. Создай `TransactionsScreen` (UI), `TransactionsViewModel` и дата-класс `TransactionsState`. Реализуй вывод списка транзакций, кнопки фильтрации периодов и красивый Empty State.
  2. Создай экран `AddEditTransactionScreen` с формой ввода (сумма, выбор типа дохода/расхода, выбор даты и категории). Подключи валидацию: если сумма равна 0 или категория не выбрана, кнопка сохранения должна быть заблокирована.

### Шаг 10: Экран аналитики за месяц
- **Задача:** Интегрировать графики с бизнес-логикой.
- **Инструкция для ИИ:**
  1. Создай `AnalyticsScreen`, `AnalyticsViewModel`, `AnalyticsState`.
  2. Размести вверху экрана переключатель месяцев (вперед/назад). Ниже выведи общие карточки: "Доходы", "Расходы", "Остаток".
  3. Интегрируй в экран созданные ранее `PieChartCanvas` и `BarChartCanvas`, скармливая им данные из аналитических UseCase-ов.

### Шаг 11: Управление кастомными категориями
- **Задача:** Позволить пользователю управлять категориями расходов/доходов.
- **Инструкция для ИИ:**
  1. Создай `CategoriesScreen`, `CategoriesViewModel`, `CategoriesState`.
  2. Реализуй отображение сетки текущих категорий и кнопку "Создать категорию".
  3. При нажатии открывай диалоговое окно или экран, где пользователь вводит имя и выбирает цвет категории из палитры, после чего она сохраняется в Room.

### Шаг 12: Полировка и финальная проверка
- **Задача:** Финальный аудит, чистка кода и запуск MVP.
- **Инструкция для ИИ:**
  1. Напиши базовые Unit-тесты для проверки логики `AddTransactionUseCase` и вычислений во `ViewModels`.
  2. Проверь обработку всех крайних состояний: нулевые балансы, длинные названия категорий, корректное отображение знака валюты "₸". Убедись, что проект компилируется под Android и готов к iOS.
