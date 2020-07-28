package com.android.ometriasdk.core.event

import android.content.Context
import com.android.ometriasdk.core.LocalCache
import com.android.ometriasdk.core.Logger
import com.android.ometriasdk.core.event.OmetriaEventType.LAUNCH_APPLICATION

/**
 * Created by cristiandregan
 * on 27/07/2020.
 */

private val TAG = EventHandler::class.simpleName

internal class EventHandler(private val context: Context, private val localCache: LocalCache) {

    fun processEvent(event: Event) {
        when (event.type) {
            LAUNCH_APPLICATION -> sendEvent(event.toGeneric(context, localCache))
            else -> sendEvent(event)
        }
    }

    private fun <T> sendEvent(event: T) where T : Event {
        context.openFileOutput("Events.txt", Context.MODE_APPEND).use {
            it.write("$event\n".toByteArray())
        }

        Logger.d(TAG, "Track event: ", event)
    }
}