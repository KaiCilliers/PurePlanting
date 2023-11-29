plugins {
    kotlin("jvm") version "1.9.20"
    id("java-library")
}

kotlin.sourceSets.all() {
    languageSettings.enableLanguageFeature("DataObjects")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlin.stdLib)
    implementation(libs.kotlin.coroutines.core)
}