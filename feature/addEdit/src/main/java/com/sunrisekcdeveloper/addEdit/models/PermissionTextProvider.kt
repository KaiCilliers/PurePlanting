package com.sunrisekcdeveloper.addEdit

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}