package com.sunrisekcdeveloper.design.ui

sealed class SnackbarEmitterType {
    data class Text(val text: String) : SnackbarEmitterType()
    data class TextRes(val resId: Int) : SnackbarEmitterType()
    data class Undo(val text: String, val action: () -> Unit) : SnackbarEmitterType()
}

