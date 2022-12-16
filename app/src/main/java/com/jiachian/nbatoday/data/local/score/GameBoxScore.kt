package com.jiachian.nbatoday.data.local.score


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.score.PlayerActiveStatus

@Entity(tableName = "nba_game_box_score")
data class GameBoxScore(
    @PrimaryKey @ColumnInfo(name = "game_id")
    val gameId: String, // e.g. 0022200414
    @ColumnInfo(name = "game_date")
    val gameDate: String, // e.g. 2022-12-15
    @ColumnInfo(name = "game_code")
    val gameCode: String, // e.g. 20221214/DETCHA
    @ColumnInfo(name = "game_status_text")
    val gameStatusText: String, // e.g. Final
    @ColumnInfo(name = "game_status")
    val gameStatus: GameStatusCode,
    @ColumnInfo(name = "home_team")
    val homeTeam: BoxScoreTeam?,
    @ColumnInfo(name = "away_team")
    val awayTeam: BoxScoreTeam?
) {
    data class BoxScoreTeam(
        @ColumnInfo(name = "team_id")
        val teamId: Int, // e.g. 1610612766
        @ColumnInfo(name = "team_name")
        val teamName: String, // e.g. Hornets
        @ColumnInfo(name = "team_city")
        val teamCity: String, // e.g. Charlotte
        @ColumnInfo(name = "team_tri_code")
        val teamTricode: String, // e.g. CHA
        @ColumnInfo(name = "score")
        val score: Int, // e.g. 134
        @ColumnInfo(name = "in_bonus")
        val inBonus: Boolean, // 是否在加罰階段
        @ColumnInfo(name = "timeouts_remaining")
        val timeoutsRemaining: Int, // 尚可使用暫停次數
        @ColumnInfo(name = "periods")
        val periods: List<Period>,
        @ColumnInfo(name = "players")
        val players: List<Player>,
        @ColumnInfo(name = "statistics")
        val statistics: Statistics?
    ) {
        data class Period(
            @ColumnInfo(name = "period")
            val period: Int, // 第幾節, e.g. 1
            @ColumnInfo(name = "period_label")
            val periodLabel: String, // 第幾節標籤, e.g. 1st or OT1
            @ColumnInfo(name = "score")
            val score: Int // e.g. 33
        )

        data class Player(
            @ColumnInfo(name = "status")
            val status: PlayerActiveStatus,
            @ColumnInfo(name = "not_playing_reason")
            val notPlayingReason: String?, // status為INACTIVE時才有值, e.g. INACTIVE_INJURY
            @ColumnInfo(name = "order")
            val order: Int, // 排序, e.g. 1
            @ColumnInfo(name = "personId")
            val personId: Int, // e.g. 1626162
            @ColumnInfo(name = "jersey_num")
            val jerseyNum: String, // e.g. 12
            @ColumnInfo(name = "position")
            val position: String, // e.g. SF
            @ColumnInfo(name = "starter")
            val starter: Boolean, // 是否為先發
            @ColumnInfo(name = "on_court")
            val onCourt: Boolean, // 是否目前在場上
            @ColumnInfo(name = "played")
            val played: Boolean, // 是否已上場
            @ColumnInfo(name = "statistics")
            val statistics: Statistics?,
            @ColumnInfo(name = "name")
            val name: String, // e.g. Kelly Oubre Jr.
            @ColumnInfo(name = "name_abbr")
            val nameAbbr: String, // e.g. K. Oubre Jr.
            @ColumnInfo(name = "first_name")
            val firstName: String, // e.g. Kelly
            @ColumnInfo(name = "family_name")
            val familyName: String // e.g. Oubre Jr.
        ) {
            data class Statistics(
                @ColumnInfo(name = "assists")
                val assists: Int, // 助攻數
                @ColumnInfo(name = "blocks")
                val blocks: Int, // 阻攻數
                @ColumnInfo(name = "field_goals_attempted")
                val fieldGoalsAttempted: Int, // 出手次數
                @ColumnInfo(name = "field_goals_made")
                val fieldGoalsMade: Int, // 進球數
                @ColumnInfo(name = "field_goals_percentage")
                val fieldGoalsPercentage: Double, // 41.2
                @ColumnInfo(name = "fouls_offensive")
                val foulsOffensive: Int, // 進攻犯規數
                @ColumnInfo(name = "fouls_drawn")
                val foulsDrawn: Int, // 被犯規數
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
                @ColumnInfo(name = "minus")
                val minus: Double, // 在場上時，球隊損失總分
                @ColumnInfo(name = "minutes")
                val minutes: String, // 上場總時間, e.g. 36:12 (分：秒)
                @ColumnInfo(name = "plus")
                val plus: Double, // 在場上時，球隊得分總分
                @ColumnInfo(name = "plus_minus_points")
                val plusMinusPoints: Double, // 加減評分
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
            )
        }

        data class Statistics(
            @ColumnInfo(name = "assists")
            val assists: Int, // 球隊總助攻數
            @ColumnInfo(name = "blocks")
            val blocks: Int, // 球隊總阻攻數
            @ColumnInfo(name = "blocks_received")
            val blocksReceived: Int, // 球隊被阻攻數
            @ColumnInfo(name = "field_goals_attempted")
            val fieldGoalsAttempted: Int, // 球隊出手次數
            @ColumnInfo(name = "field_goals_made")
            val fieldGoalsMade: Int, // 球隊進球次數
            @ColumnInfo(name = "field_goals_percentage")
            val fieldGoalsPercentage: Double, // 66.6
            @ColumnInfo(name = "fouls_offensive")
            val foulsOffensive: Int, // 球隊進攻犯規總數
            @ColumnInfo(name = "fouls_drawn")
            val foulsDrawn: Int, // 球隊被犯規總數
            @ColumnInfo(name = "fouls_personal")
            val foulsPersonal: Int, // 球隊防守犯規總數
            @ColumnInfo(name = "fouls_team")
            val foulsTeam: Int, // 球隊犯規數 (total)
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
            @ColumnInfo(name = "rebounds_personal")
            val reboundsPersonal: Int, // 球隊籃板總數
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
            @ColumnInfo(name = "turnovers_team")
            val turnoversTeam: Int, // 球隊失誤次數
            @ColumnInfo(name = "turnovers_total")
            val turnoversTotal: Int, // 球隊失誤總數
            @ColumnInfo(name = "two_pointers_attempted")
            val twoPointersAttempted: Int, // 球隊兩分出手總數
            @ColumnInfo(name = "two_pointers_made")
            val twoPointersMade: Int, // 球隊兩分進球總數
            @ColumnInfo(name = "two_pointers_percentage")
            val twoPointersPercentage: Double // 66.6
        )
    }
}