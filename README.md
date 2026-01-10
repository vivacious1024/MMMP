# MMMP - Mathematical Modeling Method Package 📊📱

**MMMP** is a powerful Android application designed to perform complex mathematical modeling and data analysis directly on your mobile device. Built with modern Android technologies and integrated with a robust Python scientific stack, MMMP brings the power of desktop data analysis to your pocket.

## ✨ Key Features

*   **🧮 Local Python Algorithms**: Run sophisticated mathematical models (AHP, EWM, TOPSIS, Logistic Regression, Grey Prediction) offline using **Chaquopy**.
*   **🎨 Modern UI/UX**: Built entirely with **Jetpack Compose** and Material 3 design system for a fluid, beautiful, and responsive user experience.
*   **📈 Data Visualization**: Generate professional charts and graphs (via Matplotlib/Seaborn) and save them directly to your gallery.
*   **📂 Excel Integration**: Seamlessly import `.xlsx` / `.xls` data files for analysis.
*   **🛡️ Secure & Offline**: Uses **Room Database** for local user management and data persistence. No internet connection required for calculations.

## 🛠️ Tech Stack

### Android (Kotlin)
*   **Architecture**: MVVM (Model-View-ViewModel)
*   **UI Framework**: Jetpack Compose (Material 3)
*   **Database**: Room (SQLite)
*   **Asynchronous**: Coroutines & Flow
*   **Dependency Injection**: Koin / Manual (Context specific)

### Python (Data Science)
Integrated via **Chaquopy** plugin:
*   **NumPy** & **Pandas**: Data manipulation and numerical analysis.
*   **Scikit-learn**: Machine learning algorithms.
*   **Seaborn** & **Matplotlib**: Statistical data visualization.

## 📱 Supported Algorithms

1.  **AHP (Analytic Hierarchy Process)**: For multi-criteria decision making.
2.  **EWM (Entropy Weight Method)**: Objective weighting method for index evaluation.
3.  **TOPSIS**: Technique for Order of Preference by Similarity to Ideal Solution.
4.  **Logistic Regression**: Statistical model for binary classification.
5.  **Grey Prediction (GM 1,1)**: For forecasting with limited data samples.

## 🚀 Getting Started

### Prerequisites
*   Android Studio Hedgehog or newer.
*   JDK 17.
*   Android SDK API 34+ (target).

### Installation
1.  **Clone the repository**:
    ```bash
    git clone https://github.com/vivacious1024/MMMP.git
    ```
2.  **Open in Android Studio**:
    *   File -> Open -> Select the cloned `MMMP` folder.
3.  **Sync Gradle**:
    *   Wait for the project to download dependencies and configure the Python environment (this may take a few minutes for Chaquopy setup).
4.  **Run**:
    *   Connect your Android device or use an Emulator.
    *   Click the **Run** button (▶️).

## 📂 Project Structure

```
app/src/main
├── java/com/kk/mmmp
│   ├── runcode/       # Python interface & ViewModel
│   ├── screens/       # Jetpack Compose UI Screens
│   ├── database/      # Room Database entities & DAOs
│   └── MainActivity.kt
├── python/            # Python scripts (TOPSIS.py, Logistic.py, etc.)
└── res/               # Resources & Assets
```

## 🎨 Design
The application icon and key visual assets were rendered using **Cinema 4D**, providing a unique and premium aesthetic that stands out from standard utility apps.

## 📄 License
This project is licensed under the Apache 2.0 License - see the LICENSE file for details.

---
*Developed by [Your Name/Team]*
