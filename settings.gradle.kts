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
include(":domain:plant")
include(":domain:notification")
include(":feature:plantList")
include(":feature:plantDetail")
include(":feature:addEditPlant")
include(":feature:notificationList")
include(":shared:android")
include(":shared:domain")
include(":shared:test")
include(":data:plant")
include(":data:notification")
include(":feature:reminders")
