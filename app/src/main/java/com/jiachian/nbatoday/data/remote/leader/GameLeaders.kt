package com.jiachian.nbatoday.data.remote.leader

import androidx.room.ColumnInfo


data class GameLeaders(
    @ColumnInfo(name = "home_leaders")
    val homeLeaders: GameLeader?,
    @ColumnInfo(name = "away_leaders")
    val awayLeaders: GameLeader?
) {
    data class GameLeader(
        @ColumnInfo(name = "person_id")
        val personId: Int, // e.g. 1626179
        @ColumnInfo(name = "person_name")
        val name: String, // e.g. Terry Rozier
        @ColumnInfo(name = "jersey_number")
        val jerseyNum: String, // e.g. 3
        @ColumnInfo(name = "person_position")
        val position: String, // e.g. G
        @ColumnInfo(name = "team_tri_code")
        val teamTricode: String, // e.g. CHA
        @ColumnInfo(name = "person_points")
        val points: Double, // e.g. 如果比賽已經結束： 22，否則：22.2
        @ColumnInfo(name = "person_rebounds")
        val rebounds: Double, // e.g. 如果比賽已經結束： 22，否則：22.2
        @ColumnInfo(name = "person_assists")
        val assists: Double // e.g. 如果比賽已經結束： 22，否則：22.2
    )
}