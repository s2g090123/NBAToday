package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.data.remote.RemoteScheduleGenerator
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.remote.game.extensions.toGames
import com.jiachian.nbatoday.utils.getOrAssert

object GameGenerator {
    fun getFinal(): Game {
        return RemoteScheduleGenerator
            .get()
            .leagueSchedule
            ?.toGames()
            ?.get(0)
            .getOrAssert()
    }

    fun getPlaying(): Game {
        return RemoteScheduleGenerator
            .get()
            .leagueSchedule
            ?.toGames()
            ?.get(1)
            .getOrAssert()
    }

    fun getComingSoon(): Game {
        return RemoteScheduleGenerator
            .get()
            .leagueSchedule
            ?.toGames()
            ?.get(2)
            .getOrAssert()
    }
}
