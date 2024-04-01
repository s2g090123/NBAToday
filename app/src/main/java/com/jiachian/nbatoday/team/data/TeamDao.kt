package com.jiachian.nbatoday.team.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jiachian.nbatoday.team.data.model.local.Team
import com.jiachian.nbatoday.team.data.model.local.TeamAndPlayers
import com.jiachian.nbatoday.team.data.model.local.TeamPlayer
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import kotlinx.coroutines.flow.Flow

/**
 * Handle operations related to the Team entity in the database.
 */
@Dao
interface TeamDao {
    /**
     * Retrieves a flow of teams based on the conference from the database.
     *
     * @param conference The [NBATeam.Conference] for which to retrieve teams.
     * @return Flow emitting a list of [Team].
     */
    @Query("SELECT * FROM team WHERE team_conference == :conference")
    fun getTeams(conference: NBATeam.Conference): Flow<List<Team>>

    /**
     * Retrieves a flow of TeamAndPlayers for a specific team ID from the database.
     *
     * @param teamId The ID of the team for which to retrieve TeamAndPlayers.
     * @return Flow emitting a nullable [TeamAndPlayers].
     */
    @Query("SELECT * FROM team WHERE team_id == :teamId")
    fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?>

    /**
     * Retrieves the standing rank of a team within a specific conference from the database.
     *
     * @param teamId The ID of the team for which to retrieve the standing rank.
     * @param conference The [NBATeam.Conference] in which to determine the standing rank.
     * @return Flow emitting the standing rank of the team.
     */
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
    fun getTeamStanding(teamId: Int, conference: NBATeam.Conference): Flow<Int?>

    /**
     * Retrieves the points rank of a team from the database.
     *
     * @param teamId The ID of the team for which to retrieve the points rank.
     * @return Flow emitting the points rank of the team.
     */
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
    fun getPointsRank(teamId: Int): Flow<Int?>

    /**
     * Retrieves the rebounds rank of a team from the database.
     *
     * @param teamId The ID of the team for which to retrieve the rebounds rank.
     * @return Flow emitting the rebounds rank of the team.
     */
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
    fun getReboundsRank(teamId: Int): Flow<Int?>

    /**
     * Retrieves the assists rank of a team from the database.
     *
     * @param teamId The ID of the team for which to retrieve the assists rank.
     * @return Flow emitting the assists rank of the team.
     */
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
    fun getAssistsRank(teamId: Int): Flow<Int?>

    /**
     * Retrieves the plus-minus rank of a team from the database.
     *
     * @param teamId The ID of the team for which to retrieve the plus-minus rank.
     * @return Flow emitting the plus-minus rank of the team.
     */
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
    fun getPlusMinusRank(teamId: Int): Flow<Int?>

    /**
     * Inserts a list of teams into the database, replacing any existing entries on conflict.
     *
     * @param stats The list of [Team] objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeams(stats: List<Team>)

    /**
     * Inserts a list of team players into the database, replacing any existing entries on conflict.
     *
     * @param stats The list of [TeamPlayer] objects to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeamPlayers(stats: List<TeamPlayer>)

    /**
     * Deletes team players associated with a specific team and player IDs from the database.
     *
     * @param teamId The ID of the team for which to delete team players.
     * @param playerIds The list of player IDs to be deleted.
     */
    @Query("DELETE FROM team_player WHERE team_id == :teamId AND player_id IN (:playerIds)")
    suspend fun deleteTeamPlayers(teamId: Int, playerIds: List<Int>)
}
