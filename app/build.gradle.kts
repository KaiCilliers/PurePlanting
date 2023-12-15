plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("de.mannodermaus.android-junit5")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.sunrisekcdeveloper.pureplanting"
    compileSdk = 34

    defaultConfig {
        manifestPlaceholders["appLabel"] = "PurePlanting"
        applicationId = "com.sunrisekcdeveloper.pureplanting"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

//        create("pureLogic") {
//            initWith(getByName("debug"))
//            manifestPlaceholders["appLabel"] = "PureLogic"
//            applicationIdSuffix = ".logic"
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        languageVersion = "1.9" // data objects
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        // Kotlin version to compose compiler map --> https://developer.android.com/jetpack/androidx/releases/compose-kotlin
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin.sourceSets.all() {
    languageSettings.enableLanguageFeature("DataObjects")
}

dependencies {
    implementation(project(":library:design"))
    implementation(project(":library:navigation"))
    implementation(project(":feature:plantList"))
    implementation(project(":feature:addEdit"))
    implementation(project(":feature:detail"))
    implementation(project(":feature:notificationList"))
    implementation(project(":business:plant"))
    implementation(project(":business:notification"))

    testImplementation(project(":library:test"))
    androidTestImplementation(project(":library:test"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.lifecycle.runtimeKtx)
    implementation(libs.androidx.activity.compose)

    // version mapping https://developer.android.com/jetpack/compose/bom/bom-mapping
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.livedata)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons)
    implementation(libs.material)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.bundles.simplestack)
    implementation(libs.androidx.work.runtimeKtx)
    implementation(libs.util.coil)
    implementation(libs.util.flow.combineTuple)
    implementation(libs.bundles.eventEmitter)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.junit4.ext)
    androidTestImplementation(libs.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.work.testing)

    testImplementation(libs.test.assertk)
    testImplementation(libs.bundles.junit5)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)
    testImplementation(libs.junit4)
    testRuntimeOnly(libs.junit.jupiter.engine)
}