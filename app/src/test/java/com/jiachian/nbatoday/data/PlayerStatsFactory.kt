package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.*
import com.jiachian.nbatoday.data.local.player.PlayerStats

object PlayerStatsFactory {

    fun getHomePlayerStats(): PlayerStats {
        return PlayerStats(
            playerId = HOME_PLAYER_ID,
            teamId = HOME_TEAM_ID,
            playerName = HOME_PLAYER_FULL_NAME,
            playerNickName = HOME_PLAYER_LAST_NAME,
            gamePlayed = BASIC_NUMBER,
            win = BASIC_NUMBER,
            lose = BASIC_NUMBER,
            winPercentage = BASIC_PERCENTAGE,
            fieldGoalsMade = BASIC_NUMBER,
            fieldGoalsAttempted = BASIC_NUMBER,
            fieldGoalsPercentage = BASIC_PERCENTAGE,
            threePointersMade = BASIC_NUMBER,
            threePointersAttempted = BASIC_NUMBER,
            threePointersPercentage = BASIC_PERCENTAGE,
            freeThrowsMade = BASIC_NUMBER,
            freeThrowsAttempted = BASIC_NUMBER,
            freeThrowsPercentage = BASIC_PERCENTAGE,
            reboundsOffensive = BASIC_NUMBER,
            reboundsDefensive = BASIC_NUMBER,
            reboundsTotal = BASIC_NUMBER,
            assists = BASIC_NUMBER,
            turnovers = BASIC_NUMBER,
            steals = BASIC_NUMBER,
            blocks = BASIC_NUMBER,
            foulsPersonal = BASIC_NUMBER,
            points = BASIC_NUMBER,
            plusMinus = BASIC_NUMBER
        )
    }

    fun getAwayPlayerStats(): PlayerStats {
        return PlayerStats(
            playerId = AWAY_PLAYER_ID,
            teamId = AWAY_TEAM_ID,
            playerName = AWAY_PLAYER_FULL_NAME,
            playerNickName = AWAY_PLAYER_LAST_NAME,
            gamePlayed = BASIC_NUMBER,
            win = BASIC_NUMBER,
            lose = BASIC_NUMBER,
            winPercentage = BASIC_PERCENTAGE,
            fieldGoalsMade = BASIC_NUMBER,
            fieldGoalsAttempted = BASIC_NUMBER,
            fieldGoalsPercentage = BASIC_PERCENTAGE,
            threePointersMade = BASIC_NUMBER,
            threePointersAttempted = BASIC_NUMBER,
            threePointersPercentage = BASIC_PERCENTAGE,
            freeThrowsMade = BASIC_NUMBER,
            freeThrowsAttempted = BASIC_NUMBER,
            freeThrowsPercentage = BASIC_PERCENTAGE,
            reboundsOffensive = BASIC_NUMBER,
            reboundsDefensive = BASIC_NUMBER,
            reboundsTotal = BASIC_NUMBER,
            assists = BASIC_NUMBER,
            turnovers = BASIC_NUMBER,
            steals = BASIC_NUMBER,
            blocks = BASIC_NUMBER,
            foulsPersonal = BASIC_NUMBER,
            points = BASIC_NUMBER,
            plusMinus = BASIC_NUMBER
        )
    }
}