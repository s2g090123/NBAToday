package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team

object TeamGenerator {
    fun getHome(): Team {
        return Team(
            teamId = HomeTeamId,
            team = NBATeamGenerator.getHome(),
            teamConference = NBATeam.Conference.EAST,
            gamePlayed = BasicNumber,
            win = BasicNumber,
            lose = BasicNumber,
            winPercentage = BasicPercentage,
            fieldGoalsPercentage = BasicPercentage,
            threePointersPercentage = BasicPercentage,
            freeThrowsPercentage = BasicPercentage,
            reboundsDefensive = BasicNumber,
            reboundsOffensive = BasicNumber,
            reboundsTotal = BasicNumber,
            assists = BasicNumber,
            turnovers = BasicNumber,
            steals = BasicNumber,
            blocks = BasicNumber,
            points = BasicNumber,
            plusMinus = BasicNumber.toDouble()
        )
    }

    fun getAway(): Team {
        return Team(
            teamId = AwayTeamId,
            team = NBATeamGenerator.getAway(),
            teamConference = NBATeam.Conference.WEST,
            gamePlayed = BasicNumber,
            win = BasicNumber,
            lose = BasicNumber,
            winPercentage = BasicPercentage,
            fieldGoalsPercentage = BasicPercentage,
            threePointersPercentage = BasicPercentage,
            freeThrowsPercentage = BasicPercentage,
            reboundsDefensive = BasicNumber,
            reboundsOffensive = BasicNumber,
            reboundsTotal = BasicNumber,
            assists = BasicNumber,
            turnovers = BasicNumber,
            steals = BasicNumber,
            blocks = BasicNumber,
            points = BasicNumber,
            plusMinus = BasicNumber.toDouble()
        )
    }
}
