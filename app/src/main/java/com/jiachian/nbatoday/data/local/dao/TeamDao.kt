package com.jiachian.nbatoday.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Query("SELECT * FROM nba_team_stats")
    fun getTeamStats(): Flow<List<TeamStats>>

    @Query("SELECT * FROM nba_team_stats WHERE team_id == :teamId")
    fun getTeamAndPlayerStats(teamId: Int): Flow<TeamAndPlayers?>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM nba_team_stats b
                WHERE b.win_percentage > a.win_percentage AND team_conference == :conference
            )
            FROM nba_team_stats a
            WHERE team_id == :teamId AND team_conference == :conference
            ORDER BY win_percentage
        """
    )
    fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM nba_team_stats b
                WHERE
                    CASE
                        WHEN (b.points * 1.0 / b.game_played) == (a.points * 1.0 / a.game_played) THEN b.win_percentage > a.win_percentage
                        ELSE (b.points * 1.0 / b.game_played) > (a.points * 1.0 / a.game_played)
                    END
            )
            FROM nba_team_stats a
            WHERE team_id == :teamId
            ORDER BY points
        """
    )
    fun getPointsRank(teamId: Int): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM nba_team_stats b
                WHERE
                    CASE
                        WHEN (b.rebounds_total * 1.0 / b.game_played) == (a.rebounds_total * 1.0 / a.game_played) THEN b.win_percentage > a.win_percentage
                        ELSE (b.rebounds_total * 1.0 / b.game_played) > (a.rebounds_total * 1.0 / a.game_played)
                    END
            )
            FROM nba_team_stats a
            WHERE team_id == :teamId
            ORDER BY rebounds_total
        """
    )
    fun getReboundsRank(teamId: Int): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM nba_team_stats b
                WHERE
                    CASE
                        WHEN (b.assists * 1.0 / b.game_played) == (a.assists * 1.0 / a.game_played) THEN b.win_percentage > a.win_percentage
                        ELSE (b.assists * 1.0 / b.game_played) > (a.assists * 1.0 / a.game_played)
                    END
            )
            FROM nba_team_stats a
            WHERE team_id == :teamId
            ORDER BY assists
        """
    )
    fun getAssistsRank(teamId: Int): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM nba_team_stats b
                WHERE
                    CASE
                        WHEN b.plus_minus == a.plus_minus THEN b.win_percentage > a.win_percentage
                        ELSE b.plus_minus > a.plus_minus
                    END

            )
            FROM nba_team_stats a
            WHERE team_id == :teamId
            ORDER BY plus_minus
        """
    )
    fun getPlusMinusRank(teamId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamStats(stats: List<TeamStats>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamStats(stats: TeamStats)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerStats(stats: List<PlayerStats>)

    @Query("DELETE FROM nba_player_stats WHERE team_id == :teamId AND player_id IN (:playerIds)")
    suspend fun deleteTeamPlayersStats(teamId: Int, playerIds: List<Int>)
}
