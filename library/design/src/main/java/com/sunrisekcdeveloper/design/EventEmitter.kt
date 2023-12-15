package com.sunrisekcdeveloper.design

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EventEmitter {

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    private val eventsChannel = Channel<Event>()
    val events: ReceiveChannel<Event> = eventsChannel

    fun emit(event: Event) {
        scope.launch {
            delay(4000)
            eventsChannel.send(event) }
    }

    sealed class Event(
        open val undoAction: (() -> Unit)? = null,
    ) {
        data class Text(val text: String, override val undoAction: (() -> Unit)? = null) : Event(undoAction)
        data class TextRes(val textRes: Int, override val undoAction: (() -> Unit)? = null) : Event(undoAction)
    }
}