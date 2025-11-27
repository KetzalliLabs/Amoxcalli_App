# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Amoxcalli** is an Android mobile app for learning Mexican Sign Language (LSM - Lengua de Señas Mexicana) through micro-lessons, interactive exercises, and gamification. The app uses video content, spaced repetition, and a Duolingo-style learning path with offline support.

**Tech Stack:**
- **Language:** Kotlin
- **UI:** Jetpack Compose with Material 3
- **Architecture:** MVVM with Clean Architecture patterns
- **Backend:** REST API (Railway deployment)
- **Authentication:** Firebase Auth + Google Sign-In
- **Networking:** Retrofit + OkHttp
- **Image Loading:** Coil
- **Database:** Room (for offline support)
- **Navigation:** Jetpack Navigation Compose

**Package:** `com.req.software.amoxcalli_app`

---

## Build & Development Commands

### Building the App
```bash
# Clean and build
./gradlew clean build

# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

### Running Tests
```bash
# Run unit tests
./gradlew test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest
```

### Debugging
```bash
# View logs from connected device
adb logcat

# Filter logs for Amoxcalli app
adb logcat | grep -i "amoxcalli"

# Clear app data
adb shell pm clear com.req.software.amoxcalli_app
```

### Code Quality
```bash
# Run lint checks
./gradlew lint

# Generate lint report (outputs to app/build/reports/lint-results.html)
./gradlew lintDebug
```

---

## Architecture Overview

### Layer Structure

The app follows **Clean Architecture** with clear separation of concerns:

```
app/src/main/java/com/req/software/amoxcalli_app/
├── MainActivity.kt                 # Entry point, sets up navigation
├── ui/                            # Presentation layer
│   ├── screens/                   # Screen composables
│   │   ├── home/                  # Home screen with stats & recommendations
│   │   ├── learnScreen/           # Learning path screens
│   │   ├── library/               # Dictionary/library screens
│   │   ├── exercises/             # Interactive games & exercises
│   │   │   ├── games/             # Specific game types (VideoToText, ImageToText, etc.)
│   │   │   ├── common/            # Shared game components
│   │   │   └── models/            # Exercise UI models
│   │   └── profile/               # User profile & settings
│   ├── components/                # Reusable UI components
│   │   ├── buttons/               # Custom buttons
│   │   ├── cards/                 # Card components
│   │   ├── navigation/            # Navigation bars
│   │   └── headers/               # Header components
│   ├── navigation/                # Navigation graphs
│   └── theme/                     # Material 3 theme configuration
├── viewmodel/                     # ViewModels for state management
├── domain/                        # Domain layer
│   ├── model/                     # Domain models (UserProfile, Lesson, etc.)
│   └── usecase/                   # Business logic use cases
├── data/                          # Data layer
│   ├── repository/                # Repository implementations
│   └── dto/                       # Data Transfer Objects for API
├── service/                       # Service interfaces
├── network/                       # Retrofit & networking
├── config/                        # Configuration objects
└── analytics/                     # Analytics tracking
```

### Key Architectural Patterns

#### 1. Navigation Architecture
- **Two-level navigation:**
  - Top level: `MainActivity` handles Login → AppNavigation routing
  - App level: `AppNavigation` (NavGraph.kt) manages main app screens (Home, Learn, Library, Quiz, Profile)
- **Bottom navigation** shown on main screens only
- Legacy routes maintained for backward compatibility (MainMenu, Lesson screens)

#### 2. Authentication Flow
1. User signs in via **Google Sign-In**
2. Google credentials authenticate with **Firebase Auth** to get `firebase_uid`
3. Firebase UID + user data sent to **backend API** (`/api/auth/login`)
4. Backend creates/updates user record and returns user data + stats
5. Firebase ID token stored for subsequent API calls (Bearer token)

**Key files:**
- `viewmodel/AuthViewModel.kt` - Auth state management
- `service/AuthService.kt` - Auth API interface
- `config/FirebaseConfig.kt` - Firebase configuration
- `ui/screens/LoginScreen.kt` - Login UI

#### 3. Data Flow Pattern
```
UI Screen → ViewModel → Repository → Service/API → Backend
                ↓
         StateFlow/State
                ↓
         UI Updates (Compose)
```

- **ViewModels** expose `StateFlow` for reactive UI updates
- **Repositories** handle caching and data source coordination
- **Services** define API contracts (Retrofit interfaces)
- **DTOs** map between API responses and domain models

#### 4. Exercise System
The app features multiple exercise types for LSM learning:

- **VIDEO_TO_TEXT**: Watch LSM video → select matching word
- **IMAGE_TO_TEXT**: View image → select matching word
- **WORD_TO_SIGN**: Given word → select correct LSM sign

**ExerciseRouter** dynamically loads exercises based on type. Game screens are composable and reusable with different UI states (`LearnGameUiState`).

---

## Configuration Files

### API Configuration
**File:** `config/ApiConfig.kt`

```kotlin
const val BASE_URL = "https://ketzallidbapi-production.up.railway.app/api/"
```

Update this URL to point to your backend. For local development:
- **Android Emulator:** Use `http://10.0.2.2:PORT/api/`
- **Physical Device:** Use your computer's local IP (e.g., `http://192.168.1.100:PORT/api/`)

### Firebase Configuration
**File:** `config/FirebaseConfig.kt`

Replace `WEB_CLIENT_ID` with your Firebase Web Client ID from Firebase Console → Authentication → Sign-in method → Google → Web SDK configuration.

**File:** `app/google-services.json`

Ensure this file exists with your Firebase project configuration. Download from Firebase Console → Project Settings → Add App → Download google-services.json.

---

## Important Implementation Notes

### Video & Media Handling
- LSM videos stored on Cloudflare R2 CDN
- Video URLs use format: `https://pub-05700fc259bc4e839552241871f5e896.r2.dev/{filename}`
- Use Coil for image loading with proper error handling
- Implement video caching for offline mode

### JSON Parsing Pattern
Library/dictionary data parsed from JSON responses. Key parsers:
- `LibraryJsonParser.kt` - Parses word/sign data
- `CategoryJsonParser.kt` - Parses category data

Pattern: JSON response → parse to UI models → display in composables

### State Management
- **ViewModels** hold UI state using `StateFlow`
- Use `collectAsState()` in composables to observe state
- **Example:**
  ```kotlin
  val userStats by viewModel.userStats.collectAsState()
  ```

### Offline Support (Planned)
- Room database for local caching
- Sync mechanism with Last-Write-Wins (LWW) strategy
- Download units for offline use
- See README.md for offline roadmap

---

## Common Development Patterns

### Adding a New Screen
1. Create screen composable in `ui/screens/{feature}/`
2. Add route to `navigation/Screen.kt`
3. Register route in `AppNavigation` (NavGraph.kt)
4. Create ViewModel if needed in `viewmodel/`
5. Add navigation calls from other screens

### Adding a New API Endpoint
1. Define DTOs in `data/dto/`
2. Add endpoint to service interface (e.g., `service/UserService.kt`)
3. Create/update repository in `data/repository/`
4. Update RetrofitClient if new service needed
5. Call from ViewModel with proper error handling

### Adding a New Exercise Type
1. Define question type in `LearnQuestionType` enum
2. Create game screen composable in `ui/screens/exercises/games/`
3. Update `ExerciseRouter.kt` to handle new type
4. Create UI state model in `exercises/models/`
5. Implement scoring/feedback logic

---

## Gamification System

The app includes a comprehensive gamification system:
- **XP (Experience Points):** Earned from lessons and practices
- **Cacao (Soft Currency):** Used for hints and extra exercises
- **Medallas (Badges):** Jade, Obsidiana, Quetzal, etc.
- **Streaks:** Daily login/practice tracking
- **Levels:** User progression system
- **Estelas:** Certification system for module mastery

These features are tracked via backend API and displayed in profile/home screens.

---

## Security & Privacy

- All API communication over HTTPS
- Firebase ID tokens for authenticated requests
- No sensitive data in version control (use `.env` or Firebase Remote Config for secrets)
- Rate limiting handled by backend
- WCAG 2.2 AA accessibility compliance required

---

## Testing Strategy

### Unit Tests
- Test ViewModels with mock repositories
- Test data mappers and parsers
- Test business logic in use cases

### Integration Tests
- Test API service calls with mock server
- Test repository implementations
- Test navigation flows

### UI Tests (Espresso/Compose Testing)
- Test critical user flows (login, exercise completion)
- Test accessibility features
- Test offline mode behavior

---

## Team & Context

Developed by **Ketzalli Labs** - Team #3 (Grupo 504) for university project.

**Key Requirements:**
- Performance: App start < 3s, screen loads < 500ms
- Availability: Backend > 99.9% uptime
- Crash-free sessions > 99.5%
- Accessibility: WCAG 2.2 AA compliance
- Video quality: 720p+ at 24-60 fps

---

## Current Development Branch

**Branch:** `Development`
**Main branch:** Not set (use `Development` for PRs)

Check git status before making changes. App is actively being developed - coordinate with team before major architectural changes.
