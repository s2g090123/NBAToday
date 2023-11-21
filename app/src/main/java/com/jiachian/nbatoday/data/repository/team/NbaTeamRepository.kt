package com.jiachian.nbatoday.data.repository.team

import com.jiachian.nbatoday.data.local.LocalDataSource
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.TeamAndPlayers
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import com.jiachian.nbatoday.data.repository.game.GameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NbaTeamRepository(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val gameRepository: GameRepository,
) : TeamRepository() {
    override suspend fun refreshTeamStats(teamId: Int?) {
        val stats = remoteDataSource.getTeamStats(teamId = teamId)
        stats?.also {
            val teamStats = stats.toLocal()
            localDataSource.updateTeamStats(teamStats)
        }
    }

    override suspend fun refreshTeamPlayersStats(teamId: Int) {
        val stats = remoteDataSource.getTeamPlayersStats(teamId = teamId)
        stats?.also {
            val localStats = localDataSource.getTeamAndPlayersStats(teamId).firstOrNull()
            val playersStats = stats.toLocal()
            val oldPlayerIds = localStats?.playersStats?.map { it.playerId }
            val newPlayerIds = playersStats.map { it.playerId }
            oldPlayerIds?.filterNot { it in newPlayerIds }?.also {
                localDataSource.deletePlayerStats(teamId, it)
            }
            localDataSource.updatePlayerStats(playersStats)
        }
    }

    override fun getTeamStats(): Flow<List<TeamStats>> {
        return localDataSource.getTeamStats()
    }

    override fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?> {
        return localDataSource.getTeamAndPlayersStats(teamId)
    }

    override fun getTeamRank(teamId: Int, conference: NBATeam.Conference): Flow<Int> {
        return localDataSource.getTeamRank(teamId, conference)
    }

    override fun getTeamPointsRank(teamId: Int): Flow<Int> {
        return localDataSource.getTeamPointsRank(teamId)
    }

    override fun getTeamReboundsRank(teamId: Int): Flow<Int> {
        return localDataSource.getTeamReboundsRank(teamId)
    }

    override fun getTeamAssistsRank(teamId: Int): Flow<Int> {
        return localDataSource.getTeamAssistsRank(teamId)
    }

    override fun getTeamPlusMinusRank(teamId: Int): Flow<Int> {
        return localDataSource.getTeamPlusMinusRank(teamId)
    }

    override fun getGamesBefore(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return gameRepository.getGamesAndBetsBeforeByTeam(teamId, from)
    }

    override fun getGamesAfter(teamId: Int, from: Long): Flow<List<NbaGameAndBet>> {
        return gameRepository.getGamesAndBetsAfterByTeam(teamId, from)
    }
}
