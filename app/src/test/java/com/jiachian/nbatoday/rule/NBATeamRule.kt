package com.jiachian.nbatoday.rule

import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.data.local.NBATeamGenerator
import com.jiachian.nbatoday.models.local.team.NBATeam
import io.mockk.every
import io.mockk.mockkObject
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
        /**
         * WORKAROUND!
         * When dealing with #52-collectAsStateWithLifecycle, upgraded the android gradle plugin and gradle versions,
         * and then started encountering the `io.mockk.MockKException: can't find stub Companion(Companion)` problem.
         *
         * Unable to confirm the exact cause of the issue,
         * but suspect it may be related to issues with unmock.
         * Therefore, commented out the following code for now.
         */
        //unmockkObject(NBATeam)
    }
}
