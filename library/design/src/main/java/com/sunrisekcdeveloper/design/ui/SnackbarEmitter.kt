package com.sunrisekcdeveloper.design.ui

import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

class SnackbarEmitter {
    private val emitter: EventEmitter<SnackbarEmitterType> = EventEmitter()
    val snackbarEvent: EventSource<SnackbarEmitterType> = emitter

    fun emit(type: SnackbarEmitterType) {
        emitter.emit(type)
    }
}