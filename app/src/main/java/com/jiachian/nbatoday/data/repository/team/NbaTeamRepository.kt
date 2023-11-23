package com.jiachian.nbatoday.data.repository.team

import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.datasource.team.TeamLocalSource
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import com.jiachian.nbatoday.data.repository.game.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NbaTeamRepository(
    private val teamLocalSource: TeamLocalSource,
    private val remoteDataSource: RemoteDataSource,
    private val gameRepository: GameRepository,
) : TeamRepository() {
    override suspend fun refreshTeamStats(teamId: Int?) {
        val stats = remoteDataSource.getTeamStats(teamId = teamId)
        stats?.also {
            val teamStats = stats.toLocal()
            teamLocalSource.updateTeamStats(teamStats)
        }
    }

    override suspend fun refreshTeamPlayersStats(teamId: Int) {
        val stats = remoteDataSource.getTeamPlayersStats(teamId = teamId)
        stats?.also {
            val localStats = teamLocalSource.getTeamAndPlayersStats(teamId).firstOrNull()
            val playersStats = stats.toLocal()
            val oldPlayerIds = localStats?.playersStats?.map { it.playerId }
            val newPlayerIds = playersStats.map { it.playerId }
            oldPlayerIds?.filterNot { it in newPlayerIds }?.also {
                teamLocalSource.deletePlayerStats(teamId, it)
            }
            teamLocalSource.updatePlayerStats(playersStats)
        }
    }

    override fun getTeamStats(): Flow<List<TeamStats>> {
        return teamLocalSource.getTeamStats()
    }

    override fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?> {
        return teamLocalSource.getTeamAndPlayersStats(teamId)
    }

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int> {
        return teamLocalSource.getTeamRank(teamId, conference)
    }

    override fun getTeamPointsRank(teamId: Int): Flow<Int> {
        return teamLocalSource.getTeamPointsRank(teamId)
    }

    override fun getTeamReboundsRank(teamId: Int): Flow<Int> {
        return teamLocalSource.getTeamReboundsRank(teamId)
    }

    override fun getTeamAssistsRank(teamId: Int): Flow<Int> {
        return teamLocalSource.getTeamAssistsRank(teamId)
    }

    override fun getTeamPlusMinusRank(teamId: Int): Flow<Int> {
        return teamLocalSource.getTeamPlusMinusRank(teamId)
    }

    override fun getGamesBefore(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return gameRepository.getGamesAndBetsBeforeByTeam(teamId, from)
    }

    override fun getGamesAfter(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return gameRepository.getGamesAndBetsAfterByTeam(teamId, from)
    }
}
