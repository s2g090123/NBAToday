package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.flow.MutableStateFlow

class TestDataStore : BaseDataStore {

    override val statsCookies = MutableStateFlow<Set<String>>(emptySet())
    override val recordScheduleToday = MutableStateFlow(NbaUtils.formatDate(1990, 1, 1))
    override val themeColors = MutableStateFlow(LakersColors)
    override val userData = MutableStateFlow<User?>(null)

    override suspend fun updateStatsCookies(cookies: Set<String>) {
        statsCookies.value = cookies
    }

    override suspend fun updateRecordScheduleToday(year: Int, month: Int, day: Int) {
        recordScheduleToday.value = NbaUtils.formatDate(year, month, day)
    }

    override suspend fun updateThemeColor(teamId: Int) {
        val team = NBATeam.getTeamById(teamId)
        val color = team?.colors ?: teamOfficial.colors
        themeColors.value = color
    }

    override suspend fun updateUser(user: User?) {
        userData.value = user
    }

    fun clear() {
        statsCookies.value = emptySet()
        recordScheduleToday.value = ""
        themeColors.value = LakersColors
        userData.value = null
    }
}
