package com.sunrisekcdeveloper.addEditPlant

import com.sunrisekcdeveloper.addEditPlant.PermissionTextProvider

class CameraPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "It seems you permanently declined camera permission. " +
                    "You can go to the app settings to grant it."
        } else {
            "This app needs access to your camera so that you can " +
                    "take a picture of your plant"
        }
    }
}
