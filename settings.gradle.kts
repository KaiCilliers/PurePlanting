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
include(":feature:plantList")
include(":feature:detail")
include(":feature:addEdit")
include(":feature:notificationList")
include(":component:plant")
include(":component:notification")
include(":library:design")
include(":library:test")
include(":library:navigation")
