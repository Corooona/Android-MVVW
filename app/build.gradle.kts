plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.mvvm"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.mvvm"
        minSdk = 26
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
    buildFeatures {
        compose = true   // habilita el compilador de Compose en este módulo
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // ViewModel + LiveData (incluye viewModelScope)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    // Corrutinas — para llamadas asíncronas sin bloquear el hilo principal
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Retrofit — cliente HTTP para consumir APIs REST
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Converter de Gson — convierte el JSON de la respuesta a data classes de Kotlin automáticamente
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Coil — librería para cargar imágenes desde URL en Compose
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Jetpack Compose
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.ui:ui:1.7.6")
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    // Permite usar observeAsState() con LiveData dentro de Composables
    implementation("androidx.compose.runtime:runtime-livedata:1.7.6")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.6")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}