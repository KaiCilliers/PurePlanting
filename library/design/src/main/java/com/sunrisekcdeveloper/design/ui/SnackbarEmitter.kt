package com.sunrisekcdeveloper.design.ui

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SnackbarEmitter {
    private val emitter = MutableSharedFlow<SnackbarEmitterType>(replay = 1)
    val eventFlow: SharedFlow<SnackbarEmitterType>
        get() = emitter

    fun emit(type: SnackbarEmitterType) {
        emitter.tryEmit(type)
    }
}