plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt") version "1.9.0"
    id("com.google.gms.google-services")
    id ("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.focuschildapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.focuschildapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
    implementation("androidx.compose.material3:material3")
    implementation("io.coil-kt:coil:2.4.0")
    implementation ("io.coil-kt:coil-gif:2.2.2")
    implementation ("com.google.accompanist:accompanist-drawablepainter:0.28.0")
    implementation ("androidx.navigation:navigation-compose:2.5.0")
    implementation ("com.google.accompanist:accompanist-navigation-animation:0.24.13-rc")
    implementation("io.coil-kt:coil:2.4.0")
    implementation ("io.coil-kt:coil-gif:2.2.2")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.firebase.auth.ktx)
    val room_version = "2.6.1"
    val compose_version = "1.8.0"
    implementation ("androidx.room:room-runtime:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.dagger:hilt-android:2.47")
    kapt ("com.google.dagger:hilt-android-compiler:2.47")

    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}