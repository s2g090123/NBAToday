package com.jiachian.nbatoday.data.remote.score

import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.game.PeriodType

data class RemoteGameBoxScore(
    val game: Game?
) {
    data class Game(
        val gameId: String?,
        val gameEt: String?,
        val gameCode: String?,
        val gameStatusText: String?,
        val gameStatus: GameStatusCode?,
        val homeTeam: Team?,
        val awayTeam: Team?
    ) {
        data class Team(
            val teamId: Int?,
            val teamName: String?,
            val teamCity: String?,
            val teamTricode: String?,
            val score: Int?,
            val inBonus: String?,
            val timeoutsRemaining: Int?,
            val periods: List<Period?>?,
            val players: List<Player?>?,
            val statistics: Statistics?
        ) {
            data class Period(
                val period: Int?,
                val periodType: PeriodType?,
                val score: Int?
            ) {
                fun toLocal(): GameBoxScore.BoxScoreTeam.Period {
                    val period = period ?: 0
                    val periodLabel = when {
                        period <= 0 -> ""
                        period == 1 -> "1st"
                        period == 2 -> "2nd"
                        period == 3 -> "3rd"
                        period == 4 -> "4th"
                        else -> "OT${period - 1}"
                    }
                    return GameBoxScore.BoxScoreTeam.Period(period, periodLabel, score ?: 0)
                }
            }

            data class Player(
                val status: PlayerActiveStatus?,
                val notPlayingReason: String?,
                val order: Int?,
                val personId: Int?,
                val jerseyNum: String?,
                val position: String?,
                val starter: String?,
                val oncourt: String?,
                val played: String?,
                val statistics: Statistics?,
                val name: String?,
                val nameI: String?,
                val firstName: String?,
                val familyName: String?
            ) {
                data class Statistics(
                    val assists: Int?,
                    val blocks: Int?,
                    val fieldGoalsAttempted: Int?,
                    val fieldGoalsMade: Int?,
                    val fieldGoalsPercentage: Double?,
                    val foulsOffensive: Int?,
                    val foulsDrawn: Int?,
                    val foulsPersonal: Int?,
                    val foulsTechnical: Int?,
                    val freeThrowsAttempted: Int?,
                    val freeThrowsMade: Int?,
                    val freeThrowsPercentage: Double?,
                    val minus: Double?,
                    val minutes: String?,
                    val plus: Double?,
                    val plusMinusPoints: Double?,
                    val points: Int?,
                    val reboundsDefensive: Int?,
                    val reboundsOffensive: Int?,
                    val reboundsTotal: Int?,
                    val steals: Int?,
                    val threePointersAttempted: Int?,
                    val threePointersMade: Int?,
                    val threePointersPercentage: Double?,
                    val turnovers: Int?,
                    val twoPointersAttempted: Int?,
                    val twoPointersMade: Int?,
                    val twoPointersPercentage: Double?
                ) {
                    fun toLocal(): GameBoxScore.BoxScoreTeam.Player.Statistics {
                        return GameBoxScore.BoxScoreTeam.Player.Statistics(
                            assists ?: 0,
                            blocks ?: 0,
                            fieldGoalsAttempted ?: 0,
                            fieldGoalsMade ?: 0,
                            fieldGoalsPercentage?.times(1000)?.toInt()?.div(10.0) ?: 0.0,
                            foulsOffensive ?: 0,
                            foulsDrawn ?: 0,
                            foulsPersonal ?: 0,
                            foulsTechnical ?: 0,
                            freeThrowsAttempted ?: 0,
                            freeThrowsMade ?: 0,
                            freeThrowsPercentage?.times(1000)?.toInt()?.div(10.0) ?: 0.0,
                            minus ?: 0.0,
                            minutes?.substringAfter("PT")?.substringBefore(".")?.replace("M", ":")
                                ?: "00:00",
                            plus ?: 0.0,
                            plusMinusPoints ?: 0.0,
                            points ?: 0,
                            reboundsDefensive ?: 0,
                            reboundsOffensive ?: 0,
                            reboundsTotal ?: 0,
                            steals ?: 0,
                            threePointersAttempted ?: 0,
                            threePointersMade ?: 0,
                            threePointersPercentage?.times(1000)?.toInt()?.div(10.0) ?: 0.0,
                            turnovers ?: 0,
                            twoPointersAttempted ?: 0,
                            twoPointersMade ?: 0,
                            twoPointersPercentage?.times(1000)?.toInt()?.div(10.0) ?: 0.0
                        )
                    }
                }

                fun toLocal(): GameBoxScore.BoxScoreTeam.Player {
                    return GameBoxScore.BoxScoreTeam.Player(
                        status ?: PlayerActiveStatus.INACTIVE,
                        notPlayingReason,
                        order ?: 0,
                        personId ?: 0,
                        jerseyNum ?: "0",
                        position ?: "",
                        starter?.takeIf { it == "1" }?.let { true } ?: false,
                        oncourt?.takeIf { it == "1" }?.let { true } ?: false,
                        played?.takeIf { it == "1" }?.let { true } ?: false,
                        statistics?.toLocal(),
                        name ?: "",
                        nameI ?: "",
                        firstName ?: "",
                        familyName ?: ""
                    )
                }
            }

            data class Statistics(
                val assists: Int?,
                val blocks: Int?,
                val blocksReceived: Int?,
                val fieldGoalsAttempted: Int?,
                val fieldGoalsMade: Int?,
                val fieldGoalsPercentage: Double?,
                val foulsOffensive: Int?,
                val foulsDrawn: Int?,
                val foulsPersonal: Int?,
                val foulsTeam: Int?,
                val foulsTechnical: Int?,
                val freeThrowsAttempted: Int?,
                val freeThrowsMade: Int?,
                val freeThrowsPercentage: Double?,
                val points: Int?,
                val reboundsDefensive: Int?,
                val reboundsOffensive: Int?,
                val reboundsPersonal: Int?,
                val reboundsTotal: Int?,
                val steals: Int?,
                val threePointersAttempted: Int?,
                val threePointersMade: Int?,
                val threePointersPercentage: Double?,
                val turnovers: Int?,
                val turnoversTeam: Int?,
                val turnoversTotal: Int?,
                val twoPointersAttempted: Int?,
                val twoPointersMade: Int?,
                val twoPointersPercentage: Double?
            ) {
                fun toLocal(): GameBoxScore.BoxScoreTeam.Statistics {
                    return GameBoxScore.BoxScoreTeam.Statistics(
                        assists ?: 0,
                        blocks ?: 0,
                        blocksReceived ?: 0,
                        fieldGoalsAttempted ?: 0,
                        fieldGoalsMade ?: 0,
                        fieldGoalsPercentage?.times(1000)?.toInt()?.div(10.0) ?: 0.0,
                        foulsOffensive ?: 0,
                        foulsDrawn ?: 0,
                        foulsPersonal ?: 0,
                        foulsTeam ?: 0,
                        foulsTechnical ?: 0,
                        freeThrowsAttempted ?: 0,
                        freeThrowsMade ?: 0,
                        freeThrowsPercentage?.times(1000)?.toInt()?.div(10.0) ?: 0.0,
                        points ?: 0,
                        reboundsDefensive ?: 0,
                        reboundsOffensive ?: 0,
                        reboundsPersonal ?: 0,
                        reboundsTotal ?: 0,
                        steals ?: 0,
                        threePointersAttempted ?: 0,
                        threePointersMade ?: 0,
                        threePointersPercentage?.times(1000)?.toInt()?.div(10.0) ?: 0.0,
                        turnovers ?: 0,
                        turnoversTeam ?: 0,
                        turnoversTotal ?: 0,
                        twoPointersAttempted ?: 0,
                        twoPointersMade ?: 0,
                        twoPointersPercentage?.times(1000)?.toInt()?.div(10.0) ?: 0.0
                    )
                }
            }

            fun toLocal(): GameBoxScore.BoxScoreTeam {
                return GameBoxScore.BoxScoreTeam(
                    teamId ?: 0,
                    teamName ?: "",
                    teamCity ?: "",
                    teamTricode ?: "",
                    score ?: 0,
                    inBonus?.takeIf { it == "1" }?.let { true } ?: false,
                    timeoutsRemaining ?: 0,
                    periods?.mapNotNull { it?.toLocal() } ?: emptyList(),
                    players?.mapNotNull { it?.toLocal() } ?: emptyList(),
                    statistics?.toLocal()
                )
            }
        }

        fun toLocal(): GameBoxScore {
            return GameBoxScore(
                gameId ?: "",
                gameEt?.substringBefore("T") ?: "",
                gameCode ?: "",
                gameStatusText ?: "",
                gameStatus ?: GameStatusCode.COMING_SOON,
                homeTeam?.toLocal(),
                awayTeam?.toLocal()
            )
        }
    }
}