package com.jiachian.nbatoday.datastore

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.DataStoreName
import com.jiachian.nbatoday.annotation.ExcludeFromJacocoGeneratedReport
import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.datastore.NBADataStore.PreferencesKeys.LAST_ACCESSED_DAY
import com.jiachian.nbatoday.datastore.NBADataStore.PreferencesKeys.THEME_COLORS_TEAM_ID
import com.jiachian.nbatoday.datastore.NBADataStore.PreferencesKeys.USER
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DataStoreName)

class NBADataStore(private val application: Application) : BaseDataStore {

    object PreferencesKeys {
        val LAST_ACCESSED_DAY = stringPreferencesKey("last_accessed_day")
        val THEME_COLORS_TEAM_ID = intPreferencesKey("theme_colors_team_id")
        val USER = stringPreferencesKey("user")
    }

    private val dataStore by lazy {
        application.applicationContext.dataStore
    }

    override val lastAccessedDay by lazy {
        dataStore.data.map { pref ->
            pref[LAST_ACCESSED_DAY] ?: NbaUtils.formatDate(1990, 1, 1)
        }
    }

    override val themeColors by lazy {
        dataStore.data.map { pref ->
            val themeColorsTeamId = pref[THEME_COLORS_TEAM_ID]
            themeColorsTeamId?.let { teamId ->
                val team = NBATeam.getTeamById(teamId)
                team.colors
            } ?: LakersColors
        }
    }

    override val user by lazy {
        dataStore.data.map { pref ->
            val user = pref[USER]
            val gson = Gson()
            val type = object : TypeToken<User?>() {}.type
            gson.fromJson<User?>(user, type)
        }
    }

    override suspend fun updateLastAccessedDay(year: Int, month: Int, day: Int) {
        dataStore.edit { pref ->
            pref[LAST_ACCESSED_DAY] = NbaUtils.formatDate(year, month, day)
        }
    }

    override suspend fun updateThemeColorsTeamId(teamId: Int) {
        dataStore.edit { pref ->
            pref[THEME_COLORS_TEAM_ID] = teamId
        }
    }

    override suspend fun updateUser(user: User?) {
        dataStore.edit { pref ->
            pref[USER] = if (user != null) {
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
        dataStore.edit { pref ->
            pref.clear()
        }
    }
}
