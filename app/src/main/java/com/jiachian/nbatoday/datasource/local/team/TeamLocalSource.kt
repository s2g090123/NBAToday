package com.jiachian.nbatoday.datasource.local.team

import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamPlayer
import com.jiachian.nbatoday.models.local.team.TeamRank
import kotlinx.coroutines.flow.Flow

/**
 * The local data source for team-related operations.
 */
abstract class TeamLocalSource {
    /**
     * Retrieves a [Flow] of [Team] objects representing teams within the specified [NBATeam.Conference].
     *
     * @param conference The conference to filter teams by.
     * @return A [Flow] emitting a list of [Team] objects.
     */
    abstract fun getTeams(conference: NBATeam.Conference): Flow<List<Team>>

    /**
     * Retrieves a [Flow] of [TeamAndPlayers] representing team information and its players with the specified team ID.
     *
     * @param teamId The ID of the team.
     * @return A [Flow] emitting a nullable [TeamAndPlayers] object.
     */
    abstract fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?>

    /**
     * Retrieves a [Flow] of [TeamRank] representing the rank of a team within the specified conference.
     *
     * @param teamId The ID of the team.
     * @param conference The conference in which the rank is determined.
     * @return A [Flow] emitting a [TeamRank] object.
     */
    abstract fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<TeamRank>

    /**
     * Inserts a list of [Team] objects into the local data source.
     *
     * @param teams The list of [Team] objects to be inserted.
     */
    abstract suspend fun insertTeams(teams: List<Team>)

    /**
     * Inserts a list of [TeamPlayer] objects into the local data source.
     *
     * @param teamPlayers The list of [TeamPlayer] objects to be inserted.
     */
    abstract suspend fun insertTeamPlayers(teamPlayers: List<TeamPlayer>)

    /**
     * Deletes team players from the local data source.
     *
     * @param teamId The ID of the team.
     * @param playerIds The list of player IDs to be deleted.
     */
    abstract suspend fun deleteTeamPlayers(teamId: Int, playerIds: List<Int>)
}
