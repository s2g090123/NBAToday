package com.jiachian.nbatoday.repository.team

import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamAndPlayers
import com.jiachian.nbatoday.models.local.team.TeamRank
import kotlinx.coroutines.flow.Flow

/**
 * Manage team-related data.
 */
interface TeamRepository {
    /**
     * Inserts all teams.
     */
    suspend fun addTeams(): Response<Unit>

    /**
     * Updates the players of a specific team.
     *
     * @param teamId The ID of the team whose players need to be updated.
     */
    suspend fun updateTeamPlayers(teamId: Int): Response<Unit>

    /**
     * Retrieves a flow of teams based on their conference.
     *
     * @param conference The conference of the teams to retrieve.
     * @return A Flow emitting a list of Team objects.
     */
    fun getTeams(conference: NBATeam.Conference): Flow<List<Team>>

    /**
     * Retrieves a flow of detailed information about a specific team, including its players.
     *
     * @param teamId The ID of the team for which to retrieve information.
     * @return A Flow emitting a TeamAndPlayers object, or null if no information is available.
     */
    fun getTeamAndPlayers(teamId: Int): Flow<TeamAndPlayers?>

    /**
     * Retrieves a flow of the rank information for a specific team within a given conference.
     *
     * @param teamId The ID of the team for which to retrieve rank information.
     * @param conference The conference of the teams in which to calculate the rank.
     * @return A Flow emitting a TeamRank object.
     */
    fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<TeamRank>
}
