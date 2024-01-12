package com.jiachian.nbatoday.datasource.remote.team

import com.jiachian.nbatoday.models.remote.team.RemoteTeam
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayer
import retrofit2.Response

/**
 * The remote data source for team-related operations.
 */
abstract class TeamRemoteSource {
    /**
     * Retrieves information about NBA teams from the remote data source.
     *
     * @return A [Response] containing a [RemoteTeam] object.
     */
    abstract suspend fun getTeam(): Response<RemoteTeam>

    /**
     * Retrieves information about a specific NBA team from the remote data source.
     *
     * @param teamId The ID of the NBA team.
     * @return A [Response] containing a [RemoteTeam] object.
     */
    abstract suspend fun getTeam(teamId: Int): Response<RemoteTeam>

    /**
     * Retrieves information about team players of a specific NBA team from the remote data source.
     *
     * @param teamId The ID of the NBA team.
     * @return A [Response] containing a [RemoteTeamPlayer] object.
     */
    abstract suspend fun getTeamPlayer(teamId: Int): Response<RemoteTeamPlayer>
}
