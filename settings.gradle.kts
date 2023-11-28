pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "PurePlanting"
include(":app")
include(":shared-test")
include(":domain:plant")
include(":domain:notification")
include(":feature:plantList")
include(":feature:plantDetail")
include(":feature:addEditPlant")
include(":feature:notificationList")
