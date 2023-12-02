package com.jiachian.nbatoday.models.local.score

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.models.local.team.NBATeam

@Entity(tableName = "score")
data class BoxScore(
    @PrimaryKey @ColumnInfo(name = "game_id")
    val gameId: String, // e.g. 0022200414
    @ColumnInfo(name = "game_date")
    val gameDate: String, // e.g. 2022-12-15
    @ColumnInfo(name = "game_status_text")
    val gameStatusText: String, // e.g. Final
    @ColumnInfo(name = "game_status")
    val gameStatus: GameStatus,
    @ColumnInfo(name = "home_team")
    val homeTeam: BoxScoreTeam,
    @ColumnInfo(name = "away_team")
    val awayTeam: BoxScoreTeam
) {

    val statusText: String
        get() {
            return when {
                gameStatus != GameStatus.COMING_SOON -> gameStatusText
                else -> gameStatusText.replace(" ", "\n")
            }.trim()
        }

    data class BoxScoreTeam(
        @ColumnInfo(name = "team")
        val team: NBATeam,
        @ColumnInfo(name = "score")
        val score: Int, // e.g. 134
        @ColumnInfo(name = "periods")
        val periods: List<Period>,
        @ColumnInfo(name = "players")
        val players: List<Player>,
        @ColumnInfo(name = "statistics")
        val statistics: Statistics?
    ) {
        fun getMostPointsPlayer(): Player? {
            return players.maxByOrNull { it.statistics.points }
        }

        data class Period(
            @ColumnInfo(name = "period_label")
            val periodLabel: String, // 第幾節標籤, e.g. 1st or OT1
            @ColumnInfo(name = "score")
            val score: Int // e.g. 33
        )

        data class Player(
            @ColumnInfo(name = "status")
            val status: PlayerActiveStatus,
            @ColumnInfo(name = "not_playing_reason")
            val notPlayingReason: String, // status為INACTIVE時才有值, e.g. INACTIVE_INJURY
            @ColumnInfo(name = "player_id")
            val playerId: Int, // e.g. 1626162
            @ColumnInfo(name = "position")
            val position: String, // e.g. SF
            @ColumnInfo(name = "starter")
            val starter: Boolean, // 是否為先發
            @ColumnInfo(name = "statistics")
            val statistics: Statistics,
            @ColumnInfo(name = "name_abbr")
            val nameAbbr: String, // e.g. K. Oubre Jr.
        ) {
            data class Statistics(
                @ColumnInfo(name = "assists")
                val assists: Int, // 助攻數
                @ColumnInfo(name = "blocks")
                val blocks: Int, // 阻攻數,
                @ColumnInfo(name = "blocks_received")
                val blocksReceived: Int, // 被阻攻數
                @ColumnInfo(name = "field_goals_attempted")
                val fieldGoalsAttempted: Int, // 出手次數
                @ColumnInfo(name = "field_goals_made")
                val fieldGoalsMade: Int, // 進球數
                @ColumnInfo(name = "field_goals_percentage")
                val fieldGoalsPercentage: Double, // 41.2
                @ColumnInfo(name = "fouls_offensive")
                val foulsOffensive: Int, // 進攻犯規數
                @ColumnInfo(name = "fouls_personal")
                val foulsPersonal: Int, // 防守犯規數
                @ColumnInfo(name = "fouls_technical")
                val foulsTechnical: Int, // 技術犯規數
                @ColumnInfo(name = "free_throws_attempted")
                val freeThrowsAttempted: Int, // 罰球出手次數
                @ColumnInfo(name = "free_throws_made")
                val freeThrowsMade: Int, // 罰球進球數
                @ColumnInfo(name = "free_throws_percentage")
                val freeThrowsPercentage: Double, // 66.6
                @ColumnInfo(name = "minutes")
                val minutes: String, // 上場總時間, e.g. 36:12 (分：秒)
                @ColumnInfo(name = "plus_minus_points")
                val plusMinusPoints: Int, // 加減評分
                @ColumnInfo(name = "points")
                val points: Int, // 得分數
                @ColumnInfo(name = "rebounds_defensive")
                val reboundsDefensive: Int, // 防守籃板數
                @ColumnInfo(name = "rebounds_offensive")
                val reboundsOffensive: Int, // 進攻籃板數
                @ColumnInfo(name = "rebounds_total")
                val reboundsTotal: Int, // 總籃板數
                @ColumnInfo(name = "steals")
                val steals: Int, // 抄截數
                @ColumnInfo(name = "three_pointers_attempted")
                val threePointersAttempted: Int, // 三分出手次數
                @ColumnInfo(name = "three_pointers_made")
                val threePointersMade: Int, // 三分進球次數
                @ColumnInfo(name = "three_pointers_percentage")
                val threePointersPercentage: Double, // 66.6
                @ColumnInfo(name = "turnovers")
                val turnovers: Int, // 失誤次數
                @ColumnInfo(name = "two_pointers_attempted")
                val twoPointersAttempted: Int, // 兩分出手數
                @ColumnInfo(name = "two_pointers_made")
                val twoPointersMade: Int, // 兩分進球數
                @ColumnInfo(name = "two_pointers_percentage")
                val twoPointersPercentage: Double // 66.6
            ) {
                private val indicator
                    get() = points + reboundsTotal + assists + steals + blocks
                private val fieldGoalScore
                    get() = fieldGoalsAttempted - fieldGoalsMade
                private val freeThrowScore
                    get() = freeThrowsAttempted - freeThrowsMade
                val efficiency
                    get() = indicator - (fieldGoalScore + freeThrowScore + turnovers)

                val fieldGoalProportion
                    get() = "$fieldGoalsMade-$fieldGoalsAttempted"
                val threePointProportion
                    get() = "$threePointersMade-$threePointersAttempted"
                val freeThrowProportion
                    get() = "$freeThrowsMade-$freeThrowsAttempted"

                val fieldGoalsFormat: String
                    get() = "$fieldGoalsMade/$fieldGoalsAttempted($fieldGoalsPercentage%)"

                val twoPointsFormat: String
                    get() = "$twoPointersMade/$twoPointersAttempted($twoPointersPercentage%)"

                val threePointsFormat: String
                    get() = "$threePointersMade/$threePointersAttempted($threePointersPercentage%)"

                val freeThrowFormat: String
                    get() = "$freeThrowsMade/$freeThrowsAttempted($freeThrowsPercentage%)"
            }
        }

        data class Statistics(
            @ColumnInfo(name = "assists")
            val assists: Int, // 球隊總助攻數
            @ColumnInfo(name = "blocks")
            val blocks: Int, // 球隊總阻攻數
            @ColumnInfo(name = "field_goals_attempted")
            val fieldGoalsAttempted: Int, // 球隊出手次數
            @ColumnInfo(name = "field_goals_made")
            val fieldGoalsMade: Int, // 球隊進球次數
            @ColumnInfo(name = "field_goals_percentage")
            val fieldGoalsPercentage: Double, // 66.6
            @ColumnInfo(name = "fouls_personal")
            val foulsPersonal: Int, // 球隊防守犯規總數
            @ColumnInfo(name = "fouls_technical")
            val foulsTechnical: Int, // 球隊技術犯規數
            @ColumnInfo(name = "free_throws_attempted")
            val freeThrowsAttempted: Int, // 球隊罰球出手次數
            @ColumnInfo(name = "free_throws_made")
            val freeThrowsMade: Int, // 球隊罰球進球次數
            @ColumnInfo(name = "free_throws_percentage")
            val freeThrowsPercentage: Double, // 66.6
            @ColumnInfo(name = "points")
            val points: Int, // 球隊得分
            @ColumnInfo(name = "rebounds_defensive")
            val reboundsDefensive: Int, // 球隊防守籃板總數
            @ColumnInfo(name = "rebounds_offensive")
            val reboundsOffensive: Int, // 球隊進攻籃板總數
            @ColumnInfo(name = "rebounds_total")
            val reboundsTotal: Int, // 球隊籃板總數 (total)
            @ColumnInfo(name = "steals")
            val steals: Int, // 球隊抄截總數
            @ColumnInfo(name = "three_pointers_attempted")
            val threePointersAttempted: Int, // 球隊三分出手次數
            @ColumnInfo(name = "three_pointers_made")
            val threePointersMade: Int, // 球隊三分進球次數
            @ColumnInfo(name = "three_pointers_percentage")
            val threePointersPercentage: Double, // 66.6
            @ColumnInfo(name = "turnovers")
            val turnovers: Int, // 個人失誤總數
            @ColumnInfo(name = "two_pointers_attempted")
            val twoPointersAttempted: Int, // 球隊兩分出手總數
            @ColumnInfo(name = "two_pointers_made")
            val twoPointersMade: Int, // 球隊兩分進球總數
            @ColumnInfo(name = "two_pointers_percentage")
            val twoPointersPercentage: Double, // 66.6
            @ColumnInfo(name = "points_fast_break")
            val pointsFastBreak: Int, // 快攻得分, e.g. 10
            @ColumnInfo(name = "points_from_turnovers")
            val pointsFromTurnovers: Int, // 因對方失誤得分, e.g 10
            @ColumnInfo(name = "points_in_the_paint")
            val pointsInThePaint: Int, // 禁區得分, e.g. 10
            @ColumnInfo(name = "points_second_chance")
            val pointsSecondChance: Int, // 二次進攻得分, e.g. 10
            @ColumnInfo(name = "bench_points")
            val benchPoints: Int, // 板凳得分, e.g. 10
        ) {
            val fieldGoalsFormat: String
                get() = "$fieldGoalsMade/$fieldGoalsAttempted($fieldGoalsPercentage%)"

            val twoPointsFormat: String
                get() = "$twoPointersMade/$twoPointersAttempted($twoPointersPercentage%)"

            val threePointsFormat: String
                get() = "$threePointersMade/$threePointersAttempted($threePointersPercentage%)"

            val freeThrowFormat: String
                get() = "$freeThrowsMade/$freeThrowsAttempted($freeThrowsPercentage%)"
        }
    }
}
