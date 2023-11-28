package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.flow.MutableStateFlow

class TestDataStore : BaseDataStore {

    override val statsCookies = MutableStateFlow<Set<String>>(emptySet())
    override val recordScheduleToday = MutableStateFlow("")
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
