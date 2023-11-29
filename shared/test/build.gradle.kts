plugins {
    id("org.jetbrains.kotlin.jvm")
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // TODO: remove app dependency and replace with a common-lib module depdendency
    //  a module that all other module will depend on through which they will have access to
    //  this module automatically by depending on the shared common lib
    implementation(libs.test.coroutines)
    implementation(project(":domain:plant"))
}