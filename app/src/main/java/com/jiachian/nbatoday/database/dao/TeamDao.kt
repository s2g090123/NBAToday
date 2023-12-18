package com.jiachian.nbatoday.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Query("SELECT * FROM team WHERE team_conference == :conference")
    fun getTeams(conference: NBATeam.Conference): Flow<List<Team>>

    @Query("SELECT * FROM team WHERE team_id == :teamId")
    fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM team b
                WHERE b.win_percentage > a.win_percentage AND team_conference == :conference
            )
            FROM team a
            WHERE team_id == :teamId AND team_conference == :conference
            ORDER BY win_percentage
        """
    )
    fun getTeamStanding(teamId: Int, conference: NBATeam.Conference): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM team b
                WHERE
                    CASE
                        WHEN (b.points * 1.0 / b.game_played) == (a.points * 1.0 / a.game_played) THEN b.win_percentage > a.win_percentage
                        ELSE (b.points * 1.0 / b.game_played) > (a.points * 1.0 / a.game_played)
                    END
            )
            FROM team a
            WHERE team_id == :teamId
            ORDER BY points
        """
    )
    fun getPointsRank(teamId: Int): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM team b
                WHERE
                    CASE
                        WHEN (b.rebounds_total * 1.0 / b.game_played) == (a.rebounds_total * 1.0 / a.game_played) THEN b.win_percentage > a.win_percentage
                        ELSE (b.rebounds_total * 1.0 / b.game_played) > (a.rebounds_total * 1.0 / a.game_played)
                    END
            )
            FROM team a
            WHERE team_id == :teamId
            ORDER BY rebounds_total
        """
    )
    fun getReboundsRank(teamId: Int): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM team b
                WHERE
                    CASE
                        WHEN (b.assists * 1.0 / b.game_played) == (a.assists * 1.0 / a.game_played) THEN b.win_percentage > a.win_percentage
                        ELSE (b.assists * 1.0 / b.game_played) > (a.assists * 1.0 / a.game_played)
                    END
            )
            FROM team a
            WHERE team_id == :teamId
            ORDER BY assists
        """
    )
    fun getAssistsRank(teamId: Int): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM team b
                WHERE
                    CASE
                        WHEN b.plus_minus == a.plus_minus THEN b.win_percentage > a.win_percentage
                        ELSE b.plus_minus > a.plus_minus
                    END
            )
            FROM team a
            WHERE team_id == :teamId
            ORDER BY plus_minus
        """
    )
    fun getPlusMinusRank(teamId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(stats: List<Team>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeamPlayers(stats: List<TeamPlayer>)

    @Query("DELETE FROM team_player WHERE team_id == :teamId AND player_id IN (:playerIds)")
    suspend fun deleteTeamPlayers(teamId: Int, playerIds: List<Int>)
}
