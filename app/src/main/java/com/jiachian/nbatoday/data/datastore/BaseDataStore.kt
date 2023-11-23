package com.jiachian.nbatoday.data.datastore

import com.jiachian.nbatoday.compose.theme.NbaColors
import com.jiachian.nbatoday.data.remote.user.User
import kotlinx.coroutines.flow.Flow

interface BaseDataStore {
    val statsCookies: Flow<Set<String>>
    val recordScheduleToday: Flow<String>
    val themeColors: Flow<NbaColors>
    val userData: Flow<User?>

    suspend fun updateStatsCookies(cookies: Set<String>)
    suspend fun updateRecordScheduleToday(year: Int, month: Int, day: Int)
    suspend fun updateThemeColor(teamId: Int)
    suspend fun updateUser(user: User?)
}
