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
    implementation("androidx.navigation:navigation-compose:2.9.6")

    // --- ViewModel Compose ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

    // --- Retrofit + Gson ---
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    // --- OkHttp + Logging ---
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")

    // --- DataStore (guardar JWT, usuario, etc.) ---
    implementation("androidx.datastore:datastore-preferences:1.2.0")

    // --- Room (cache offline) ---
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    // Necesitas KSP para el compiler (ver paso 3)
    ksp("androidx.room:room-compiler:2.8.4")

    // --- Coil (cargar imágenes URL en Compose) ---
    implementation("io.coil-kt:coil-compose:2.7.0")

    // --- WorkManager (reintentos / colas cuando vuelve internet) ---
    implementation("androidx.work:work-runtime-ktx:2.11.0")

    // --- Testing (los tuyos) ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}