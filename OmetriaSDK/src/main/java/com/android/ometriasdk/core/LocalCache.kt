package com.android.ometriasdk.core

import android.content.Context
import android.content.SharedPreferences
import com.android.ometriasdk.core.event.OmetriaEvent
import com.android.ometriasdk.core.network.toJson
import com.android.ometriasdk.core.network.toOmetriaEventList

/**
 * Created by cristiandregan
 * on 24/07/2020.
 */

private const val LOCAL_CACHE_PREFERENCES = "LOCAL_CACHE_PREFERENCES"
private const val IS_FIRST_APP_RUN_KEY = "IS_FIRST_APP_RUN_KEY"
private const val INSTALLATION_ID_KEY = "INSTALLATION_ID_KEY"
private const val EVENTS_KEY = "EVENTS_KEY"
private const val PUSH_TOKEN_KEY = "PUSH_TOKEN_KEY"
private const val JSON_ARRAY = "[]"

internal class LocalCache(private val context: Context) {

    fun saveIsFirstAppRun(isFirstAppRun: Boolean) {
        getLocalCachePreferences().edit().putBoolean(IS_FIRST_APP_RUN_KEY, isFirstAppRun).apply()
    }

    fun isFirstAppRun(): Boolean {
        return getLocalCachePreferences().getBoolean(IS_FIRST_APP_RUN_KEY, true)
    }

    fun saveInstallationId(installationId: String?) {
        getLocalCachePreferences().edit().putString(INSTALLATION_ID_KEY, installationId).apply()
    }

    fun getInstallationId(): String? {
        return getLocalCachePreferences().getString(INSTALLATION_ID_KEY, null)
    }

    fun saveEvent(ometriaEvent: OmetriaEvent) {
        val eventsString =
            getLocalCachePreferences().getString(EVENTS_KEY, JSON_ARRAY) ?: JSON_ARRAY

        val eventsList = eventsString.toOmetriaEventList()

        eventsList.add(ometriaEvent)

        getLocalCachePreferences().edit().putString(EVENTS_KEY, eventsList.toJson().toString())
            .apply()
    }

    fun getEvents(): List<OmetriaEvent> {
        val eventsString = getLocalCachePreferences().getString(EVENTS_KEY, null) ?: JSON_ARRAY

        return eventsString.toOmetriaEventList()
    }

    fun updateEvents(events: List<OmetriaEvent>?, isBeingFlushed: Boolean) {
        events ?: return

        val cachedEvents = getEvents()

        events.forEach { event ->
            cachedEvents.first { it.eventId == event.eventId }.isBeingFlushed = isBeingFlushed
        }

        getLocalCachePreferences().edit()
            .putString(EVENTS_KEY, cachedEvents.toJson().toString())
            .apply()
    }

    fun removeEvents(eventsToRemove: List<OmetriaEvent>) {
        val eventsList = getEvents().toMutableList()

        eventsToRemove.forEach { event ->
            eventsList.remove(eventsList.first { it.eventId == event.eventId })
        }

        getLocalCachePreferences().edit().putString(EVENTS_KEY, eventsList.toJson().toString())
            .apply()
    }

    private fun getLocalCachePreferences(): SharedPreferences {
        return context.getSharedPreferences(LOCAL_CACHE_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun savePushToken(pushToken: String) {
        getLocalCachePreferences().edit().putString(PUSH_TOKEN_KEY, pushToken).apply()
    }

    fun getPushToken(): String? {
        return getLocalCachePreferences().getString(PUSH_TOKEN_KEY, null)
    }

    fun clearEvents() {
        getLocalCachePreferences().edit().remove(EVENTS_KEY).apply()
    }
}