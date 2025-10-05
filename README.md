# To-Do List v3

A modern Kotlin-based Android application for managing your daily tasks and boosting productivity.

---

## Features

- Create, edit, and delete to-do items
- Organize tasks by categories or priorities
- Intuitive and responsive Android UI
- Local data storage (Room database)
- Secure and optimized release builds (ProGuard)
- Easy setup and dependency management (Gradle)

---

## Installation and Setup

1. **Clone the repository**
   ```sh
   git clone https://github.com/J-DPAL/To-Do-List-v3.git
   cd To-Do-List-v3
   ```

2. **Open in Android Studio**
   - File > Open > Select the project folder.

3. **Build and run**
   - Use the Gradle wrapper (`./gradlew assembleDebug`) or Android Studio's Run button to build and deploy to a device or emulator.

---

## Usage

- Launch the app on your Android device.
- Add new tasks using the '+' button.
- Tap on a task to edit or delete.
- Organize tasks as needed.

---

## Technologies Used

- **Kotlin** – All source code is written in Kotlin.
- **Android SDK** – Native Android application development.
- **Room Database** – Local data persistence.
- **Gradle (Kotlin DSL)** – Build automation and dependency management.
- **ProGuard** – Code shrinking and security for releases.

---

## Folder Structure

```
.
├── app/                 # Main app module
│   ├── src/             # Source code (Kotlin, XML, resources)
│   ├── schemas/         # Database schemas
│   ├── build.gradle.kts # App-specific build script
│   └── proguard-rules.pro
├── gradle/              # Gradle version catalogs and wrapper
├── .idea/               # IDE configs
├── build.gradle.kts     # Root build script
├── settings.gradle.kts  # Project settings
├── gradle.properties    # Gradle properties
├── README.md            # Project documentation
└── .gitignore           # Git ignore rules
```

---

## Contribution Guidelines

Contributions are welcome!  
- Fork the repository
- Create a feature branch
- Make your changes
- Submit a pull request
