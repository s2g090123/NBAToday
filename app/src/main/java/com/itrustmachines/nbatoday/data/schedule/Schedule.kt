package com.itrustmachines.nbatoday.data.schedule


import com.google.gson.annotations.SerializedName

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
                @SerializedName("gameStatusText")
                val gameStatusText: String?, // 比賽狀態(Final或開始時間), e.g. Final or 3:00 pm ET
                @SerializedName("gameSequence")
                val gameSequence: Int?, // 今天的第幾場比賽(起始為1), e.g. 1
                @SerializedName("homeTeam")
                val homeTeam: HomeTeam?,
                @SerializedName("homeTeamTime")
                val homeTeamTime: String?, // 比賽開始時間, e.g. 2022-10-30T12:00:00Z
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
                )

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
                )

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
                )
            }
        }
    }
}