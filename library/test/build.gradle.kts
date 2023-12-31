plugins {
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.test.coroutines)
}