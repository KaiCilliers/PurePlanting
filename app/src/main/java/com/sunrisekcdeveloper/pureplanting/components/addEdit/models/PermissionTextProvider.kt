package com.sunrisekcdeveloper.pureplanting.components.addEdit.models

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}