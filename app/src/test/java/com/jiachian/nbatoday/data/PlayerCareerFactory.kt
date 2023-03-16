package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.*
import com.jiachian.nbatoday.data.local.player.PlayerCareer

object PlayerCareerFactory {

    fun createHomePlayerCareer(): PlayerCareer {
        return PlayerCareer(
            personId = HOME_PLAYER_ID,
            info = createHomePlayerInfo(),
            stats = createHomePlayerStats()
        )
    }

    fun createAwayPlayerCareer(): PlayerCareer {
        return PlayerCareer(
            personId = AWAY_PLAYER_ID,
            info = createAwayPlayerInfo(),
            stats = createAwayPlayerStats()
        )
    }

    private fun createHomePlayerInfo(): PlayerCareer.PlayerCareerInfo {
        return PlayerCareer.PlayerCareerInfo(
            playerName = HOME_PLAYER_FULL_NAME,
            playerNameAbbr = HOME_PLAYER_LAST_NAME,
            playerAge = BASIC_NUMBER,
            birthDate = GAME_DATE,
            country = TEAM_CITY,
            school = TEAM_CITY,
            height = BASIC_NUMBER.toDouble(),
            weight = BASIC_NUMBER.toDouble(),
            seasonExperience = BASIC_NUMBER,
            jersey = BASIC_NUMBER,
            position = BASIC_POSITION,
            teamId = HOME_TEAM_ID,
            teamName = HOME_TEAM_NAME,
            teamNameAbbr = HOME_TEAM_ABBR,
            teamCity = TEAM_CITY,
            fromYear = BASIC_NUMBER,
            toYear = BASIC_NUMBER,
            draftYear = BASIC_NUMBER,
            draftRound = BASIC_NUMBER,
            draftNumber = BASIC_NUMBER,
            isGreatest75 = true,
            headlineStats = createHeadlineStats()
        )
    }

    private fun createAwayPlayerInfo(): PlayerCareer.PlayerCareerInfo {
        return PlayerCareer.PlayerCareerInfo(
            playerName = AWAY_PLAYER_FULL_NAME,
            playerNameAbbr = AWAY_PLAYER_LAST_NAME,
            playerAge = BASIC_NUMBER,
            birthDate = GAME_DATE,
            country = TEAM_CITY,
            school = TEAM_CITY,
            height = BASIC_NUMBER.toDouble(),
            weight = BASIC_NUMBER.toDouble(),
            seasonExperience = BASIC_NUMBER,
            jersey = BASIC_NUMBER,
            position = BASIC_POSITION,
            teamId = AWAY_TEAM_ID,
            teamName = AWAY_TEAM_NAME,
            teamNameAbbr = AWAY_TEAM_ABBR,
            teamCity = TEAM_CITY,
            fromYear = BASIC_NUMBER,
            toYear = BASIC_NUMBER,
            draftYear = BASIC_NUMBER,
            draftRound = BASIC_NUMBER,
            draftNumber = BASIC_NUMBER,
            isGreatest75 = true,
            headlineStats = createHeadlineStats()
        )
    }

    private fun createHeadlineStats(): PlayerCareer.PlayerCareerInfo.HeadlineStats {
        return PlayerCareer.PlayerCareerInfo.HeadlineStats(
            timeFrame = GAME_SEASON,
            points = BASIC_NUMBER.toDouble(),
            assists = BASIC_NUMBER.toDouble(),
            rebounds = BASIC_NUMBER.toDouble(),
            impact = BASIC_PERCENTAGE
        )
    }

    private fun createHomePlayerStats(): PlayerCareer.PlayerCareerStats {
        return PlayerCareer.PlayerCareerStats(
            careerStats = createHomeStats(),
            careerRank = createHomeRanks()
        )
    }

    private fun createAwayPlayerStats(): PlayerCareer.PlayerCareerStats {
        return PlayerCareer.PlayerCareerStats(
            careerStats = createAwayStats(),
            careerRank = createAwayRanks()
        )
    }

    private fun createHomeStats(): ArrayList<PlayerCareer.PlayerCareerStats.Stats> {
        return arrayListOf(
            PlayerCareer.PlayerCareerStats.Stats(
                timeFrame = GAME_SEASON,
                teamId = HOME_TEAM_ID,
                teamNameAbbr = HOME_TEAM_ABBR,
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
        )
    }

    private fun createAwayStats(): ArrayList<PlayerCareer.PlayerCareerStats.Stats> {
        return arrayListOf(
            PlayerCareer.PlayerCareerStats.Stats(
                timeFrame = GAME_SEASON,
                teamId = AWAY_TEAM_ID,
                teamNameAbbr = AWAY_TEAM_ABBR,
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
        )
    }

    private fun createHomeRanks(): ArrayList<PlayerCareer.PlayerCareerStats.Rank> {
        return arrayListOf(
            PlayerCareer.PlayerCareerStats.Rank(
                timeFrame = GAME_SEASON,
                teamId = HOME_TEAM_ID,
                teamNameAbbr = HOME_TEAM_ABBR,
                gamePlayedRank = BASIC_NUMBER,
                winRank = BASIC_NUMBER,
                loseRank = BASIC_NUMBER,
                winPercentageRank = BASIC_NUMBER,
                fieldGoalsMadeRank = BASIC_NUMBER,
                fieldGoalsAttemptedRank = BASIC_NUMBER,
                fieldGoalsPercentageRank = BASIC_NUMBER,
                threePointersMadeRank = BASIC_NUMBER,
                threePointersAttemptedRank = BASIC_NUMBER,
                threePointersPercentageRank = BASIC_NUMBER,
                freeThrowsMadeRank = BASIC_NUMBER,
                freeThrowsAttemptedRank = BASIC_NUMBER,
                freeThrowsPercentageRank = BASIC_NUMBER,
                reboundsOffensiveRank = BASIC_NUMBER,
                reboundsDefensiveRank = BASIC_NUMBER,
                reboundsTotalRank = BASIC_NUMBER,
                assistsRank = BASIC_NUMBER,
                turnoversRank = BASIC_NUMBER,
                stealsRank = BASIC_NUMBER,
                blocksRank = BASIC_NUMBER,
                foulsPersonalRank = BASIC_NUMBER,
                pointsRank = BASIC_NUMBER,
                plusMinusRank = BASIC_NUMBER
            )
        )
    }

    private fun createAwayRanks(): ArrayList<PlayerCareer.PlayerCareerStats.Rank> {
        return arrayListOf(
            PlayerCareer.PlayerCareerStats.Rank(
                timeFrame = GAME_SEASON,
                teamId = AWAY_TEAM_ID,
                teamNameAbbr = AWAY_TEAM_ABBR,
                gamePlayedRank = BASIC_NUMBER,
                winRank = BASIC_NUMBER,
                loseRank = BASIC_NUMBER,
                winPercentageRank = BASIC_NUMBER,
                fieldGoalsMadeRank = BASIC_NUMBER,
                fieldGoalsAttemptedRank = BASIC_NUMBER,
                fieldGoalsPercentageRank = BASIC_NUMBER,
                threePointersMadeRank = BASIC_NUMBER,
                threePointersAttemptedRank = BASIC_NUMBER,
                threePointersPercentageRank = BASIC_NUMBER,
                freeThrowsMadeRank = BASIC_NUMBER,
                freeThrowsAttemptedRank = BASIC_NUMBER,
                freeThrowsPercentageRank = BASIC_NUMBER,
                reboundsOffensiveRank = BASIC_NUMBER,
                reboundsDefensiveRank = BASIC_NUMBER,
                reboundsTotalRank = BASIC_NUMBER,
                assistsRank = BASIC_NUMBER,
                turnoversRank = BASIC_NUMBER,
                stealsRank = BASIC_NUMBER,
                blocksRank = BASIC_NUMBER,
                foulsPersonalRank = BASIC_NUMBER,
                pointsRank = BASIC_NUMBER,
                plusMinusRank = BASIC_NUMBER
            )
        )
    }
}