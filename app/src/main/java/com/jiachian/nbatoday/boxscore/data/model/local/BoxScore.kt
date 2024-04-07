package com.jiachian.nbatoday.boxscore.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiachian.nbatoday.game.data.model.local.GameStatus
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam

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

    val scoreComparison: String
        get() = "${homeTeam.score} - ${awayTeam.score}"

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
            val periodLabel: String, // Quarter Label, e.g. 1st or OT1
            @ColumnInfo(name = "score")
            val score: Int // e.g. 33
        )

        data class Player(
            @ColumnInfo(name = "status")
            val status: PlayerActiveStatus,
            @ColumnInfo(name = "not_playing_reason")
            val notPlayingReason: String, // e.g. INACTIVE_INJURY, or empty if the status is not INACTIVE
            @ColumnInfo(name = "player_id")
            val playerId: Int, // e.g. 1626162
            @ColumnInfo(name = "position")
            val position: String, // e.g. SF
            @ColumnInfo(name = "starter")
            val starter: Boolean, // Is starting player
            @ColumnInfo(name = "statistics")
            val statistics: Statistics,
            @ColumnInfo(name = "name_abbr")
            val nameAbbr: String, // e.g. K. Oubre Jr.
        ) {
            data class Statistics(
                @ColumnInfo(name = "assists")
                val assists: Int,
                @ColumnInfo(name = "blocks")
                val blocks: Int,
                @ColumnInfo(name = "blocks_received")
                val blocksReceived: Int,
                @ColumnInfo(name = "field_goals_attempted")
                val fieldGoalsAttempted: Int,
                @ColumnInfo(name = "field_goals_made")
                val fieldGoalsMade: Int,
                @ColumnInfo(name = "field_goals_percentage")
                val fieldGoalsPercentage: Double,
                @ColumnInfo(name = "fouls_offensive")
                val foulsOffensive: Int,
                @ColumnInfo(name = "fouls_personal")
                val foulsPersonal: Int,
                @ColumnInfo(name = "fouls_technical")
                val foulsTechnical: Int,
                @ColumnInfo(name = "free_throws_attempted")
                val freeThrowsAttempted: Int,
                @ColumnInfo(name = "free_throws_made")
                val freeThrowsMade: Int,
                @ColumnInfo(name = "free_throws_percentage")
                val freeThrowsPercentage: Double,
                @ColumnInfo(name = "minutes")
                val minutes: String, // e.g. 36:12 (分：秒)
                @ColumnInfo(name = "plus_minus_points")
                val plusMinusPoints: Int,
                @ColumnInfo(name = "points")
                val points: Int,
                @ColumnInfo(name = "rebounds_defensive")
                val reboundsDefensive: Int,
                @ColumnInfo(name = "rebounds_offensive")
                val reboundsOffensive: Int,
                @ColumnInfo(name = "rebounds_total")
                val reboundsTotal: Int,
                @ColumnInfo(name = "steals")
                val steals: Int,
                @ColumnInfo(name = "three_pointers_attempted")
                val threePointersAttempted: Int,
                @ColumnInfo(name = "three_pointers_made")
                val threePointersMade: Int,
                @ColumnInfo(name = "three_pointers_percentage")
                val threePointersPercentage: Double,
                @ColumnInfo(name = "turnovers")
                val turnovers: Int,
                @ColumnInfo(name = "two_pointers_attempted")
                val twoPointersAttempted: Int,
                @ColumnInfo(name = "two_pointers_made")
                val twoPointersMade: Int,
                @ColumnInfo(name = "two_pointers_percentage")
                val twoPointersPercentage: Double
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
            val assists: Int,
            @ColumnInfo(name = "blocks")
            val blocks: Int,
            @ColumnInfo(name = "field_goals_attempted")
            val fieldGoalsAttempted: Int,
            @ColumnInfo(name = "field_goals_made")
            val fieldGoalsMade: Int,
            @ColumnInfo(name = "field_goals_percentage")
            val fieldGoalsPercentage: Double,
            @ColumnInfo(name = "fouls_personal")
            val foulsPersonal: Int,
            @ColumnInfo(name = "fouls_technical")
            val foulsTechnical: Int,
            @ColumnInfo(name = "free_throws_attempted")
            val freeThrowsAttempted: Int,
            @ColumnInfo(name = "free_throws_made")
            val freeThrowsMade: Int,
            @ColumnInfo(name = "free_throws_percentage")
            val freeThrowsPercentage: Double,
            @ColumnInfo(name = "points")
            val points: Int,
            @ColumnInfo(name = "rebounds_defensive")
            val reboundsDefensive: Int,
            @ColumnInfo(name = "rebounds_offensive")
            val reboundsOffensive: Int,
            @ColumnInfo(name = "rebounds_total")
            val reboundsTotal: Int,
            @ColumnInfo(name = "steals")
            val steals: Int,
            @ColumnInfo(name = "three_pointers_attempted")
            val threePointersAttempted: Int,
            @ColumnInfo(name = "three_pointers_made")
            val threePointersMade: Int,
            @ColumnInfo(name = "three_pointers_percentage")
            val threePointersPercentage: Double,
            @ColumnInfo(name = "turnovers")
            val turnovers: Int,
            @ColumnInfo(name = "two_pointers_attempted")
            val twoPointersAttempted: Int,
            @ColumnInfo(name = "two_pointers_made")
            val twoPointersMade: Int,
            @ColumnInfo(name = "two_pointers_percentage")
            val twoPointersPercentage: Double,
            @ColumnInfo(name = "points_fast_break")
            val pointsFastBreak: Int,
            @ColumnInfo(name = "points_from_turnovers")
            val pointsFromTurnovers: Int,
            @ColumnInfo(name = "points_in_the_paint")
            val pointsInThePaint: Int,
            @ColumnInfo(name = "points_second_chance")
            val pointsSecondChance: Int,
            @ColumnInfo(name = "bench_points")
            val benchPoints: Int,
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
