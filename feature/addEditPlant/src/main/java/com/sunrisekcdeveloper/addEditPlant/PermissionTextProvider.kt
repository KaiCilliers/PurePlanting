package com.sunrisekcdeveloper.addEditPlant

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}