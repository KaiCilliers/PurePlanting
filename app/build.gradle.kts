plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("de.mannodermaus.android-junit5")
    id("kotlin-parcelize")
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

        create("pureLogic") {
            initWith(getByName("debug"))
            manifestPlaceholders["appLabel"] = "PureLogic"
            applicationIdSuffix = ".logic"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.9" // data objects
    }
    buildFeatures {
        compose = true
        buildConfig = true
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

kotlin.sourceSets.all() {
    languageSettings.enableLanguageFeature("DataObjects")
}

dependencies {
    testImplementation(project(":shared-test"))
    androidTestImplementation(project(":shared-test"))

    implementation(libs.bundles.simplestack)

    androidTestImplementation(libs.androidx.work.testing)
    testImplementation(libs.test.assertk)
    testImplementation(libs.bundles.junit5)
    testImplementation(libs.test.coroutines)
    testImplementation(libs.test.turbine)
    testRuntimeOnly(libs.junit.jupiter.engine)
    implementation(libs.androidx.work.runtimeKtx)

    implementation("io.coil-kt:coil-compose:2.2.2")

// TODO: clear up dependencies
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")

    // version mapping https://developer.android.com/jetpack/compose/bom/bom-mapping
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("com.github.Zhuinden:flow-combinetuple-kt:1.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}