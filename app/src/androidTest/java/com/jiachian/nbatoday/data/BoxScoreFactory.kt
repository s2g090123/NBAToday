package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.AWAY_PLAYER_FIRST_NAME
import com.jiachian.nbatoday.AWAY_PLAYER_FULL_NAME
import com.jiachian.nbatoday.AWAY_PLAYER_ID
import com.jiachian.nbatoday.AWAY_PLAYER_LAST_NAME
import com.jiachian.nbatoday.AWAY_TEAM_ABBR
import com.jiachian.nbatoday.AWAY_TEAM_ID
import com.jiachian.nbatoday.AWAY_TEAM_NAME
import com.jiachian.nbatoday.BASIC_MINUTES
import com.jiachian.nbatoday.BASIC_NUMBER
import com.jiachian.nbatoday.BASIC_PERCENTAGE
import com.jiachian.nbatoday.BASIC_POSITION
import com.jiachian.nbatoday.FINAL_GAME_ID
import com.jiachian.nbatoday.GAME_CODE
import com.jiachian.nbatoday.GAME_DATE
import com.jiachian.nbatoday.GAME_STATUS_FINAL
import com.jiachian.nbatoday.GAME_STATUS_PREPARE
import com.jiachian.nbatoday.HOME_PLAYER_FIRST_NAME
import com.jiachian.nbatoday.HOME_PLAYER_FULL_NAME
import com.jiachian.nbatoday.HOME_PLAYER_ID
import com.jiachian.nbatoday.HOME_PLAYER_LAST_NAME
import com.jiachian.nbatoday.HOME_TEAM_ABBR
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.HOME_TEAM_NAME
import com.jiachian.nbatoday.PLAYING_GAME_ID
import com.jiachian.nbatoday.TEAM_CITY
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.score.PlayerActiveStatus

object BoxScoreFactory {

    fun getFinalGameBoxScore(): GameBoxScore {
        return GameBoxScore(
            gameId = FINAL_GAME_ID,
            gameDate = GAME_DATE,
            gameCode = GAME_CODE,
            gameStatusText = GAME_STATUS_FINAL,
            gameStatus = GameStatusCode.FINAL,
            homeTeam = getHomeTeamScore(),
            awayTeam = getAwayTeamScore()
        )
    }

    fun getPlayingGameBoxScore(): GameBoxScore {
        return GameBoxScore(
            gameId = PLAYING_GAME_ID,
            gameDate = GAME_DATE,
            gameCode = GAME_CODE,
            gameStatusText = GAME_STATUS_PREPARE,
            gameStatus = GameStatusCode.PLAYING,
            homeTeam = getHomeTeamScore(),
            awayTeam = getAwayTeamScore()
        )
    }

    private fun getHomeTeamScore(): GameBoxScore.BoxScoreTeam {
        return GameBoxScore.BoxScoreTeam(
            teamId = HOME_TEAM_ID,
            teamName = HOME_TEAM_NAME,
            teamCity = TEAM_CITY,
            teamTricode = HOME_TEAM_ABBR,
            score = BASIC_NUMBER * 4,
            inBonus = true,
            timeoutsRemaining = BASIC_NUMBER,
            periods = createPeriods(),
            players = createHomePlayers(),
            statistics = createTeamStatics()
        )
    }

    private fun getAwayTeamScore(): GameBoxScore.BoxScoreTeam {
        return GameBoxScore.BoxScoreTeam(
            teamId = AWAY_TEAM_ID,
            teamName = AWAY_TEAM_NAME,
            teamCity = TEAM_CITY,
            teamTricode = AWAY_TEAM_ABBR,
            score = BASIC_NUMBER * 4,
            inBonus = true,
            timeoutsRemaining = BASIC_NUMBER,
            periods = createPeriods(),
            players = createAwayPlayers(),
            statistics = createTeamStatics()
        )
    }

    private fun createPeriods(): List<GameBoxScore.BoxScoreTeam.Period> {
        return (1..4).map {
            GameBoxScore.BoxScoreTeam.Period(
                period = it,
                periodLabel = when (it) {
                    1 -> "1st"
                    2 -> "2nd"
                    3 -> "3rd"
                    else -> "${it}th"
                },
                score = BASIC_NUMBER
            )
        }
    }

    private fun createHomePlayers(): List<GameBoxScore.BoxScoreTeam.Player> {
        return listOf(
            GameBoxScore.BoxScoreTeam.Player(
                status = PlayerActiveStatus.ACTIVE,
                notPlayingReason = null,
                order = 1,
                personId = HOME_PLAYER_ID,
                jerseyNum = BASIC_NUMBER.toString(),
                position = BASIC_POSITION,
                starter = true,
                onCourt = true,
                played = true,
                statistics = createPlayerStatics(),
                name = HOME_PLAYER_FULL_NAME,
                nameAbbr = HOME_PLAYER_LAST_NAME,
                firstName = HOME_PLAYER_FIRST_NAME,
                familyName = HOME_PLAYER_LAST_NAME
            )
        )
    }

    private fun createAwayPlayers(): List<GameBoxScore.BoxScoreTeam.Player> {
        return listOf(
            GameBoxScore.BoxScoreTeam.Player(
                status = PlayerActiveStatus.ACTIVE,
                notPlayingReason = null,
                order = 1,
                personId = AWAY_PLAYER_ID,
                jerseyNum = BASIC_NUMBER.toString(),
                position = BASIC_POSITION,
                starter = true,
                onCourt = true,
                played = true,
                statistics = createPlayerStatics(),
                name = AWAY_PLAYER_FULL_NAME,
                nameAbbr = AWAY_PLAYER_LAST_NAME,
                firstName = AWAY_PLAYER_FIRST_NAME,
                familyName = AWAY_PLAYER_LAST_NAME
            )
        )
    }

    private fun createTeamStatics(): GameBoxScore.BoxScoreTeam.Statistics {
        return GameBoxScore.BoxScoreTeam.Statistics(
            assists = BASIC_NUMBER,
            blocks = BASIC_NUMBER,
            blocksReceived = BASIC_NUMBER,
            fieldGoalsAttempted = BASIC_NUMBER,
            fieldGoalsMade = BASIC_NUMBER,
            fieldGoalsPercentage = BASIC_PERCENTAGE,
            foulsOffensive = BASIC_NUMBER,
            foulsDrawn = BASIC_NUMBER,
            foulsPersonal = BASIC_NUMBER,
            foulsTeam = BASIC_NUMBER,
            foulsTechnical = BASIC_NUMBER,
            freeThrowsAttempted = BASIC_NUMBER,
            freeThrowsMade = BASIC_NUMBER,
            freeThrowsPercentage = BASIC_PERCENTAGE,
            points = BASIC_NUMBER,
            reboundsOffensive = BASIC_NUMBER,
            reboundsDefensive = BASIC_NUMBER,
            reboundsPersonal = BASIC_NUMBER,
            reboundsTotal = BASIC_NUMBER,
            steals = BASIC_NUMBER,
            threePointersAttempted = BASIC_NUMBER,
            threePointersMade = BASIC_NUMBER,
            threePointersPercentage = BASIC_PERCENTAGE,
            turnovers = BASIC_NUMBER,
            turnoversTeam = BASIC_NUMBER,
            turnoversTotal = BASIC_NUMBER,
            twoPointersAttempted = BASIC_NUMBER,
            twoPointersMade = BASIC_NUMBER,
            twoPointersPercentage = BASIC_PERCENTAGE,
            pointsFastBreak = BASIC_NUMBER,
            pointsFromTurnovers = BASIC_NUMBER,
            pointsInThePaint = BASIC_NUMBER,
            pointsSecondChance = BASIC_NUMBER,
            benchPoints = BASIC_NUMBER
        )
    }

    private fun createPlayerStatics(): GameBoxScore.BoxScoreTeam.Player.Statistics {
        return GameBoxScore.BoxScoreTeam.Player.Statistics(
            assists = BASIC_NUMBER,
            blocks = BASIC_NUMBER,
            blocksReceived = BASIC_NUMBER,
            fieldGoalsAttempted = BASIC_NUMBER,
            fieldGoalsMade = BASIC_NUMBER,
            fieldGoalsPercentage = BASIC_PERCENTAGE,
            foulsOffensive = BASIC_NUMBER,
            foulsDrawn = BASIC_NUMBER,
            foulsPersonal = BASIC_NUMBER,
            foulsTechnical = BASIC_NUMBER,
            freeThrowsAttempted = BASIC_NUMBER,
            freeThrowsMade = BASIC_NUMBER,
            freeThrowsPercentage = BASIC_PERCENTAGE,
            minus = BASIC_NUMBER,
            minutes = BASIC_MINUTES,
            plus = BASIC_NUMBER,
            plusMinusPoints = BASIC_NUMBER,
            points = BASIC_NUMBER,
            reboundsOffensive = BASIC_NUMBER,
            reboundsDefensive = BASIC_NUMBER,
            reboundsTotal = BASIC_NUMBER,
            steals = BASIC_NUMBER,
            threePointersAttempted = BASIC_NUMBER,
            threePointersMade = BASIC_NUMBER,
            threePointersPercentage = BASIC_PERCENTAGE,
            turnovers = BASIC_NUMBER,
            twoPointersAttempted = BASIC_NUMBER,
            twoPointersMade = BASIC_NUMBER,
            twoPointersPercentage = BASIC_PERCENTAGE,
        )
    }
}
