plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
    kotlin("plugin.serialization")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "host.capitalquiz.arondit"
    compileSdk = 33

    defaultConfig {
        applicationId = "host.capitalquiz.arondit"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.ktx)
    implementation(libs.appcompat)
    implementation(libs.materials)
    implementation(libs.constraintLayout)
    implementation(libs.legacy.support)
    implementation(libs.livecycle.livedata)
    implementation(libs.livecycle.viemodel)
    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)
    androidTestImplementation(libs.android.espresso)

    implementation(libs.fragment.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    implementation(libs.room)
    annotationProcessor(libs.room.compiler)

    // To use Kotlin annotation processing tool (kapt)
    kapt(libs.room.compiler)

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.ktx)

    implementation(libs.coroutines)

    implementation(libs.serialization)

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.jsoup)

    implementation(libs.pager.dots.indicator) // my library

    implementation(libs.lottie)

    implementation(libs.datastore)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}