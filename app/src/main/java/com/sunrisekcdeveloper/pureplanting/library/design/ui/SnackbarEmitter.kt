package com.sunrisekcdeveloper.pureplanting.library.design.ui

import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource

class SnackbarEmitter {
    // only needed if going to observe from multiple sources
    private val emitter = EventEmitter<SnackbarEmitterType>()
    val snackbarEvents: EventSource<SnackbarEmitterType>
        get() = emitter

    fun emit(type: SnackbarEmitterType) {
        emitter.emit(type)
    }
}