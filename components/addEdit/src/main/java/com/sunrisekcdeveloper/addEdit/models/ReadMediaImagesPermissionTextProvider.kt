package com.sunrisekcdeveloper.addEdit

class ReadMediaImagesPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined read media image permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your Gallery so that you can select an image " +
                    "for your plant."
        }
    }
}