package com.sunrisekcdeveloper.design.ui

import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class SnackbarEmitter {
    private val emitter = MutableSharedFlow<SnackbarEmitterType>(replay = 1)
    val snackbarEvent: SharedFlow<SnackbarEmitterType>
        get() = emitter

    fun emit(type: SnackbarEmitterType) {
        emitter.tryEmit(type)
    }
}