# MMMP - Mathematical Modeling Method Package (MMMP 数模方法库) 📊📱

**MMMP** is a powerful Android application designed to perform complex mathematical modeling and data analysis directly on your mobile device. Built with modern Android technologies and integrated with a robust Python scientific stack, MMMP brings the power of desktop data analysis to your pocket.

**MMMP** 是一款功能强大的 Android 应用程序，专为在移动设备上直接执行复杂的数学建模和数据分析而设计。基于现代 Android 技术构建，并集成了强大的 Python 科学计算栈，MMMP 将桌面级数据分析的能力带入您的口袋。

## ✨ Key Features / 主要功能

*   **🧮 Local Python Algorithms**: Run sophisticated mathematical models (AHP, EWM, TOPSIS, Logistic Regression, Grey Prediction) offline using **Chaquopy**.
    *   **本地 Python 算法**：使用 **Chaquopy** 离线运行复杂的数学模型（AHP、EWM、TOPSIS、逻辑回归、灰色预测）。
*   **🎨 Modern UI/UX**: Built entirely with **Jetpack Compose** and Material 3 design system for a fluid, beautiful, and responsive user experience.
    *   **现代 UI/UX**：完全使用 **Jetpack Compose** 和 Material 3 设计系统构建，提供流畅、美观且响应迅速的用户体验。
*   **📈 Data Visualization**: Generate professional charts and graphs (via Matplotlib/Seaborn) and save them directly to your gallery.
    *   **数据可视化**：生成专业图表（通过 Matplotlib/Seaborn）并直接保存到您的相册。
*   **📂 Excel Integration**: Seamlessly import `.xlsx` / `.xls` data files for analysis.
    *   **Excel 集成**：无缝导入 `.xlsx` / `.xls` 数据文件进行分析。
*   **🛡️ Secure & Offline**: Uses **Room Database** for local user management and data persistence. No internet connection required for calculations.
    *   **安全与离线**：使用 **Room Database** 进行本地用户管理和数据持久化。计算无需互联网连接。

## 🛠️ Tech Stack / 技术栈

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

## 📱 Supported Algorithms / 支持的算法

1.  **AHP (Analytic Hierarchy Process)**: For multi-criteria decision making.
    *   **AHP（层次分析法）**：用于多准则决策。
2.  **EWM (Entropy Weight Method)**: Objective weighting method for index evaluation.
    *   **EWM（熵权法）**：用于指标评估的客观赋权方法。
3.  **TOPSIS**: Technique for Order of Preference by Similarity to Ideal Solution.
    *   **TOPSIS（优劣解距离法）**：通过与理想解的相似度进行排序的技术。
4.  **Logistic Regression**: Statistical model for binary classification.
    *   **逻辑回归**：用于二分类的统计模型。
5.  **Grey Prediction (GM 1,1)**: For forecasting with limited data samples.
    *   **灰色预测 (GM 1,1)**：用于小样本数据的预测。

## 🚀 Getting Started / 快速开始

### Prerequisites / 前置要求
*   Android Studio Hedgehog or newer. (Android Studio Hedgehog 或更新版本)
*   JDK 17.
*   Android SDK API 34+ (target).

### Installation / 安装
1.  **Clone the repository / 克隆仓库**:
    ```bash
    git clone https://github.com/vivacious1024/MMMP.git
    ```
2.  **Open in Android Studio / 在 Android Studio 中打开**:
    *   File -> Open -> Select the cloned `MMMP` folder. (文件 -> 打开 -> 选择克隆的 `MMMP` 文件夹)
3.  **Sync Gradle / 同步 Gradle**:
    *   Wait for the project to download dependencies and configure the Python environment (this may take a few minutes for Chaquopy setup).
    *   等待项目下载依赖并配置 Python 环境（Chaquopy 设置可能需要几分钟）。
4.  **Run / 运行**:
    *   Connect your Android device or use an Emulator. (连接您的 Android 设备或使用模拟器)
    *   Click the **Run** button (▶️). (点击 **运行** 按钮 ▶️)

## 📂 Project Structure / 项目结构

```
app/src/main
├── java/com/kk/mmmp
│   ├── runcode/       # Python interface & ViewModel (Python 接口 & ViewModel)
│   ├── screens/       # Jetpack Compose UI Screens (Jetpack Compose UI 屏幕)
│   ├── database/      # Room Database entities & DAOs (Room 数据库实体 & DAO)
│   └── MainActivity.kt
├── python/            # Python scripts (TOPSIS.py, Logistic.py, etc.) (Python 脚本)
└── res/               # Resources & Assets (资源 & 素材)
```

## 🎨 Design / 设计
The application icon and key visual assets were rendered using **Cinema 4D**, providing a unique and premium aesthetic that stands out from standard utility apps.

应用图标和关键视觉素材使用 **Cinema 4D** 渲染，提供了独特且高级的美感，使其在标准实用工具应用中脱颖而出。

## 📄 License / 许可证
This project is licensed under the Apache 2.0 License - see the LICENSE file for details.

本项目采用 Apache 2.0 许可证 - 详见 LICENSE 文件。

---
*Developed by [Your Name/Team]*
