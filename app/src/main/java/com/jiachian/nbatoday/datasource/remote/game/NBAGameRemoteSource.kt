package com.jiachian.nbatoday.datasource.remote.game

import com.jiachian.nbatoday.models.remote.game.RemoteGame
import com.jiachian.nbatoday.models.remote.game.RemoteSchedule
import com.jiachian.nbatoday.models.remote.score.RemoteBoxScore
import com.jiachian.nbatoday.service.GameService
import retrofit2.Response

class NBAGameRemoteSource(
    private val gameService: GameService
) : GameRemoteSource() {
    override suspend fun getSchedule(): Response<RemoteSchedule> {
        return gameService.getSchedule()
    }

    override suspend fun getGame(leagueId: String, gameDate: String): Response<RemoteGame> {
        return gameService.getGame(leagueId, gameDate)
    }

    override suspend fun getGames(
        year: Int,
        month: Int,
        day: Int,
        total: Int
    ): Response<List<RemoteGame>> {
        return gameService.getGames(year, month, day, total)
    }

    override suspend fun getBoxScore(gameId: String): Response<RemoteBoxScore> {
        return gameService.getBoxScore(gameId)
    }
}
