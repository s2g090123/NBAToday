package com.jiachian.nbatoday.service

import com.jiachian.nbatoday.models.remote.team.RemoteTeam
import com.jiachian.nbatoday.models.remote.team.RemoteTeamPlayer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TeamService {
    @GET("team/{teamId}/players")
    suspend fun getTeamPlayerStats(
        @Path("teamId") teamId: Int,
        @Query("season") season: String
    ): Response<RemoteTeamPlayer>

    @GET("team/stats")
    suspend fun getTeamStats(
        @Query("season") season: String,
        @Query("id") teamId: Int?
    ): Response<RemoteTeam>
}
