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
include(":components:home")
include(":components:detail")
include(":components:addEdit")
include(":components:notificationList")
include(":business:plant")
include(":business:notification")
include(":library:design")
include(":library:test")
include(":library:navigation")
