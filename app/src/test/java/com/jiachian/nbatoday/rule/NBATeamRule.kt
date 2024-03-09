package com.jiachian.nbatoday.rule

import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.local.NBATeamGenerator
import com.jiachian.nbatoday.models.local.team.NBATeam
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class NBATeamRule : TestWatcher() {
    override fun starting(description: Description) {
        mockkObject(NBATeam)
        every {
            NBATeam.getTeamById(any())
        } answers {
            when (firstArg<Int>()) {
                HomeTeamId -> NBATeamGenerator.getHome()
                else -> NBATeamGenerator.getAway()
            }
        }
    }

    override fun finished(description: Description) {
        unmockkObject(NBATeam)
    }
}
