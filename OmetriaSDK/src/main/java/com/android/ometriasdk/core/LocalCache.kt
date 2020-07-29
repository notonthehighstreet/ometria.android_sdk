package com.android.ometriasdk.core

import android.content.Context
import android.content.SharedPreferences
import com.android.ometriasdk.core.event.Event

/**
 * Created by cristiandregan
 * on 24/07/2020.
 */

private const val LOCAL_CACHE_PREFERENCES = "LOCAL_CACHE_PREFERENCES"
private const val IS_FIRST_APP_RUN_KEY = "IS_FIRST_APP_RUN_KEY"
private const val INSTALLMENT_ID_KEY = "INSTALLMENT_ID_KEY"
private const val EVENTS_KEY = "EVENTS_KEY"

internal class LocalCache(private val context: Context) {

    fun isFirstAppRun(isFirstAppRun: Boolean) {
        getLocalCachePreferences().edit().putBoolean(IS_FIRST_APP_RUN_KEY, isFirstAppRun).apply()
    }

    fun isFirstAppRun(): Boolean {
        return getLocalCachePreferences().getBoolean(IS_FIRST_APP_RUN_KEY, true)
    }

    fun saveInstallmentID(installmentID: String) {
        getLocalCachePreferences().edit().putString(INSTALLMENT_ID_KEY, installmentID).apply()
    }

    fun getInstallmentID(): String? {
        return getLocalCachePreferences().getString(INSTALLMENT_ID_KEY, null)
    }

    fun <T> saveEvent(event: T) where T : Event {
        val eventsHashSet: HashSet<String> = getLocalCachePreferences()
            .getStringSet(EVENTS_KEY, null) as HashSet<String>? ?: HashSet()

        eventsHashSet.add(event.toJson().toString())

        getLocalCachePreferences().edit().putStringSet(EVENTS_KEY, eventsHashSet).apply()
    }

    fun getEvents(): Set<String>? {
        return getLocalCachePreferences().getStringSet(EVENTS_KEY, null)
    }

    private fun getLocalCachePreferences(): SharedPreferences {
        return context.getSharedPreferences(LOCAL_CACHE_PREFERENCES, Context.MODE_PRIVATE)
    }
}