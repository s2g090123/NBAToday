package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.data.teamOfficial
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow

class TestDataStore : BaseDataStore {

    override val statsCookies = MutableStateFlow<Set<String>>(emptySet())
    override val lastAccessedDay = MutableStateFlow(DateUtils.formatDate(1990, 1, 1))
    override val themeColors = MutableStateFlow(LakersColors)
    override val user = MutableStateFlow<User?>(null)

    override suspend fun updateStatsCookies(cookies: Set<String>) {
        statsCookies.value = cookies
    }

    override suspend fun updateLastAccessedDay(year: Int, month: Int, day: Int) {
        lastAccessedDay.value = DateUtils.formatDate(year, month, day)
    }

    override suspend fun updateThemeColorsTeamId(teamId: Int) {
        val team = NBATeam.getTeamById(teamId)
        val color = team?.colors ?: teamOfficial.colors
        themeColors.value = color
    }

    override suspend fun updateUser(user: User?) {
        this.user.value = user
    }

    fun clear() {
        statsCookies.value = emptySet()
        lastAccessedDay.value = ""
        themeColors.value = LakersColors
        user.value = null
    }
}