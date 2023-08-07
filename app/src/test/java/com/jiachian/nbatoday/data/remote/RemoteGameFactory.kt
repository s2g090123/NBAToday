package com.jiachian.nbatoday.data.remote

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
import com.jiachian.nbatoday.GAME_DAY
import com.jiachian.nbatoday.GAME_SEASON
import com.jiachian.nbatoday.GAME_STATUS_FINAL
import com.jiachian.nbatoday.HOME_PLAYER_FIRST_NAME
import com.jiachian.nbatoday.HOME_PLAYER_FULL_NAME
import com.jiachian.nbatoday.HOME_PLAYER_ID
import com.jiachian.nbatoday.HOME_PLAYER_LAST_NAME
import com.jiachian.nbatoday.HOME_TEAM_ABBR
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.HOME_TEAM_NAME
import com.jiachian.nbatoday.NBA_LEAGUE_ID
import com.jiachian.nbatoday.TEAM_CITY
import com.jiachian.nbatoday.data.GameLeaderFactory
import com.jiachian.nbatoday.data.GameTeamFactory
import com.jiachian.nbatoday.data.remote.game.GameScoreboard
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.game.PeriodType
import com.jiachian.nbatoday.data.remote.game.Schedule
import com.jiachian.nbatoday.data.remote.score.PlayerActiveStatus
import com.jiachian.nbatoday.data.remote.score.RemoteGameBoxScore

object RemoteGameFactory {
    fun getRemoteGameScoreboard(): GameScoreboard {
        return GameScoreboard(
            scoreboard = GameScoreboard.Scoreboard(
                gameDate = GAME_DATE,
                games = listOf(
                    GameScoreboard.Scoreboard.Game(
                        gameId = FINAL_GAME_ID,
                        gameCode = GAME_CODE,
                        gameStatus = BASIC_NUMBER,
                        gameStatusText = GAME_STATUS_FINAL,
                        gameTime = GAME_DATE,
                        gameLeaders = GameLeaderFactory.getGameLeaders(),
                        teamLeaders = GameLeaderFactory.getGameLeaders(),
                        homeTeam = GameTeamFactory.getDefaultHomeTeam(),
                        awayTeam = GameTeamFactory.getDefaultAwayTeam()
                    )
                )
            )
        )
    }

    fun getRemoteSchedule(): Schedule {
        return Schedule(
            Schedule.LeagueSchedule(
                gameDates = listOf(
                    Schedule.LeagueSchedule.GameDate(
                        gameDate = GAME_DATE,
                        games = listOf(
                            Schedule.LeagueSchedule.GameDate.Game(
                                awayTeam = Schedule.LeagueSchedule.GameDate.Game.AwayTeam(
                                    losses = BASIC_NUMBER,
                                    score = BASIC_NUMBER,
                                    teamCity = TEAM_CITY,
                                    teamId = AWAY_TEAM_ID,
                                    teamName = AWAY_TEAM_NAME,
                                    teamTricode = AWAY_TEAM_ABBR,
                                    wins = BASIC_NUMBER
                                ),
                                day = GAME_DAY,
                                gameCode = GAME_CODE,
                                gameId = FINAL_GAME_ID,
                                gameStatus = GameStatusCode.FINAL,
                                gameStatusText = GAME_STATUS_FINAL,
                                gameSequence = BASIC_NUMBER,
                                homeTeam = Schedule.LeagueSchedule.GameDate.Game.HomeTeam(
                                    losses = BASIC_NUMBER,
                                    score = BASIC_NUMBER,
                                    teamCity = TEAM_CITY,
                                    teamId = HOME_TEAM_ID,
                                    teamName = HOME_TEAM_NAME,
                                    teamTricode = HOME_TEAM_ABBR,
                                    wins = BASIC_NUMBER
                                ),
                                gameDateEst = GAME_DATE,
                                gameDateTimeEst = GAME_DATE,
                                monthNum = BASIC_NUMBER,
                                pointsLeaders = listOf(
                                    Schedule.LeagueSchedule.GameDate.Game.PointsLeader(
                                        firstName = HOME_PLAYER_FIRST_NAME,
                                        lastName = HOME_PLAYER_LAST_NAME,
                                        personId = HOME_PLAYER_ID,
                                        points = BASIC_NUMBER.toDouble(),
                                        teamId = HOME_TEAM_ID,
                                        teamName = HOME_TEAM_NAME,
                                        teamTricode = HOME_TEAM_ABBR
                                    )
                                ),
                                weekNumber = BASIC_NUMBER
                            )
                        )
                    )
                ),
                leagueId = NBA_LEAGUE_ID,
                seasonYear = GAME_SEASON
            )
        )
    }

    fun getRemoteGameBoxScore(): RemoteGameBoxScore {
        return RemoteGameBoxScore(
            game = RemoteGameBoxScore.Game(
                gameId = FINAL_GAME_ID,
                gameEt = GAME_DATE,
                gameCode = GAME_CODE,
                gameStatusText = GAME_STATUS_FINAL,
                gameStatus = GameStatusCode.FINAL,
                homeTeam = RemoteGameBoxScore.Game.Team(
                    teamId = HOME_TEAM_ID,
                    teamName = HOME_TEAM_NAME,
                    teamCity = TEAM_CITY,
                    teamTricode = HOME_TEAM_ABBR,
                    score = BASIC_NUMBER,
                    inBonus = "1",
                    timeoutsRemaining = BASIC_NUMBER,
                    periods = listOf(
                        RemoteGameBoxScore.Game.Team.Period(
                            period = 1,
                            periodType = PeriodType.REGULAR,
                            score = BASIC_NUMBER
                        )
                    ),
                    players = listOf(
                        RemoteGameBoxScore.Game.Team.Player(
                            status = PlayerActiveStatus.ACTIVE,
                            notPlayingReason = "",
                            order = BASIC_NUMBER,
                            personId = HOME_PLAYER_ID,
                            jerseyNum = BASIC_NUMBER.toString(),
                            position = BASIC_POSITION,
                            starter = "1",
                            oncourt = "1",
                            played = "1",
                            statistics = RemoteGameBoxScore.Game.Team.Player.Statistics(
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
                                minus = BASIC_NUMBER.toDouble(),
                                minutes = BASIC_MINUTES,
                                plus = BASIC_NUMBER.toDouble(),
                                plusMinusPoints = BASIC_NUMBER.toDouble(),
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
                                twoPointersPercentage = BASIC_PERCENTAGE
                            ),
                            name = HOME_PLAYER_FULL_NAME,
                            nameI = HOME_PLAYER_FULL_NAME,
                            firstName = HOME_PLAYER_FIRST_NAME,
                            familyName = HOME_PLAYER_LAST_NAME
                        )
                    ),
                    statistics = RemoteGameBoxScore.Game.Team.Statistics(
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
                        reboundsDefensive = BASIC_NUMBER,
                        reboundsOffensive = BASIC_NUMBER,
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
                ),
                awayTeam = RemoteGameBoxScore.Game.Team(
                    teamId = AWAY_TEAM_ID,
                    teamName = AWAY_TEAM_NAME,
                    teamCity = TEAM_CITY,
                    teamTricode = AWAY_TEAM_ABBR,
                    score = BASIC_NUMBER,
                    inBonus = "1",
                    timeoutsRemaining = BASIC_NUMBER,
                    periods = listOf(
                        RemoteGameBoxScore.Game.Team.Period(
                            period = 1,
                            periodType = PeriodType.REGULAR,
                            score = BASIC_NUMBER
                        )
                    ),
                    players = listOf(
                        RemoteGameBoxScore.Game.Team.Player(
                            status = PlayerActiveStatus.ACTIVE,
                            notPlayingReason = "",
                            order = BASIC_NUMBER,
                            personId = AWAY_PLAYER_ID,
                            jerseyNum = BASIC_NUMBER.toString(),
                            position = BASIC_POSITION,
                            starter = "1",
                            oncourt = "1",
                            played = "1",
                            statistics = RemoteGameBoxScore.Game.Team.Player.Statistics(
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
                                minus = BASIC_NUMBER.toDouble(),
                                minutes = BASIC_MINUTES,
                                plus = BASIC_NUMBER.toDouble(),
                                plusMinusPoints = BASIC_NUMBER.toDouble(),
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
                                twoPointersPercentage = BASIC_PERCENTAGE
                            ),
                            name = AWAY_PLAYER_FULL_NAME,
                            nameI = AWAY_PLAYER_FULL_NAME,
                            firstName = AWAY_PLAYER_FIRST_NAME,
                            familyName = AWAY_PLAYER_LAST_NAME
                        )
                    ),
                    statistics = RemoteGameBoxScore.Game.Team.Statistics(
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
                        reboundsDefensive = BASIC_NUMBER,
                        reboundsOffensive = BASIC_NUMBER,
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
                )
            )
        )
    }
}