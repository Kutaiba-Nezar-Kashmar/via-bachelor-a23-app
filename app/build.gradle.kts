
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

    id("com.apollographql.apollo3").version("3.7.3")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.dagger.hilt.android")
}

apollo {
    service("service") {
        packageName.set("io.github.viabachelora23michaelkutaibakasper.bprapp")
    }
}
kotlin {
    jvmToolchain(17)
}
android {
    namespace = "io.github.viabachelora23michaelkutaibakasper.bprapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.viabachelora23michaelkutaibakasper.bprapp"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        kotlinOptions {
            jvmTarget = "17"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Android
    implementation(`androidx-core-ktx`())
    implementation(`androidx-lifecycle-runtime-ktx`())
    implementation(`androidx-activity-compose`())

    // Compose
    implementation(platform(`androidx-compose-bom`()))
    implementation(`androidx-compose-ui`())
    implementation(`androidx-compose-ui-graphics`())
    implementation(`androidx-compose-ui-tooling-preview`())
    implementation(`androidx-compose-material3`())
    // TODO: Move rest of dependencies into Versions.kt and Dependencies.kt
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Google maps
    implementation(`play-services-maps`())
    implementation(`play-services-location`())
    // Google maps for compose
    implementation(`maps-compose`())

    // KTX for the Maps SDK for Android
    implementation(`maps-ktx`())
    // KTX for the Maps SDK for Android Utility Library
    implementation(`maps-utils-ktx`())


    // Serialization

    //apollo
    implementation("com.apollographql.apollo3:apollo-runtime:3.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")

    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-android-compiler:2.42")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
}