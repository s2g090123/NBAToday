package com.jiachian.nbatoday.data.remote.leader

import androidx.room.ColumnInfo

data class GameLeaders(
    @ColumnInfo(name = "home_leaders")
    val homeLeaders: GameLeader,
    @ColumnInfo(name = "away_leaders")
    val awayLeaders: GameLeader
) {
    data class GameLeader(
        @ColumnInfo(name = "person_id")
        val personId: Int = 0, // e.g. 1626179
        @ColumnInfo(name = "person_name")
        val name: String = "unknown", // e.g. Terry Rozier
        @ColumnInfo(name = "jersey_number")
        val jerseyNum: String = "0", // e.g. 3
        @ColumnInfo(name = "person_position")
        val position: String = "unknown", // e.g. G
        @ColumnInfo(name = "team_tri_code")
        val teamTricode: String = "unknown", // e.g. CHA
        @ColumnInfo(name = "person_points")
        val points: Double = 0.0, // e.g. 如果比賽已經結束： 22，否則：22.2
        @ColumnInfo(name = "person_rebounds")
        val rebounds: Double = 0.0, // e.g. 如果比賽已經結束： 22，否則：22.2
        @ColumnInfo(name = "person_assists")
        val assists: Double = 0.0 // e.g. 如果比賽已經結束： 22，否則：22.2
    ) {
        companion object {
            fun default(): GameLeader = GameLeader()
        }
    }
}
