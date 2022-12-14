package com.jiachian.nbatoday.data.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jiachian.nbatoday.DATA_STORE_NAME
import com.jiachian.nbatoday.data.datastore.NbaDataStore.PreferencesKeys.SCHEDULE_FROM
import com.jiachian.nbatoday.data.datastore.NbaDataStore.PreferencesKeys.SCHEDULE_TO
import com.jiachian.nbatoday.data.datastore.NbaDataStore.PreferencesKeys.STATS_COOKIES
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class NbaDataStore(
    private val application: Application
) {

    object PreferencesKeys {
        val SCHEDULE_FROM = stringPreferencesKey("schedule_from")
        val SCHEDULE_TO = stringPreferencesKey("schedule_to")
        val STATS_COOKIES = stringSetPreferencesKey("stats_cookies")
    }

    private val dataStore by lazy {
        application.applicationContext.dataStore
    }

    val scheduleFrom by lazy {
        dataStore.data.map { pref ->
            pref[SCHEDULE_FROM] ?: ""
        }
    }
    val scheduleTo by lazy {
        dataStore.data.map { pref ->
            pref[SCHEDULE_TO] ?: ""
        }
    }

    val statsCookies by lazy {
        dataStore.data.map { pref ->
            pref[STATS_COOKIES] ?: setOf()
        }
    }

    suspend fun updateScheduleDate(from: String, to: String) {
        dataStore.edit { settings ->
            settings[SCHEDULE_FROM] = from
            settings[SCHEDULE_TO] = to
        }
    }

    suspend fun updateStatsCookies(cookies: Set<String>) {
        dataStore.edit { settings ->
            settings[STATS_COOKIES] = cookies
        }
    }
}