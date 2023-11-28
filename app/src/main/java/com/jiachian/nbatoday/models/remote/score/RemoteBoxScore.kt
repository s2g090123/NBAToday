package com.jiachian.nbatoday.models.remote.score

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.models.local.score.PlayerActiveStatus
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.getOrNA
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

data class RemoteBoxScore(
    val game: RemoteGame?
) {
    data class RemoteGame(
        val gameId: String?,
        val gameEt: String?, // e.g. 2022-12-15T20:00:00-05:00
        val gameStatusText: String?,
        val gameStatus: GameStatus?,
        val homeTeam: RemoteTeam?,
        val awayTeam: RemoteTeam?
    ) {
        data class RemoteTeam(
            val teamId: Int?,
            val score: Int?,
            val periods: List<RemotePeriod>?,
            val players: List<RemotePlayer>?,
            val statistics: RemoteStatistics?
        ) {
            data class RemotePeriod(
                val period: Int?,
                val score: Int?
            )

            data class RemotePlayer(
                val status: PlayerActiveStatus?,
                val notPlayingReason: String?,
                @SerializedName("personId") val playerId: Int?,
                val position: String?,
                val starter: String?,
                val statistics: RemoteStatistics?,
                @SerializedName("nameI") val nameAbbr: String?,
            ) {
                data class RemoteStatistics(
                    val assists: Int?,
                    val blocks: Int?,
                    val blocksReceived: Int?,
                    val fieldGoalsAttempted: Int?,
                    val fieldGoalsMade: Int?,
                    val fieldGoalsPercentage: Double?,
                    val foulsOffensive: Int?,
                    val foulsPersonal: Int?,
                    val foulsTechnical: Int?,
                    val freeThrowsAttempted: Int?,
                    val freeThrowsMade: Int?,
                    val freeThrowsPercentage: Double?,
                    val minutes: String?,
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
                    fun getFormattedMinutes(): String {
                        return minutes
                            ?.substringAfter("PT")
                            ?.substringBefore(".")
                            ?.replace("M", ":")
                            ?: "00:00"
                    }
                }

                fun isStarter(): Boolean {
                    return starter?.takeIf { it == "1" }?.let { true } ?: false
                }

                fun getFormattedNotPlayingReason(): String {
                    return when (notPlayingReason) {
                        "INACTIVE_INJURY" -> "Injury"
                        "INACTIVE_GLEAGUE_TWOWAY" -> "G League Two Way"
                        "INACTIVE_PERSONAL" -> "Personal"
                        "INACTIVE_COACH" -> "Coach's Decision"
                        "INACTIVE_NOT_WITH_TEAM" -> "Not With Team"
                        "INACTIVE_GLEAGUE_ON_ASSIGNMENT" -> "G League On Assignment"
                        "INACTIVE_HEALTH_AND_SAFETY_PROTOCOLS" -> "Health And Safety Protocols"
                        else -> notPlayingReason
                    } ?: ""
                }
            }

            data class RemoteStatistics(
                val assists: Int?,
                val blocks: Int?,
                val fieldGoalsAttempted: Int?,
                val fieldGoalsMade: Int?,
                val fieldGoalsPercentage: Double?,
                val foulsPersonal: Int?,
                val foulsTechnical: Int?,
                val freeThrowsAttempted: Int?,
                val freeThrowsMade: Int?,
                val freeThrowsPercentage: Double?,
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
                val twoPointersPercentage: Double?,
                val pointsFastBreak: Int?,
                val pointsFromTurnovers: Int?,
                val pointsInThePaint: Int?,
                val pointsSecondChance: Int?,
                val benchPoints: Int?
            )
        }

        @SuppressLint("SimpleDateFormat")
        fun getFormattedGameDate(): String {
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
                } catch (e: ParseException) {
                    null
                }
            }.getOrNA()
        }
    }
}
