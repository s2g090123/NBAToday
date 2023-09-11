package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import kotlinx.coroutines.flow.Flow

class NbaLocalDataSource(
    private val dao: NbaDao
) : LocalDataSource() {

    override val dates = dao.getDates()

    override val games = dao.getGames()

    override val gamesAndBets = dao.getGamesAndBets()

    override suspend fun getGamesAt(date: Long): List<NbaGame> {
        return dao.getGamesAt(date)
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return dao.getGamesDuring(from, to)
    }

    override fun getGamesAndBetsDuring(from: Long, to: Long): Flow<List<NbaGameAndBet>> {
        return dao.getGamesAndBetsDuring(from, to)
    }

    override fun getGamesBefore(from: Long): Flow<List<NbaGame>> {
        return dao.getGamesBefore(from)
    }

    override fun getGamesAfter(from: Long): Flow<List<NbaGame>> {
        return dao.getGamesAfter(from)
    }

    override suspend fun existsGame(): Boolean {
        return dao.exitsGames()
    }

    override suspend fun insertGames(games: List<NbaGame>) {
        dao.insertGames(games)
    }

    override suspend fun updateGames(games: List<GameUpdateData>) {
        dao.updateGames(games)
    }

    override suspend fun updateGamesScore(games: List<GameScoreUpdateData>) {
        dao.updateGamesScore(games)
    }

    override fun getGameBoxScore(gameId: String): Flow<GameBoxScore?> {
        return dao.getGameBoxScore(gameId)
    }

    override suspend fun insertGameBoxScore(boxScore: GameBoxScore) {
        dao.insertGameBoxScore(boxScore)
    }

    override fun getTeamStats(): Flow<List<TeamStats>> {
        return dao.getTeamStats()
    }

    override fun getTeamAndPlayersStats(teamId: Int): Flow<TeamAndPlayers?> {
        return dao.getTeamAndPlayerStats(teamId)
    }

    override fun getTeamRank(teamId: Int, conference: DefaultTeam.Conference): Flow<Int> {
        return dao.getRank(teamId, conference)
    }

    override fun getTeamPointsRank(teamId: Int): Flow<Int> {
        return dao.getPointsRank(teamId)
    }

    override fun getTeamReboundsRank(teamId: Int): Flow<Int> {
        return dao.getReboundsRank(teamId)
    }

    override fun getTeamAssistsRank(teamId: Int): Flow<Int> {
        return dao.getAssistsRank(teamId)
    }

    override fun getTeamPlusMinusRank(teamId: Int): Flow<Int> {
        return dao.getPlusMinusRank(teamId)
    }

    override suspend fun updateTeamStats(stats: TeamStats) {
        dao.insertTeamStats(stats)
    }

    override suspend fun updateTeamStats(stats: List<TeamStats>) {
        dao.insertTeamStats(stats)
    }

    override suspend fun updatePlayerStats(stats: List<PlayerStats>) {
        dao.insertPlayerStats(stats)
    }

    override suspend fun insertPlayerCareer(stats: PlayerCareer) {
        dao.insertPlayerCareer(stats)
    }

    override suspend fun updatePlayerCareerStats(stats: PlayerCareerStatsUpdate) {
        dao.updatePlayerCareerStats(stats)
    }

    override suspend fun deletePlayerStats(teamId: Int, playerIds: List<Int>) {
        dao.deleteTeamPlayersStats(teamId, playerIds)
    }

    override suspend fun updatePlayerCareerInfo(info: PlayerCareerInfoUpdate) {
        dao.updatePlayerCareerInfo(info)
    }

    override fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?> {
        return dao.getPlayerCareer(playerId)
    }

    override suspend fun insertBet(
        account: String,
        gameId: String,
        homePoints: Long,
        awayPoints: Long
    ) {
        val bets = Bets(
            account = account,
            gameId = gameId,
            homePoints = homePoints,
            awayPoints = awayPoints
        )
        dao.insertBet(bets)
    }

    override fun getBetsAndGames(): Flow<List<BetAndNbaGame>> {
        return dao.getBetsAndGames()
    }

    override fun getBetsAndGamesByUser(account: String): Flow<List<BetAndNbaGame>> {
        return dao.getBetsAndGamesByUser(account)
    }

    override suspend fun deleteBets(bets: Bets) {
        dao.deleteBet(bets)
    }

    override suspend fun existPlayer(playerId: Int): Boolean {
        return dao.exitsPlayer(playerId)
    }
}
