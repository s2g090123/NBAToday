package com.jiachian.nbatoday.data.remote

import com.jiachian.nbatoday.AwayPlayerFirstName
import com.jiachian.nbatoday.AwayPlayerFullName
import com.jiachian.nbatoday.AwayPlayerId
import com.jiachian.nbatoday.AwayPlayerLastName
import com.jiachian.nbatoday.AwayTeamAbbr
import com.jiachian.nbatoday.AwayTeamId
import com.jiachian.nbatoday.AwayTeamName
import com.jiachian.nbatoday.BasicMinutes
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.BasicPercentage
import com.jiachian.nbatoday.BasicPosition
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameCode
import com.jiachian.nbatoday.GameDate
import com.jiachian.nbatoday.GameDateTime
import com.jiachian.nbatoday.GameDay
import com.jiachian.nbatoday.GameSeason
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.HomePlayerFirstName
import com.jiachian.nbatoday.HomePlayerFullName
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomePlayerLastName
import com.jiachian.nbatoday.HomeTeamAbbr
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.HomeTeamName
import com.jiachian.nbatoday.NbaLeagueId
import com.jiachian.nbatoday.TeamCity
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
                gameDate = GameDate,
                games = listOf(
                    GameScoreboard.Scoreboard.Game(
                        gameId = FinalGameId,
                        gameCode = GameCode,
                        gameStatus = GameStatusCode.FINAL.status,
                        gameStatusText = GameStatusFinal,
                        gameTime = GameDate,
                        gameLeaders = GameLeaderFactory.getGameLeaders(),
                        teamLeaders = GameLeaderFactory.getGameLeaders(),
                        homeTeam = GameTeamFactory.getDefaultRemoteHomeTeam(),
                        awayTeam = GameTeamFactory.getDefaultRemoteAwayTeam()
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
                        gameDate = GameDate,
                        games = listOf(
                            Schedule.LeagueSchedule.GameDate.Game(
                                awayTeam = Schedule.LeagueSchedule.GameDate.Game.Team(
                                    losses = BasicNumber,
                                    score = BasicNumber,
                                    teamCity = TeamCity,
                                    teamId = AwayTeamId,
                                    teamName = AwayTeamName,
                                    teamTricode = AwayTeamAbbr,
                                    wins = BasicNumber
                                ),
                                day = GameDay,
                                gameCode = GameCode,
                                gameId = FinalGameId,
                                gameStatus = GameStatusCode.FINAL,
                                gameStatusText = GameStatusFinal,
                                gameSequence = BasicNumber,
                                homeTeam = Schedule.LeagueSchedule.GameDate.Game.HomeTeam(
                                    losses = BasicNumber,
                                    score = BasicNumber,
                                    teamCity = TeamCity,
                                    teamId = HomeTeamId,
                                    teamName = HomeTeamName,
                                    teamTricode = HomeTeamAbbr,
                                    wins = BasicNumber
                                ),
                                gameDateEst = GameDateTime,
                                gameDateTimeEst = GameDateTime,
                                monthNum = BasicNumber,
                                pointsLeaders = listOf(
                                    Schedule.LeagueSchedule.GameDate.Game.PointsLeader(
                                        firstName = HomePlayerFirstName,
                                        lastName = HomePlayerLastName,
                                        personId = HomePlayerId,
                                        points = BasicNumber.toDouble(),
                                        teamId = HomeTeamId,
                                        teamName = HomeTeamName,
                                        teamTricode = HomeTeamAbbr
                                    )
                                ),
                                weekNumber = BasicNumber
                            )
                        )
                    )
                ),
                leagueId = NbaLeagueId,
                seasonYear = GameSeason
            )
        )
    }

    fun getRemoteGameBoxScore(): RemoteGameBoxScore {
        return RemoteGameBoxScore(
            game = RemoteGameBoxScore.Game(
                gameId = FinalGameId,
                gameEt = GameDate,
                gameCode = GameCode,
                gameStatusText = GameStatusFinal,
                gameStatus = GameStatusCode.FINAL,
                homeTeam = RemoteGameBoxScore.Game.Team(
                    teamId = HomeTeamId,
                    teamName = HomeTeamName,
                    teamCity = TeamCity,
                    teamTricode = HomeTeamAbbr,
                    score = BasicNumber,
                    inBonus = "1",
                    timeoutsRemaining = BasicNumber,
                    periods = listOf(
                        RemoteGameBoxScore.Game.Team.Period(
                            period = 1,
                            periodType = PeriodType.REGULAR,
                            score = BasicNumber
                        )
                    ),
                    players = listOf(
                        RemoteGameBoxScore.Game.Team.Player(
                            status = PlayerActiveStatus.ACTIVE,
                            notPlayingReason = "",
                            order = BasicNumber,
                            personId = HomePlayerId,
                            jerseyNum = BasicNumber.toString(),
                            position = BasicPosition,
                            starter = "1",
                            oncourt = "1",
                            played = "1",
                            statistics = RemoteGameBoxScore.Game.Team.Player.Statistics(
                                assists = BasicNumber,
                                blocks = BasicNumber,
                                blocksReceived = BasicNumber,
                                fieldGoalsAttempted = BasicNumber,
                                fieldGoalsMade = BasicNumber,
                                fieldGoalsPercentage = BasicPercentage,
                                foulsOffensive = BasicNumber,
                                foulsDrawn = BasicNumber,
                                foulsPersonal = BasicNumber,
                                foulsTechnical = BasicNumber,
                                freeThrowsAttempted = BasicNumber,
                                freeThrowsMade = BasicNumber,
                                freeThrowsPercentage = BasicPercentage,
                                minus = BasicNumber.toDouble(),
                                minutes = BasicMinutes,
                                plus = BasicNumber.toDouble(),
                                plusMinusPoints = BasicNumber.toDouble(),
                                points = BasicNumber,
                                reboundsOffensive = BasicNumber,
                                reboundsDefensive = BasicNumber,
                                reboundsTotal = BasicNumber,
                                steals = BasicNumber,
                                threePointersAttempted = BasicNumber,
                                threePointersMade = BasicNumber,
                                threePointersPercentage = BasicPercentage,
                                turnovers = BasicNumber,
                                twoPointersAttempted = BasicNumber,
                                twoPointersMade = BasicNumber,
                                twoPointersPercentage = BasicPercentage
                            ),
                            name = HomePlayerFullName,
                            nameI = HomePlayerFullName,
                            firstName = HomePlayerFirstName,
                            familyName = HomePlayerLastName
                        )
                    ),
                    statistics = RemoteGameBoxScore.Game.Team.Statistics(
                        assists = BasicNumber,
                        blocks = BasicNumber,
                        blocksReceived = BasicNumber,
                        fieldGoalsAttempted = BasicNumber,
                        fieldGoalsMade = BasicNumber,
                        fieldGoalsPercentage = BasicPercentage,
                        foulsOffensive = BasicNumber,
                        foulsDrawn = BasicNumber,
                        foulsPersonal = BasicNumber,
                        foulsTeam = BasicNumber,
                        foulsTechnical = BasicNumber,
                        freeThrowsAttempted = BasicNumber,
                        freeThrowsMade = BasicNumber,
                        freeThrowsPercentage = BasicPercentage,
                        points = BasicNumber,
                        reboundsDefensive = BasicNumber,
                        reboundsOffensive = BasicNumber,
                        reboundsPersonal = BasicNumber,
                        reboundsTotal = BasicNumber,
                        steals = BasicNumber,
                        threePointersAttempted = BasicNumber,
                        threePointersMade = BasicNumber,
                        threePointersPercentage = BasicPercentage,
                        turnovers = BasicNumber,
                        turnoversTeam = BasicNumber,
                        turnoversTotal = BasicNumber,
                        twoPointersAttempted = BasicNumber,
                        twoPointersMade = BasicNumber,
                        twoPointersPercentage = BasicPercentage,
                        pointsFastBreak = BasicNumber,
                        pointsFromTurnovers = BasicNumber,
                        pointsInThePaint = BasicNumber,
                        pointsSecondChance = BasicNumber,
                        benchPoints = BasicNumber
                    )
                ),
                awayTeam = RemoteGameBoxScore.Game.Team(
                    teamId = AwayTeamId,
                    teamName = AwayTeamName,
                    teamCity = TeamCity,
                    teamTricode = AwayTeamAbbr,
                    score = BasicNumber,
                    inBonus = "1",
                    timeoutsRemaining = BasicNumber,
                    periods = listOf(
                        RemoteGameBoxScore.Game.Team.Period(
                            period = 1,
                            periodType = PeriodType.REGULAR,
                            score = BasicNumber
                        )
                    ),
                    players = listOf(
                        RemoteGameBoxScore.Game.Team.Player(
                            status = PlayerActiveStatus.ACTIVE,
                            notPlayingReason = "",
                            order = BasicNumber,
                            personId = AwayPlayerId,
                            jerseyNum = BasicNumber.toString(),
                            position = BasicPosition,
                            starter = "1",
                            oncourt = "1",
                            played = "1",
                            statistics = RemoteGameBoxScore.Game.Team.Player.Statistics(
                                assists = BasicNumber,
                                blocks = BasicNumber,
                                blocksReceived = BasicNumber,
                                fieldGoalsAttempted = BasicNumber,
                                fieldGoalsMade = BasicNumber,
                                fieldGoalsPercentage = BasicPercentage,
                                foulsOffensive = BasicNumber,
                                foulsDrawn = BasicNumber,
                                foulsPersonal = BasicNumber,
                                foulsTechnical = BasicNumber,
                                freeThrowsAttempted = BasicNumber,
                                freeThrowsMade = BasicNumber,
                                freeThrowsPercentage = BasicPercentage,
                                minus = BasicNumber.toDouble(),
                                minutes = BasicMinutes,
                                plus = BasicNumber.toDouble(),
                                plusMinusPoints = BasicNumber.toDouble(),
                                points = BasicNumber,
                                reboundsOffensive = BasicNumber,
                                reboundsDefensive = BasicNumber,
                                reboundsTotal = BasicNumber,
                                steals = BasicNumber,
                                threePointersAttempted = BasicNumber,
                                threePointersMade = BasicNumber,
                                threePointersPercentage = BasicPercentage,
                                turnovers = BasicNumber,
                                twoPointersAttempted = BasicNumber,
                                twoPointersMade = BasicNumber,
                                twoPointersPercentage = BasicPercentage
                            ),
                            name = AwayPlayerFullName,
                            nameI = AwayPlayerFullName,
                            firstName = AwayPlayerFirstName,
                            familyName = AwayPlayerLastName
                        )
                    ),
                    statistics = RemoteGameBoxScore.Game.Team.Statistics(
                        assists = BasicNumber,
                        blocks = BasicNumber,
                        blocksReceived = BasicNumber,
                        fieldGoalsAttempted = BasicNumber,
                        fieldGoalsMade = BasicNumber,
                        fieldGoalsPercentage = BasicPercentage,
                        foulsOffensive = BasicNumber,
                        foulsDrawn = BasicNumber,
                        foulsPersonal = BasicNumber,
                        foulsTeam = BasicNumber,
                        foulsTechnical = BasicNumber,
                        freeThrowsAttempted = BasicNumber,
                        freeThrowsMade = BasicNumber,
                        freeThrowsPercentage = BasicPercentage,
                        points = BasicNumber,
                        reboundsDefensive = BasicNumber,
                        reboundsOffensive = BasicNumber,
                        reboundsPersonal = BasicNumber,
                        reboundsTotal = BasicNumber,
                        steals = BasicNumber,
                        threePointersAttempted = BasicNumber,
                        threePointersMade = BasicNumber,
                        threePointersPercentage = BasicPercentage,
                        turnovers = BasicNumber,
                        turnoversTeam = BasicNumber,
                        turnoversTotal = BasicNumber,
                        twoPointersAttempted = BasicNumber,
                        twoPointersMade = BasicNumber,
                        twoPointersPercentage = BasicPercentage,
                        pointsFastBreak = BasicNumber,
                        pointsFromTurnovers = BasicNumber,
                        pointsInThePaint = BasicNumber,
                        pointsSecondChance = BasicNumber,
                        benchPoints = BasicNumber
                    )
                )
            )
        )
    }
}
