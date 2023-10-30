package com.sunrisekcdeveloper.pureplanting.util

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}