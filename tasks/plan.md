# Implementation Plan: Daily Hisab (দৈনিক হিসাব)

## Overview
Personal finance/expense tracking Android app in Bangla. Users can track daily income/expenses, manage categories, view reports, secure with email auth + biometric (Face ID/Fingerprint), full Bangla UI.

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3 (Material You)
- **Architecture:** MVVM + Clean Architecture
- **Database:** Room (SQLite) - local storage
- **Auth:** Firebase Authentication (email/password)
- **Security:** Android Biometric API (Face ID / Fingerprint)
- **Settings:** DataStore Preferences
- **Charts:** MPAndroidChart or Vico
- **DI:** Hilt (Dagger)
- **Build:** Gradle + GitHub Actions CI/CD

## Architecture
```
app/
├── data/
│   ├── local/        (Room DAO, entities)
│   ├── repository/   (Repository pattern)
│   └── auth/         (Firebase Auth wrapper)
├── domain/
│   ├── model/        (Domain models)
│   ├── repository/   (Repository interfaces)
│   └── usecase/      (Business logic)
├── ui/
│   ├── auth/         (Login/Register screens)
│   ├── home/         (Dashboard/Main screen)
│   ├── transactions/ (Add/Edit/List transactions)
│   ├── categories/   (Category management)
│   ├── reports/      (Charts & Reports)
│   ├── settings/     (Settings screen)
│   └── components/   (Shared composables)
├── security/         (Biometric auth helper)
├── localization/     (Bangla strings)
└── di/              (Hilt modules)
```

## Features
1. **Authentication** — Email/password login via Firebase
2. **Biometric Security** — Face ID / Fingerprint lock for app
3. **Dashboard** — Today's summary, recent transactions, balance
4. **Transactions** — Add/edit/delete income & expense entries
5. **Categories** — Default categories + user can add/edit/delete
6. **Reports** — Daily/weekly/monthly/yearly charts & summaries
7. **Settings** — Theme toggle, category management, profile, security settings
8. **Bangla Localization** — Full UI in Bangla (বাংলা)

## Task List

### Phase 1: Project Setup & Foundation
- [ ] Task 1: Create Android project structure (Gradle, manifest, etc.)
- [ ] Task 2: Set up Hilt DI, Room database, DataStore
- [ ] Task 3: Set up Firebase Auth integration
- [ ] Task 4: Set up GitHub Actions for APK build

### Phase 2: Core Data Layer
- [ ] Task 5: Define Room entities & DAOs (User, Transaction, Category)
- [ ] Task 6: Define repositories & use cases
- [ ] Task 7: Firebase Auth repository

### Phase 3: UI - Auth & Security
- [ ] Task 8: Login/Register screens (Bangla UI)
- [ ] Task 9: Biometric (Face ID/Fingerprint) lock screen

### Phase 4: UI - Main Features
- [ ] Task 10: Dashboard/Home screen
- [ ] Task 11: Transaction add/edit screen
- [ ] Task 12: Transaction list screen
- [ ] Task 13: Category management screen (CRUD)
- [ ] Task 14: Reports & Charts screen
- [ ] Task 15: Settings screen

### Phase 5: Polish & Localization
- [ ] Task 16: Full Bangla localization (strings.xml)
- [ ] Task 17: Material 3 theming (Light/Dark)
- [ ] Task 18: GitHub repo creation & push

## Risks
| Risk | Impact | Mitigation |
|------|--------|------------|
| Firebase setup needs user's google-services.json | High | User must create Firebase project |
| Can't compile locally (no Android SDK) | Medium | GitHub Actions will handle build |
| Biometric (Face ID) hardware dependent | Low | Graceful fallback to PIN |

## Open Questions
- Firebase project user nijei create korben? Ami `google-services.json` placeholder rakhbo.
- App icon/logo ki dorkar?
- First version e ki ki feature immediate chai?
