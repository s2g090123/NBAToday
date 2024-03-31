package com.jiachian.nbatoday.home.user.domain

import com.jiachian.nbatoday.common.data.datastore.BaseDataStore

class UpdateTheme(
    private val dataStore: BaseDataStore
) {
    suspend operator fun invoke(teamId: Int) {
        dataStore.updateThemeColorsTeamId(teamId)
    }
}
