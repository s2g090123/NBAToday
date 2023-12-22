package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.data.remote.RemoteGameGenerator
import com.jiachian.nbatoday.models.local.game.GameTeam
import com.jiachian.nbatoday.models.remote.game.extensions.toGameTeam
import com.jiachian.nbatoday.utils.getOrAssert

object GameTeamGenerator {
    fun getHome(): GameTeam {
        return RemoteGameGenerator
            .get()
            .scoreboard
            ?.games
            ?.get(0)
            ?.homeTeam
            ?.toGameTeam()
            .getOrAssert()
    }

    fun getAway(): GameTeam {
        return RemoteGameGenerator
            .get()
            .scoreboard
            ?.games
            ?.get(0)
            ?.homeTeam
            ?.toGameTeam()
            .getOrAssert()
    }
}
