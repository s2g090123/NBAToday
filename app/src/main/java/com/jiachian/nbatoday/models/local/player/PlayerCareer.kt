package com.jiachian.nbatoday.models.local.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiachian.nbatoday.models.local.team.NBATeam

@Entity(tableName = "player")
data class PlayerCareer(
    @PrimaryKey @ColumnInfo(name = "player_id") val playerId: Int,
    @ColumnInfo(name = "player_info") val info: PlayerCareerInfo,
    @ColumnInfo(name = "player_stats") val stats: PlayerCareerStats
) {

    data class PlayerCareerInfo(
        @ColumnInfo(name = "player_name") val playerName: String,
        @ColumnInfo(name = "player_name_abbr") val playerNameAbbr: String,
        @ColumnInfo(name = "player_age") val playerAge: Int,
        @ColumnInfo(name = "birth_date") val birthDate: String,
        @ColumnInfo(name = "country") val country: String,
        @ColumnInfo(name = "school") val school: String,
        @ColumnInfo(name = "height") val height: Double, // cm
        @ColumnInfo(name = "weight") val weight: Double, // kg
        @ColumnInfo(name = "season_exp") val seasonExperience: Int,
        @ColumnInfo(name = "jersey") val jersey: Int,
        @ColumnInfo(name = "position") val position: String,
        @ColumnInfo(name = "team") val team: NBATeam,
        @ColumnInfo(name = "draft_year") val draftYear: Int,
        @ColumnInfo(name = "draft_round") val draftRound: Int,
        @ColumnInfo(name = "draft_number") val draftNumber: Int,
        @ColumnInfo(name = "is_greatest_75") val isGreatest75: Boolean,
        @ColumnInfo(name = "headline_stats") val headlineStats: HeadlineStats
    ) {
        data class HeadlineStats(
            @ColumnInfo(name = "points") val points: Double,
            @ColumnInfo(name = "assists") val assists: Double,
            @ColumnInfo(name = "rebounds") val rebounds: Double,
            @ColumnInfo(name = "impact_estimate") val impact: Double // 球員影響值(%)
        )

        val points: Double
            get() = headlineStats.points

        val assists: Double
            get() = headlineStats.assists

        val rebounds: Double
            get() = headlineStats.rebounds

        val impact: Double
            get() = headlineStats.impact
    }

    data class PlayerCareerStats(
        @ColumnInfo(name = "career_stats") val careerStats: List<Stats>,
    ) {
        data class Stats(
            @ColumnInfo(name = "time_frame") val timeFrame: String, // 2022-23
            @ColumnInfo(name = "team_id") val teamId: Int,
            @ColumnInfo(name = "team_name_abbr") val teamNameAbbr: String,
            @ColumnInfo(name = "game_played") val gamePlayed: Int,
            @ColumnInfo(name = "win") val win: Int,
            @ColumnInfo(name = "lose") val lose: Int,
            @ColumnInfo(name = "win_percentage") val winPercentage: Double, // 勝敗場百分比, e.g. 50.0
            @ColumnInfo(name = "field_goals_made") val fieldGoalsMade: Int,
            @ColumnInfo(name = "field_goals_attempted") val fieldGoalsAttempted: Int,
            @ColumnInfo(name = "field_goals_percentage") val fieldGoalsPercentage: Double,
            @ColumnInfo(name = "three_pointers_made") val threePointersMade: Int,
            @ColumnInfo(name = "three_pointers_attempted") val threePointersAttempted: Int,
            @ColumnInfo(name = "three_pointers_percentage") val threePointersPercentage: Double,
            @ColumnInfo(name = "free_throws_made") val freeThrowsMade: Int,
            @ColumnInfo(name = "free_throws_attempted") val freeThrowsAttempted: Int,
            @ColumnInfo(name = "free_throws_percentage") val freeThrowsPercentage: Double,
            @ColumnInfo(name = "rebounds_offensive") val reboundsOffensive: Int,
            @ColumnInfo(name = "rebounds_defensive") val reboundsDefensive: Int,
            @ColumnInfo(name = "rebounds_total") val reboundsTotal: Int,
            @ColumnInfo(name = "assists") val assists: Int,
            @ColumnInfo(name = "turnovers") val turnovers: Int,
            @ColumnInfo(name = "steals") val steals: Int,
            @ColumnInfo(name = "blocks") val blocks: Int,
            @ColumnInfo(name = "fouls_personal") val foulsPersonal: Int,
            @ColumnInfo(name = "points") val points: Int,
            @ColumnInfo(name = "plus_minus") val plusMinus: Int
        )
    }
}
