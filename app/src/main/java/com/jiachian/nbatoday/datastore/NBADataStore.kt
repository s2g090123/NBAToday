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
import com.jiachian.nbatoday.datastore.NBADataStore.PreferencesKeys.LAST_ACCESSED_DATE
import com.jiachian.nbatoday.datastore.NBADataStore.PreferencesKeys.THEME_COLORS_TEAM_ID
import com.jiachian.nbatoday.datastore.NBADataStore.PreferencesKeys.USER
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.utils.DateUtils
import kotlinx.coroutines.flow.map

class NBADataStore(
    private val application: Application,
    dataStoreName: String = DataStoreName,
) : BaseDataStore {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = dataStoreName)

    object PreferencesKeys {
        val LAST_ACCESSED_DATE = stringPreferencesKey("last_accessed_date")
        val THEME_COLORS_TEAM_ID = intPreferencesKey("theme_colors_team_id")
        val USER = stringPreferencesKey("user")
    }

    private val dataStore by lazy {
        application.applicationContext.dataStore
    }

    // Retrieve the last accessed date from DataStore
    override val lastAccessedDate by lazy {
        dataStore.data.map { pref ->
            pref[LAST_ACCESSED_DATE] ?: DateUtils.formatDate(1990, 1, 1)
        }
    }

    // Retrieve theme colors based on the team ID from DataStore
    override val themeColors by lazy {
        dataStore.data.map { pref ->
            pref[THEME_COLORS_TEAM_ID]?.let { teamId ->
                NBATeam.getTeamById(teamId).colors
            } ?: LakersColors
        }
    }

    // Retrieve user information from DataStore
    override val user by lazy {
        dataStore.data.map { pref ->
            pref[USER].let { user ->
                Gson().fromJson<User?>(
                    user,
                    object : TypeToken<User?>() {}.type
                )
            }
        }
    }

    override suspend fun updateLastAccessedDate(year: Int, month: Int, day: Int) {
        dataStore.edit { pref ->
            pref[LAST_ACCESSED_DATE] = DateUtils.formatDate(year, month, day)
        }
    }

    override suspend fun updateThemeColorsTeamId(teamId: Int) {
        dataStore.edit { pref ->
            pref[THEME_COLORS_TEAM_ID] = teamId
        }
    }

    override suspend fun updateUser(user: User?) {
        dataStore.edit { pref ->
            pref[USER] = user?.let { user ->
                Gson().toJson(user)
            } ?: ""
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
