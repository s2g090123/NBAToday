package com.jiachian.nbatoday.datastore.data

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.utils.DateUtils

class TestDataStore(
    dataHolder: DataHolder
) : BaseDataStore {
    override val lastAccessedDate = dataHolder.lastAccessedDate
    override val themeColors = dataHolder.themeColors
    override val user = dataHolder.user

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
