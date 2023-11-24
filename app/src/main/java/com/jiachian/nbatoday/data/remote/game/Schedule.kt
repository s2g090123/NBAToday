package com.jiachian.nbatoday.data.remote.game

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.teamOfficial
import com.jiachian.nbatoday.data.remote.team.GameTeam
import com.jiachian.nbatoday.utils.getOrError
import com.jiachian.nbatoday.utils.getOrNA
import com.jiachian.nbatoday.utils.getOrZero
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

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
                val awayTeam: Team?,
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
                val homeTeam: Team?,
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
                data class Team(
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
                    fun toGameTeam(): GameTeam {
                        val team = teamId?.let { NBATeam.getTeamById(it) } ?: teamOfficial
                        val losses = losses.getOrZero()
                        val score = score.getOrZero()
                        val wins = wins.getOrZero()
                        return GameTeam(
                            team = team,
                            losses = losses,
                            score = score,
                            wins = wins,
                            periods = emptyList()
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
                        if (personId == null || teamId == null) return null
                        val firstName = firstName.getOrNA()
                        val lastName = lastName.getOrNA()
                        val points = points.getOrZero()
                        val teamName = teamName.getOrNA()
                        val teamTricode = teamTricode.getOrNA()
                        return NbaGame.NbaPointsLeader(
                            firstName = firstName,
                            lastName = lastName,
                            personId = personId,
                            points = points,
                            teamId = teamId,
                            teamName = teamName,
                            teamTricode = teamTricode
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
            gameDates?.forEach { gameDate ->
                gameDate?.games?.mapNotNull { game ->
                    game?.let {
                        createNbaGame(it, leagueId, dateFormat)
                    }
                }?.also { nbaGames ->
                    games.addAll(nbaGames)
                }
            }
            return games
        }

        private fun createNbaGame(
            game: GameDate.Game,
            leagueId: String,
            dateFormat: SimpleDateFormat
        ): NbaGame? {
            val gameDate = parseDate(game.gameDateEst, dateFormat)
            val gameDateTime = parseDate(game.gameDateTimeEst, dateFormat)
            val day = game.day
            val isDateValid = day != null && gameDate != null && gameDateTime != null

            val awayTeam = game.awayTeam?.toGameTeam()
            val homeTeam = game.homeTeam?.toGameTeam()
            val isTeamValid = homeTeam != null && awayTeam != null

            val gameId = game.gameId
            val gameStatus = game.gameStatus
            val isLeaderValid = game.pointsLeaders != null
            val isGameValid = gameId != null && gameStatus != null && isLeaderValid

            if (!isDateValid || !isTeamValid || !isGameValid) return null

            val leaders = game.pointsLeaders
                .getOrError()
                .mapNotNull { leader ->
                    leader?.toNbaPointsLeader()
                }

            val gameCode = game.gameCode.getOrNA()
            val gameStatusText = game.gameStatusText.getOrNA()
            val gameSequence = game.gameSequence.getOrZero()

            val monthNum = game.monthNum.getOrZero()
            val weekNumber = game.weekNumber.getOrZero()

            return NbaGame(
                leagueId = leagueId,
                homeTeamId = homeTeam.getOrError().team.teamId,
                awayTeamId = awayTeam.getOrError().team.teamId,
                awayTeam = awayTeam.getOrError(),
                day = day.getOrError(),
                gameCode = gameCode,
                gameId = gameId.getOrError(),
                gameStatus = gameStatus.getOrError(),
                gameStatusText = gameStatusText,
                gameSequence = gameSequence,
                homeTeam = homeTeam.getOrError(),
                gameDate = gameDate.getOrError(),
                gameDateTime = gameDateTime.getOrError(),
                monthNum = monthNum,
                pointsLeaders = leaders,
                weekNumber = weekNumber,
                gameLeaders = null,
                teamLeaders = null
            )
        }

        private fun parseDate(dateString: String?, dateFormat: SimpleDateFormat): Date? {
            return dateString?.let { time ->
                try {
                    dateFormat.parse(time)
                } catch (e: ParseException) {
                    null
                }
            }
        }
    }
}
