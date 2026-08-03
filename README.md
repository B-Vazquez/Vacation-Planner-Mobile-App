# Vacation Planner Mobile Application

A secure and intuitive Android application designed to help users plan, organize, and share their travel itineraries.

## 🌟 Features
- **Trip Management**: Create, view, update, and delete vacation plans.
- **Excursion Tracking**: Associate specific activities (excursions) with each vacation.
- **Real-time Alerts**: Set notifications for trip start and end dates.
- **Sharing**: Easily share trip details via messaging or email.
- **Secure Access**: Integrated user authentication (Sign Up/Log In).
- **Offline Support**: Local database persistence using Room.

## 🛠 Technology Stack
- **Language**: Java
- **Database**: Room Persistence Library (SQLite)
- **Architecture**: MVVM (Model-View-ViewModel) / Repository Pattern
- **UI**: XML Layouts with Material Design components
- **Background Tasks**: AlarmManager for notifications

## 🏗 Project Structure
- `com.example.vacationplanner.UI`: Activity classes and Adapters.
- `com.example.vacationplanner.database`: Room Database and Repository implementation.
- `com.example.vacationplanner.entities`: Data models (Vacation, Excursion, User).
- `com.example.vacationplanner.utilities`: Helper classes for validation, security, and logic.
- `com.example.vacationplanner.viewmodel`: UI-related data handling.

## 🚀 Getting Started
1. Clone the repository.
2. Open the project in Android Studio (Ladybug or later recommended).
3. Build and run on an emulator or physical device (API 26+).
4. Download the latest APK from the [official release page](https://b-vazquez.github.io/Vazquez-D424/).

---
*For detailed operation, please refer to the 'Vacation Planner User Guide' included in the documentation folder.*
