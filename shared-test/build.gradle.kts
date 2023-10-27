@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.sunrisekcdeveloper.shared_test"
    compileSdk = 33

    defaultConfig {
        minSdk = 28
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    // TODO: remove app dependency and replace with a common-lib module depdendency
    //  a module that all other module will depend on through which they will have access to
    //  this module automatically by depending on the shared common lib
    implementation(project(":app"))
    implementation(libs.test.coroutines)
}