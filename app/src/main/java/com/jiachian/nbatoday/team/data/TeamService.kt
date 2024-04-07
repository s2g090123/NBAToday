package com.jiachian.nbatoday.team.data

import com.jiachian.nbatoday.team.data.model.remote.TeamDto
import com.jiachian.nbatoday.team.data.model.remote.TeamPlayerDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TeamService {
    @GET("team/{teamId}/players")
    suspend fun getTeamPlayer(
        @Path("teamId") teamId: Int,
        @Query("season") season: String
    ): Response<TeamPlayerDto>

    @GET("team/stats")
    suspend fun getTeam(
        @Query("season") season: String,
        @Query("id") teamId: Int?
    ): Response<TeamDto>
}
