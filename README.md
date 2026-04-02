# Smart Notes POS Lite

**Smart Notes POS Lite** is a lightweight and efficient Point of Sale (POS) application developed for Android. It is designed to help small business owners or individuals manage their products and prices with ease.

## 🚀 Purpose
The primary goal of this project is to provide a simple, offline-first solution for tracking product information. It demonstrates modern Android development practices, including a reactive UI and persistent local storage.

## ✨ Features
- **Product Management:** Add new products with names and prices.
- **Real-time List:** Instantly view all saved products in a clean, scrollable list.
- **Data Persistence:** Uses Android DataStore to ensure your data is saved safely on the device.
- **Modern UI/UX:** Built entirely with Jetpack Compose for a responsive and material-style interface.
- **Fast & Lightweight:** Minimal overhead, ensuring smooth performance even on older devices.

## 🛠️ Tech Stack
- **Language:** [Kotlin](https://kotlinlang.org/)
- **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Declarative UI)
- **Asynchronous Programming:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
- **Local Storage:** [Jetpack DataStore](https://developer.android.com/topic/libraries/architecture/datastore) (Preferences)
- **JSON Serialization:** [Moshi](https://github.com/square/moshi) (with KSP)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Build System:** Gradle (Kotlin DSL)

## 📂 Project Structure
- `data/local`: Handles data persistence logic using `PosStorageManager`.
- `data/models`: Defines the `Product` data class.
- `data/repository`: Implements the repository pattern to abstract data sources.
- `ui/viewmodel`: Contains `PosViewModel` to manage UI state and business logic.
- `ui/theme`: App-wide styling, including colors, typography, and themes.
- `MainActivity.kt`: The main entry point of the app, hosting the Compose UI.

## ⚙️ How to Run
1. Clone the repository.
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Ensure you have **JDK 17** configured.
4. Sync the Gradle files.
5. Run the app on an emulator or physical device (Minimum SDK: 24).

## 📄 License
This project is for educational and practice purposes.
