package com.sunrisekcdeveloper.pureplanting.features.addEdit.models

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}