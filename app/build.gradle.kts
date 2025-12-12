plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}

android {
    namespace = "com.example.movilecibershield"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.movilecibershield"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // --- Compose base (ya los tienes, ok) ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // --- Navigation Compose ---
    implementation(libs.androidx.navigation.compose)

    // --- ViewModel Compose ---
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // --- Retrofit + Gson ---
    implementation(libs.retrofit)
    //noinspection UseTomlInstead
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // --- OkHttp + Logging ---
    implementation(libs.okhttp)
    //noinspection UseTomlInstead
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

    // --- DataStore (guardar JWT, usuario, etc.) ---
    implementation(libs.androidx.datastore.preferences)

    // --- Room (cache offline) ---
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.firebase.crashlytics.buildtools)
    // Necesitas KSP para el compiler (ver paso 3)
    ksp(libs.androidx.room.compiler)

    // --- Coil (cargar imágenes URL en Compose) ---
    //noinspection UseTomlInstead
    implementation("io.coil-kt:coil-compose:2.7.0")

    // --- WorkManager (reintentos / colas cuando vuelve internet) ---
    implementation(libs.androidx.work.runtime.ktx)

    // --- Testing (los tuyos) ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}