package com.jiachian.nbatoday.datastore.data

import com.jiachian.nbatoday.compose.theme.LakersColors
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow

class TestDataStore : BaseDataStore {
    override val lastAccessedDate = MutableStateFlow("")
    override val themeColors = MutableStateFlow(LakersColors)
    override val user = MutableStateFlow<User?>(null)

    override suspend fun updateLastAccessedDate(year: Int, month: Int, day: Int) {
        lastAccessedDate.value = DateUtils.formatDate(year, month, day)
    }

    override suspend fun updateThemeColorsTeamId(teamId: Int) {
        themeColors.value = NBATeam.getTeamById(teamId).colors
    }

    override suspend fun updateUser(user: User?) {
        this.user.value = user
    }
}
