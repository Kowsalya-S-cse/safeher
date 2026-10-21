# SafeHer 🛡️ - AI-Powered Smart Women Safety System

> 📺 **[Watch Live Demo Video](https://drive.google.com/file/d/15LBFj9d9LqmQVHRrfts4xkvL0sBX-19P/view?usp=sharing)** — See SafeHer in action with real-time SOS, AI threat assessment, and GPS tracking.

SafeHer is a real-time, proactive personal safety Android application built with Jetpack Compose, Kotlin Coroutines, Room Database, and Google Gemini AI. It provides automated threat assessment, discrete distress detection, emergency SOS alerts, and live GPS tracking to keep women safe anywhere.

---

## ✨ Features

- **🚨 Instant SOS & Emergency Network**: One-tap trigger with automatic emergency siren, countdown overlay to prevent accidental triggers, and automatic dispatch to trusted contacts.
- **🤖 Gemini AI Threat Assessment**: AI-driven analysis evaluating contextual risk signals, surroundings, and audio indicators.
- **🎙️ Discrete Distress Recognition**: Background audio monitoring designed to detect distress keywords and rapid acoustic anomalies.
- **📍 Live GPS Tracking & Safe Zones**: Real-time geolocation tracking with safe-zone geofencing (home, campus, workplace) and safe-route sharing.
- **📞 Discreet Fake Call Simulation**: Realistic incoming call simulator designed to help users gracefully exit uncomfortable or suspicious situations.
- **⌚ Wearable & Sensor Anomaly Detection**: Integration with wearable triggers, sudden impact detection, and rapid movement anomalies.
- **📴 Offline Resilience**: Local SQLite Room Database storing emergency profiles, offline contacts, and incident logs with automatic cloud synchronization when connectivity is restored.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.0+
- **UI Framework**: Modern Jetpack Compose with Material 3 & custom dark/light theme
- **Architecture**: Clean Architecture + MVVM (Model-View-ViewModel) + StateFlow
- **Database**: Android Jetpack Room DB (Local persistence)
- **AI Integration**: Google Gemini API via Generative AI Android SDK
- **Async & Concurrency**: Kotlin Coroutines & Flow
- **Build System**: Gradle Kotlin DSL (`build.gradle.kts`)

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio Ladybug (2024.2+)** or **VS Code / Antigravity IDE** with Android SDK
- **Android SDK Platform 34+**
- **JDK 17 or higher**
- A connected Android device or Android Emulator (e.g. Pixel 8 / Pixel 9 with Google Play Services)

### Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Kowsalya-S-cse/SafeHer.git
   cd SafeHer
   ```

2. **Configure API Keys**:
   - Copy `.env.example` to `.env`:
     ```bash
     copy .env.example .env
     ```
   - Add your Google Gemini API Key inside `.env`:
     ```env
     GEMINI_API_KEY=your_actual_gemini_api_key_here
     ```

3. **Build and Run**:
   - Using Gradle:
     ```bash
     ./gradlew installDebug
     ```
   - Or run the included launch script:
     ```cmd
     run.bat
     ```

---

## 📁 Project Structure

```text
SafeHer/
├── app/
│   ├── src/main/java/com/example/
│   │   ├── data/             # Local Room DB, DAOs, Entities, API services
│   │   ├── domain/           # Models, State, Location & Sensor services
│   │   ├── ui/               # Jetpack Compose UI (Screens, Theme, Components)
│   │   └── MainActivity.kt   # App entry point & navigation host
│   └── build.gradle.kts      # App-level dependencies & build configuration
├── gradle/                   # Gradle wrapper configuration
├── .gitignore                # Git ignore configuration
├── run.bat                   # 1-click emulator launch & install script
└── README.md
```

---

## 🔒 Privacy & Permissions

SafeHer prioritizes user privacy. Sensitive permissions (Microphone, Fine Location, SMS) are strictly requested on-demand, processed on-device whenever possible, and never shared with unauthorized third parties.

---

## 📄 License

This project is created for educational, research, and personal safety development.
