plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("androidx.room")         //signal
    id("com.google.devtools.ksp")     //ksp
    id("com.chaquo.python")         //chaquo
}
// For KSP
ksp {
    arg("option_name", "option_value")
    // other options...
}

android {
    namespace = "com.kk.mmmp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kk.mmmp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        ndk {
            // 增加 armeabi-v7a 和 x86 以支持更多设备和模拟器
            abiFilters += listOf("arm64-v8a", "x86_64", "armeabi-v7a", "x86")
        }   //标记3

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }       //signal
}
//chaquopy
chaquopy {
    defaultConfig {
        version = "3.8"
        buildPython("G:\\Anaconda\\envs\\test\\python.exe")
        pip{
            install("numpy")
            install("pandas")
            install("matplotlib")
            install("openpyxl")
            install("scikit-learn")
            install("seaborn==0.12.2")
        }
    }
    sourceSets {
        getByName("main") {
            srcDir("some/other/dir")
        }
    }
}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    // implementation(libs.androidx.material3.android) // Removed to resolve version conflict with BOM and compileSdk
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //signal:room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

    //livedata
    implementation(libs.androidx.runtime.livedata)

    //coil-compose
    implementation("io.coil-kt:coil-compose:2.6.0")
}