package com.jiachian.nbatoday.common.data.datastore

import com.jiachian.nbatoday.common.ui.theme.NBAColors
import com.jiachian.nbatoday.home.user.data.model.local.User
import kotlinx.coroutines.flow.Flow

interface BaseDataStore {
    val lastAccessedDate: Flow<String>
    val themeColors: Flow<NBAColors>
    val user: Flow<User?>

    suspend fun updateLastAccessedDate(year: Int, month: Int, day: Int)
    suspend fun updateThemeColorsTeamId(teamId: Int)
    suspend fun updateUser(user: User?)
}
