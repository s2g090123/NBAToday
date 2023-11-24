package com.jiachian.nbatoday.data.datastore

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.DataStoreName
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.data.datastore.NbaDataStore.PreferencesKeys.RECORD_SCHEDULE_TODAY
import com.jiachian.nbatoday.data.datastore.NbaDataStore.PreferencesKeys.STATS_COOKIES
import com.jiachian.nbatoday.data.datastore.NbaDataStore.PreferencesKeys.THEME_COLORS
import com.jiachian.nbatoday.data.datastore.NbaDataStore.PreferencesKeys.USER_DATA
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreName)

class NbaDataStore(
    private val application: Application
) : BaseDataStore {

    object PreferencesKeys {
        val STATS_COOKIES = stringSetPreferencesKey("stats_cookies")
        val RECORD_SCHEDULE_TODAY = stringPreferencesKey("record_schedule_today")
        val THEME_COLORS = intPreferencesKey("theme_colors")
        val USER_DATA = stringPreferencesKey("user_data")
    }

    private val dataStore by lazy {
        application.applicationContext.dataStore
    }

    override val statsCookies by lazy {
        dataStore.data.map { pref ->
            pref[STATS_COOKIES] ?: setOf()
        }
    }

    override val recordScheduleToday by lazy {
        dataStore.data.map { pref ->
            pref[RECORD_SCHEDULE_TODAY] ?: NbaUtils.formatDate(1990, 1, 1)
        }
    }

    override val themeColors by lazy {
        dataStore.data.map { pref ->
            val themeColorTeamId = pref[THEME_COLORS]
            themeColorTeamId?.let {
                val team = NBATeam.getTeamById(it)
                team?.colors
            } ?: LakersColors
        }
    }

    override val userData by lazy {
        dataStore.data.map { pref ->
            val userData = pref[USER_DATA]
            val gson = Gson()
            val type = object : TypeToken<User>() {}.type
            gson.fromJson<User?>(userData, type)
        }
    }

    override suspend fun updateStatsCookies(cookies: Set<String>) {
        dataStore.edit { settings ->
            settings[STATS_COOKIES] = cookies
        }
    }

    override suspend fun updateRecordScheduleToday(year: Int, month: Int, day: Int) {
        dataStore.edit { settings ->
            settings[RECORD_SCHEDULE_TODAY] = NbaUtils.formatDate(year, month, day)
        }
    }

    override suspend fun updateThemeColor(teamId: Int) {
        dataStore.edit { settings ->
            settings[THEME_COLORS] = teamId
        }
    }

    override suspend fun updateUser(user: User?) {
        dataStore.edit { settings ->
            settings[USER_DATA] = if (user != null) {
                val gson = Gson()
                gson.toJson(user)
            } else {
                ""
            }
        }
    }

    @VisibleForTesting
    @ExcludeFromJacocoGeneratedReport
    suspend fun clear() {
        dataStore.edit { settings ->
            settings.clear()
        }
    }
}
