package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.models.local.game.GameTeam

object GameTeamGenerator {
    fun getHome(): GameTeam {
        return GameTeam(
            team = NBATeamGenerator.getHome(),
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
        )
    }

    fun getAway(): GameTeam {
        return GameTeam(
            team = NBATeamGenerator.getAway(),
            losses = BasicNumber,
            score = BasicNumber,
            wins = BasicNumber,
        )
    }
}
