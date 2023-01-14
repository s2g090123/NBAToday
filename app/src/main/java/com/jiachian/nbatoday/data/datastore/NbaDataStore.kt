package com.jiachian.nbatoday.data.datastore

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.jiachian.nbatoday.DATA_STORE_NAME
import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.data.datastore.NbaDataStore.PreferencesKeys.RECORD_SCHEDULE_TODAY
import com.jiachian.nbatoday.data.datastore.NbaDataStore.PreferencesKeys.STATS_COOKIES
import com.jiachian.nbatoday.data.datastore.NbaDataStore.PreferencesKeys.THEME_COLORS
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

class NbaDataStore(
    private val application: Application
) {

    object PreferencesKeys {
        val STATS_COOKIES = stringSetPreferencesKey("stats_cookies")
        val RECORD_SCHEDULE_TODAY = stringPreferencesKey("record_schedule_today")
        val THEME_COLORS = intPreferencesKey("theme_colors")
    }

    private val dataStore by lazy {
        application.applicationContext.dataStore
    }

    val statsCookies by lazy {
        dataStore.data.map { pref ->
            pref[STATS_COOKIES] ?: setOf()
        }
    }

    val recordScheduleToday by lazy {
        dataStore.data.map { pref ->
            pref[RECORD_SCHEDULE_TODAY] ?: NbaUtils.formatDate(1990, 1, 1)
        }
    }

    val themeColors by lazy {
        dataStore.data.map { pref ->
            val themeColorTeamId = pref[THEME_COLORS]
            themeColorTeamId?.let {
                DefaultTeam.getColorsById(it)
            } ?: LakersColors

        }
    }

    suspend fun updateStatsCookies(cookies: Set<String>) {
        dataStore.edit { settings ->
            settings[STATS_COOKIES] = cookies
        }
    }

    suspend fun updateRecordScheduleToday(year: Int, month: Int, day: Int) {
        dataStore.edit { settings ->
            settings[RECORD_SCHEDULE_TODAY] = NbaUtils.formatDate(year, month, day)
        }
    }

    suspend fun updateThemeColor(teamId: Int) {
        dataStore.edit { settings ->
            settings[THEME_COLORS] = teamId
        }
    }
}