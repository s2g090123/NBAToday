package com.jiachian.nbatoday.data.remote.score

import android.annotation.SuppressLint
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.game.PeriodType
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import java.text.SimpleDateFormat
import java.util.Calendar

data class RemoteGameBoxScore(
    val game: Game?
) {
    data class Game(
        val gameId: String?,
        val gameEt: String?, // e.g. 2022-12-15T20:00:00-05:00
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
            val periods: List<Period>?,
            val players: List<Player>?,
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
                        else -> "OT${period - 4}"
                    }
                    return GameBoxScore.BoxScoreTeam.Period(
                        period = period,
                        periodLabel = periodLabel,
                        score = score.getOrZero()
                    )
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
                    val blocksReceived: Int?,
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
                            assists = assists.getOrZero(),
                            blocks = blocks.getOrZero(),
                            blocksReceived = blocksReceived.getOrZero(),
                            fieldGoalsAttempted = fieldGoalsAttempted.getOrZero(),
                            fieldGoalsMade = fieldGoalsMade.getOrZero(),
                            fieldGoalsPercentage = fieldGoalsPercentage?.parsePercentage().getOrZero(),
                            foulsOffensive = foulsOffensive.getOrZero(),
                            foulsDrawn = foulsDrawn.getOrZero(),
                            foulsPersonal = foulsPersonal.getOrZero(),
                            foulsTechnical = foulsTechnical.getOrZero(),
                            freeThrowsAttempted = freeThrowsAttempted.getOrZero(),
                            freeThrowsMade = freeThrowsMade.getOrZero(),
                            freeThrowsPercentage = freeThrowsPercentage?.parsePercentage().getOrZero(),
                            minus = minus?.toInt().getOrZero(),
                            minutes = parseMinutes(),
                            plus = plus?.toInt().getOrZero(),
                            plusMinusPoints = plusMinusPoints?.toInt().getOrZero(),
                            points = points.getOrZero(),
                            reboundsDefensive = reboundsDefensive.getOrZero(),
                            reboundsOffensive = reboundsOffensive.getOrZero(),
                            reboundsTotal = reboundsTotal.getOrZero(),
                            steals = steals.getOrZero(),
                            threePointersAttempted = threePointersAttempted.getOrZero(),
                            threePointersMade = threePointersMade.getOrZero(),
                            threePointersPercentage = threePointersPercentage?.parsePercentage().getOrZero(),
                            turnovers = turnovers.getOrZero(),
                            twoPointersAttempted = twoPointersAttempted.getOrZero(),
                            twoPointersMade = twoPointersMade.getOrZero(),
                            twoPointersPercentage = twoPointersPercentage?.parsePercentage().getOrZero()
                        )
                    }

                    private fun parseMinutes(): String {
                        return minutes
                            ?.substringAfter("PT")
                            ?.substringBefore(".")
                            ?.replace("M", ":")
                            ?: "00:00"
                    }
                }

                fun toLocal(): GameBoxScore.BoxScoreTeam.Player? {
                    val notPlaying = when (notPlayingReason) {
                        "INACTIVE_INJURY" -> "Injury"
                        "INACTIVE_GLEAGUE_TWOWAY" -> "G League Two Way"
                        "INACTIVE_PERSONAL" -> "Personal"
                        "INACTIVE_COACH" -> "Coach's Decision"
                        "INACTIVE_NOT_WITH_TEAM" -> "Not With Team"
                        "INACTIVE_GLEAGUE_ON_ASSIGNMENT" -> "G League On Assignment"
                        "INACTIVE_HEALTH_AND_SAFETY_PROTOCOLS" -> "Health And Safety Protocols"
                        else -> notPlayingReason
                    }
                    return GameBoxScore.BoxScoreTeam.Player(
                        status = status ?: PlayerActiveStatus.INACTIVE,
                        notPlayingReason = notPlaying,
                        order = order.getOrZero(),
                        personId = personId ?: return null,
                        jerseyNum = jerseyNum.getOrNA(),
                        position = position.getOrNA(),
                        starter = isStarter(),
                        onCourt = isOnCourt(),
                        played = isPlayed(),
                        statistics = statistics?.toLocal(),
                        name = name.getOrNA(),
                        nameAbbr = nameI.getOrNA(),
                        firstName = firstName.getOrNA(),
                        familyName = familyName.getOrNA()
                    )
                }

                private fun isStarter(): Boolean {
                    return starter?.takeIf { it == "1" }?.let { true } ?: false
                }

                private fun isOnCourt(): Boolean {
                    return oncourt?.takeIf { it == "1" }?.let { true } ?: false
                }

                private fun isPlayed(): Boolean {
                    return played?.takeIf { it == "1" }?.let { true } ?: false
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
                val twoPointersPercentage: Double?,
                val pointsFastBreak: Int?,
                val pointsFromTurnovers: Int?,
                val pointsInThePaint: Int?,
                val pointsSecondChance: Int?,
                val benchPoints: Int?
            ) {
                fun toLocal(): GameBoxScore.BoxScoreTeam.Statistics {
                    return GameBoxScore.BoxScoreTeam.Statistics(
                        assists = assists.getOrZero(),
                        blocks = blocks.getOrZero(),
                        blocksReceived = blocksReceived.getOrZero(),
                        fieldGoalsAttempted = fieldGoalsAttempted.getOrZero(),
                        fieldGoalsMade = fieldGoalsMade.getOrZero(),
                        fieldGoalsPercentage = fieldGoalsPercentage?.parsePercentage().getOrZero(),
                        foulsOffensive = foulsOffensive.getOrZero(),
                        foulsDrawn = foulsDrawn.getOrZero(),
                        foulsPersonal = foulsPersonal.getOrZero(),
                        foulsTeam = foulsTeam.getOrZero(),
                        foulsTechnical = foulsTechnical.getOrZero(),
                        freeThrowsAttempted = freeThrowsAttempted.getOrZero(),
                        freeThrowsMade = freeThrowsMade.getOrZero(),
                        freeThrowsPercentage = freeThrowsPercentage?.parsePercentage().getOrZero(),
                        points = points.getOrZero(),
                        reboundsDefensive = reboundsDefensive.getOrZero(),
                        reboundsOffensive = reboundsOffensive.getOrZero(),
                        reboundsPersonal = reboundsPersonal.getOrZero(),
                        reboundsTotal = reboundsTotal.getOrZero(),
                        steals = steals.getOrZero(),
                        threePointersAttempted = threePointersAttempted.getOrZero(),
                        threePointersMade = threePointersMade.getOrZero(),
                        threePointersPercentage = threePointersPercentage?.parsePercentage().getOrZero(),
                        turnovers = turnovers.getOrZero(),
                        turnoversTeam = turnoversTeam.getOrZero(),
                        turnoversTotal = turnoversTotal.getOrZero(),
                        twoPointersAttempted = twoPointersAttempted.getOrZero(),
                        twoPointersMade = twoPointersMade.getOrZero(),
                        twoPointersPercentage = twoPointersPercentage?.parsePercentage().getOrZero(),
                        pointsFastBreak = pointsFastBreak.getOrZero(),
                        pointsFromTurnovers = pointsFromTurnovers.getOrZero(),
                        pointsInThePaint = pointsInThePaint.getOrZero(),
                        pointsSecondChance = pointsSecondChance.getOrZero(),
                        benchPoints = benchPoints.getOrZero()
                    )
                }
            }

            fun toLocal(): GameBoxScore.BoxScoreTeam {
                val team = teamId?.let { NBATeam.getTeamById(it) } ?: teamOfficial
                return GameBoxScore.BoxScoreTeam(
                    team = team,
                    score = score.getOrZero(),
                    inBonus = isInBonus(),
                    timeoutsRemaining = timeoutsRemaining.getOrZero(),
                    periods = periods?.map { it.toLocal() } ?: emptyList(),
                    players = players?.mapNotNull { it.toLocal() } ?: emptyList(),
                    statistics = statistics?.toLocal()
                )
            }

            private fun isInBonus(): Boolean {
                return inBonus?.takeIf { it == "1" }?.let { true } ?: false
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun toLocal(): GameBoxScore? {
            return GameBoxScore(
                gameId = gameId ?: return null,
                gameDate = getGameDate(),
                gameCode = gameCode.getOrNA(),
                gameStatusText = gameStatusText.getOrNA(),
                gameStatus = gameStatus ?: GameStatusCode.COMING_SOON,
                homeTeam = homeTeam?.toLocal(),
                awayTeam = awayTeam?.toLocal()
            )
        }

        @SuppressLint("SimpleDateFormat")
        private fun getGameDate(): String {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            return gameEt?.substringBeforeLast("-")?.let {
                try {
                    dateFormat.parse(it)?.let { date ->
                        val cal = NbaUtils.getCalendar()
                        cal.time = date
                        NbaUtils.formatScoreboardGameDate(
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH) + 1,
                            cal.get(Calendar.DAY_OF_MONTH)
                        )
                    }
                } catch (e: Exception) {
                    null
                }
            }.getOrNA()
        }
    }
}

private fun Double.parsePercentage(): Double = times(1000).toInt().div(10.0)
