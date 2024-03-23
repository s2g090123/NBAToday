package com.jiachian.nbatoday.usecase.user

import com.jiachian.nbatoday.datastore.BaseDataStore

class UpdateTheme(
    private val dataStore: BaseDataStore
) {
    suspend operator fun invoke(teamId: Int) {
        dataStore.updateThemeColorsTeamId(teamId)
    }
}
