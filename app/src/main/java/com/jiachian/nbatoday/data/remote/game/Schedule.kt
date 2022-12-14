package com.jiachian.nbatoday.data.remote.game

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.team.GameTeam
import java.text.SimpleDateFormat
import java.util.*

data class Schedule(
    @SerializedName("leagueSchedule") val leagueSchedule: LeagueSchedule?
) {
    data class LeagueSchedule(
        @SerializedName("gameDates")
        val gameDates: List<GameDate?>?,
        @SerializedName("leagueId")
        val leagueId: String?, // 聯盟id, e.g. 00
        @SerializedName("seasonYear")
        val seasonYear: String?, // 賽季年份, e.g. 2022-23
    ) {

        data class GameDate(
            @SerializedName("gameDate")
            val gameDate: String?, // 比賽日期，時間都12:00, e.g. 10/30/2022 12:00:00 AM
            @SerializedName("games")
            val games: List<Game?>?
        ) {
            data class Game(
                @SerializedName("awayTeam")
                val awayTeam: AwayTeam?,
                @SerializedName("day")
                val day: String?, // 星期幾(縮寫), e.g. Sun
                @SerializedName("gameCode")
                val gameCode: String?, // 日期與對戰隊伍, e.g. 20221030/WASBOS
                @SerializedName("gameId")
                val gameId: String?, // 比賽id, e.g. 0022200089
                @SerializedName("gameStatus")
                val gameStatus: GameStatusCode?,
                @SerializedName("gameStatusText")
                val gameStatusText: String?, // 比賽狀態(Final或開始時間), e.g. Final or 3:00 pm ET
                @SerializedName("gameSequence")
                val gameSequence: Int?, // 今天的第幾場比賽(起始為1), e.g. 1
                @SerializedName("homeTeam")
                val homeTeam: HomeTeam?,
                @ColumnInfo(name = "gameDateEst")
                val gameDateEst: String?, // 比賽開始日期(固定為00:00), e.g. 2022-11-20T00:00:00Z
                @SerializedName("gameDateTimeEst")
                val gameDateTimeEst: String?, // 比賽開始時間, e.g. 2022-10-30T12:00:00Z
                @SerializedName("monthNum")
                val monthNum: Int?, // 月份, e.g. 10
                @SerializedName("pointsLeaders")
                val pointsLeaders: List<PointsLeader?>?,
                @SerializedName("weekNumber")
                val weekNumber: Int? // 系列賽第幾週, e.g. 3
            ) {
                data class AwayTeam(
                    @SerializedName("losses")
                    val losses: Int?, // 敗場場次(從這場之前), e.g. 2
                    @SerializedName("score")
                    val score: Int?, // 比分, e.g. 100
                    @SerializedName("teamCity")
                    val teamCity: String?, // 城市名, e.g. LA
                    @SerializedName("teamId")
                    val teamId: Int?, // 隊伍id, e.g. 1610612746
                    @SerializedName("teamName")
                    val teamName: String?, // 隊伍名稱, e.g. Clippers
                    @SerializedName("teamTricode")
                    val teamTricode: String?, // 隊伍縮寫名稱, e.g. LAC
                    @SerializedName("wins")
                    val wins: Int? // 勝場場次(從這場之前), e.g. 2
                ) {
                    fun toNbaAwayTeam(): GameTeam? {
                        val losses = losses ?: return null
                        val score = score ?: return null
                        val teamCity = teamCity ?: return null
                        val teamId = teamId ?: return null
                        val teamName = teamName ?: return null
                        val teamTricode = teamTricode ?: return null
                        val wins = wins ?: return null
                        return GameTeam(
                            losses,
                            score,
                            teamCity,
                            teamId,
                            teamName,
                            teamTricode,
                            wins,
                            emptyList()
                        )
                    }
                }

                data class HomeTeam(
                    @SerializedName("losses")
                    val losses: Int?,
                    @SerializedName("score")
                    val score: Int?,
                    @SerializedName("teamCity")
                    val teamCity: String?,
                    @SerializedName("teamId")
                    val teamId: Int?,
                    @SerializedName("teamName")
                    val teamName: String?,
                    @SerializedName("teamTricode")
                    val teamTricode: String?,
                    @SerializedName("wins")
                    val wins: Int?
                ) {
                    fun toNbaHomeTeam(): GameTeam? {
                        val losses = losses ?: return null
                        val score = score ?: return null
                        val teamCity = teamCity ?: return null
                        val teamId = teamId ?: return null
                        val teamName = teamName ?: return null
                        val teamTricode = teamTricode ?: return null
                        val wins = wins ?: return null
                        return GameTeam(
                            losses,
                            score,
                            teamCity,
                            teamId,
                            teamName,
                            teamTricode,
                            wins,
                            emptyList()
                        )
                    }
                }

                data class PointsLeader(
                    @SerializedName("firstName")
                    val firstName: String?, // 球員名稱(姓), e.g. Shai
                    @SerializedName("lastName")
                    val lastName: String?, // 球員名稱(名), e.g. Gilgeous-Alexander
                    @SerializedName("personId")
                    val personId: Int?, // 球員id, e.g. 1628983
                    @SerializedName("points")
                    val points: Double?, // 球員當場得分, e.g. 38.0
                    @SerializedName("teamId")
                    val teamId: Int?, // 球員所屬球隊id, e.g. 1610612760
                    @SerializedName("teamName")
                    val teamName: String?, // 球員所屬球隊名稱, e.g. Thunder
                    @SerializedName("teamTricode")
                    val teamTricode: String? // 球員所屬球隊縮寫名稱, e.g. OKC
                ) {
                    fun toNbaPointsLeader(): NbaGame.NbaPointsLeader? {
                        val firstName = firstName ?: return null
                        val lastName = lastName ?: return null
                        val personId = personId ?: return null
                        val points = points ?: return null
                        val teamId = teamId ?: return null
                        val teamName = teamName ?: return null
                        val teamTricode = teamTricode ?: return null
                        return NbaGame.NbaPointsLeader(
                            firstName, lastName, personId, points, teamId, teamName, teamTricode
                        )
                    }
                }
            }
        }

        @SuppressLint("SimpleDateFormat")
        fun toNbaGames(): List<NbaGame> {
            val leagueId = leagueId ?: return emptyList()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
                timeZone = TimeZone.getTimeZone("EST")
            }
            val games = mutableListOf<NbaGame>()
            gameDates?.forEach {
                it?.games?.mapNotNull { game ->
                    game ?: return@mapNotNull null
                    val awayTeam = game.awayTeam?.toNbaAwayTeam() ?: return@mapNotNull null
                    val homeTeam = game.homeTeam?.toNbaHomeTeam() ?: return@mapNotNull null
                    val pointsLeaders = game.pointsLeaders?.mapNotNull { leader ->
                        leader?.toNbaPointsLeader()
                    } ?: return@mapNotNull null
                    val day = game.day ?: return@mapNotNull null
                    val gameCode = game.gameCode ?: return@mapNotNull null
                    val gameId = game.gameId ?: return@mapNotNull null
                    val gameStatus = game.gameStatus ?: return@mapNotNull null
                    val gameStatusText = game.gameStatusText ?: return@mapNotNull null
                    val gameSequence = game.gameSequence ?: return@mapNotNull null
                    val gameDate = game.gameDateEst?.let { time ->
                        try {
                            dateFormat.parse(time)
                        } catch (e: Exception) {
                            null
                        }
                    } ?: return@mapNotNull null
                    val gameDateTime = game.gameDateTimeEst?.let { time ->
                        try {
                            dateFormat.parse(time)
                        } catch (e: Exception) {
                            null
                        }
                    } ?: return@mapNotNull null
                    val monthNum = game.monthNum ?: return@mapNotNull null
                    val weekNumber = game.weekNumber ?: return@mapNotNull null
                    NbaGame(
                        leagueId,
                        awayTeam,
                        day,
                        gameCode,
                        gameId,
                        gameStatus,
                        gameStatusText,
                        gameSequence,
                        homeTeam,
                        gameDate,
                        gameDateTime,
                        monthNum,
                        pointsLeaders,
                        weekNumber,
                        null,
                        null
                    )
                }?.also { nbaGames ->
                    games.addAll(nbaGames)
                }
            }
            return games
        }
    }
}