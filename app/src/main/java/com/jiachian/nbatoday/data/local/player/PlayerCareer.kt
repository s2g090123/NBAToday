package com.jiachian.nbatoday.data.local.player

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jiachian.nbatoday.data.local.team.NBATeam

@Entity(tableName = "nba_player_career_stats")
data class PlayerCareer(
    @PrimaryKey @ColumnInfo(name = "person_id") val personId: Int,
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
        @ColumnInfo(name = "from_year") val fromYear: Int,
        @ColumnInfo(name = "to_year") val toYear: Int,
        @ColumnInfo(name = "draft_year") val draftYear: Int,
        @ColumnInfo(name = "draft_round") val draftRound: Int,
        @ColumnInfo(name = "draft_number") val draftNumber: Int,
        @ColumnInfo(name = "is_greatest_75") val isGreatest75: Boolean,
        @ColumnInfo(name = "headline_stats") val headlineStats: HeadlineStats
    ) {
        data class HeadlineStats(
            @ColumnInfo(name = "time_frame") val timeFrame: String, // 2022-23
            @ColumnInfo(name = "points") val points: Double,
            @ColumnInfo(name = "assists") val assists: Double,
            @ColumnInfo(name = "rebounds") val rebounds: Double,
            @ColumnInfo(name = "impact_estimate") val impact: Double // 球員影響值(%)
        )
    }

    data class PlayerCareerStats(
        @ColumnInfo(name = "career_stats") val careerStats: ArrayList<Stats>,
        @ColumnInfo(name = "career_rank") val careerRank: ArrayList<Rank>
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

        data class Rank(
            @ColumnInfo(name = "time_frame") val timeFrame: String, // 2022-23
            @ColumnInfo(name = "team_id") val teamId: Int,
            @ColumnInfo(name = "team_name_abbr") val teamNameAbbr: String,
            @ColumnInfo(name = "game_played_rank") val gamePlayedRank: Int,
            @ColumnInfo(name = "win_rank") val winRank: Int,
            @ColumnInfo(name = "lose_rank") val loseRank: Int,
            @ColumnInfo(name = "win_percentage_rank") val winPercentageRank: Int, // 勝敗場百分比, e.g. 50.0
            @ColumnInfo(name = "field_goals_made_rank") val fieldGoalsMadeRank: Int,
            @ColumnInfo(name = "field_goals_attempted_rank") val fieldGoalsAttemptedRank: Int,
            @ColumnInfo(name = "field_goals_percentage_rank") val fieldGoalsPercentageRank: Int,
            @ColumnInfo(name = "three_pointers_made_rank") val threePointersMadeRank: Int,
            @ColumnInfo(name = "three_pointers_attempted_rank") val threePointersAttemptedRank: Int,
            @ColumnInfo(name = "three_pointers_percentage_rank") val threePointersPercentageRank: Int,
            @ColumnInfo(name = "free_throws_made_rank") val freeThrowsMadeRank: Int,
            @ColumnInfo(name = "free_throws_attempted_rank") val freeThrowsAttemptedRank: Int,
            @ColumnInfo(name = "free_throws_percentage_rank") val freeThrowsPercentageRank: Int,
            @ColumnInfo(name = "rebounds_offensive_rank") val reboundsOffensiveRank: Int,
            @ColumnInfo(name = "rebounds_defensive_rank") val reboundsDefensiveRank: Int,
            @ColumnInfo(name = "rebounds_total_rank") val reboundsTotalRank: Int,
            @ColumnInfo(name = "assists_rank") val assistsRank: Int,
            @ColumnInfo(name = "turnovers_rank") val turnoversRank: Int,
            @ColumnInfo(name = "steals_rank") val stealsRank: Int,
            @ColumnInfo(name = "blocks_rank") val blocksRank: Int,
            @ColumnInfo(name = "fouls_personal_rank") val foulsPersonalRank: Int,
            @ColumnInfo(name = "points_rank") val pointsRank: Int,
            @ColumnInfo(name = "plus_minus_rank") val plusMinusRank: Int
        )
    }
}
