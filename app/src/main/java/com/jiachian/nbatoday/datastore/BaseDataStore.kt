package com.jiachian.nbatoday.datastore

import com.jiachian.nbatoday.compose.theme.NBAColors
import com.jiachian.nbatoday.models.local.user.User
import kotlinx.coroutines.flow.Flow

interface BaseDataStore {
    val lastAccessedDate: Flow<String>
    val themeColors: Flow<NBAColors>
    val user: Flow<User?>

    suspend fun updateLastAccessedDate(year: Int, month: Int, day: Int)
    suspend fun updateThemeColorsTeamId(teamId: Int)
    suspend fun updateUser(user: User?)
}
