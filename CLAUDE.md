# CLAUDE.md

## Specs & Design Resources

| Resource                          | Path                                                 |
|-----------------------------------|------------------------------------------------------|
| Feature requirements & edge cases | `specs/habit-tracker-design.md`                      |
| UI mockups & design system        | `specs/Habit Tracker — Design System & Screens.html` |
| Inter variable font               | `specs/Fonts/Inter/Inter-VariableFont_opsz,wght.ttf` |
| Manrope variable font             | `specs/Fonts/Manrope/Manrope-VariableFont_wght.ttf`  |

**Before writing any UI code**: read the HTML mockup file to get exact colors, typography, spacing,
and component shapes.  
**Before implementing any feature**: read `habit-tracker-design.md` for requirements and edge cases.

---

## Architecture

Single-module Android Compose app. Follow these patterns consistently:

**Presentation — MVI**

- One `UiState` sealed class, one `UiEvent` sealed class, one `UiEffect` sealed class per screen
- ViewModel exposes `StateFlow<UiState>`; side-effects go through a `SharedFlow<UiEffect>`
- Composables are stateless; they receive state and emit events

**Navigation**

- Compose Navigation with `NavHost` and typed routes; single `MainActivity`
- Define all routes in a central `AppNavigation` composable

**Data layer**

- Room for local persistence; DAO functions return `Flow` or `suspend`
- Repository is the single source of truth; ViewModels never touch the DAO directly

**Dependency Injection — Koin**

- Separate Koin modules per layer (data, domain if present, presentation)
- Inject ViewModels via `koinViewModel()`; no manual factory boilerplate

**Async**

- Kotlin Coroutines throughout; `viewModelScope` for ViewModel work
- Prefer `Flow` over callbacks; collect in composables with `collectAsStateWithLifecycle`

---

## Build Commands

```bash
# Windows
gradlew.bat build
gradlew.bat installDebug
gradlew.bat test
gradlew.bat connectedAndroidTest
gradlew.bat lint
```

Add dependencies by updating `gradle/libs.versions.toml` first, then reference via `libs.*` in
`app/build.gradle.kts`.

---

## Git Workflow

- **Stage every new file immediately** — after creating any file, run `git add <path>` so nothing is
  ever left untracked at commit time.
- **Commit in meaningful, modular units** — group related changes into focused commits with
  descriptive messages (e.g. one commit per screen, per layer change, or per feature slice). Avoid
  mega-commits that bundle unrelated work.
