package com.jiachian.nbatoday.data.local

import com.jiachian.nbatoday.BASIC_NUMBER
import com.jiachian.nbatoday.BASIC_TIME
import com.jiachian.nbatoday.FINAL_GAME_ID
import com.jiachian.nbatoday.HOME_PLAYER_ID
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.NEXT_TIME
import com.jiachian.nbatoday.USER_ACCOUNT
import com.jiachian.nbatoday.data.BoxScoreFactory
import com.jiachian.nbatoday.data.GameLeaderFactory
import com.jiachian.nbatoday.data.NbaGameFactory
import com.jiachian.nbatoday.data.PlayerCareerFactory
import com.jiachian.nbatoday.data.PlayerStatsFactory
import com.jiachian.nbatoday.data.TeamStatsFactory
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
import com.jiachian.nbatoday.data.remote.game.GameStatusCode
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NbaLocalDataSourceTest {
    private lateinit var dataSource: NbaLocalDataSource
    private lateinit var dao: TestDao

    @Before
    fun setup() {
        dao = TestDao()
        dataSource = NbaLocalDataSource(dao)
    }

    @Test
    fun init_checksInitStatusCorrect() = runTest {
        assertThat(dataSource.dates.first(), `is`(dao.getDates().first()))
        assertThat(dataSource.games.first(), `is`(dao.getGames().first()))
        assertThat(dataSource.gamesAndBets.first(), `is`(dao.getGamesAndBets().first()))
    }

    @Test
    fun getGamesAt_checksDataCorrect() = runTest {
        dataSource.insertGames(listOf(NbaGameFactory.getFinalGame()))
        assertThat(dataSource.getGamesAt(BASIC_TIME), hasItem(NbaGameFactory.getFinalGame()))
    }

    @Test
    fun getGamesDuring_checksDataCorrect() = runTest {
        dataSource.insertGames(listOf(NbaGameFactory.getFinalGame()))
        assertThat(
            dataSource.getGamesDuring(BASIC_TIME, NEXT_TIME).first(),
            hasItem(NbaGameFactory.getFinalGame())
        )
    }

    @Test
    fun getGamesAndBetsDuring_checksDataCorrect() = runTest {
        dataSource.insertGames(listOf(NbaGameFactory.getFinalGame()))
        val actual = dataSource.getGamesAndBetsDuring(BASIC_TIME - 1, BASIC_TIME + 1).first().map {
            it.game
        }
        assertThat(actual, hasItem(NbaGameFactory.getFinalGame()))
    }

    @Test
    fun getGamesBefore_checksDataCorrect() = runTest {
        dataSource.insertGames(listOf(NbaGameFactory.getFinalGame()))
        assertThat(
            dataSource.getGamesBefore(NEXT_TIME).first(),
            hasItem(NbaGameFactory.getFinalGame())
        )
    }

    @Test
    fun getGamesAfter_checksDataCorrect() = runTest {
        dataSource.insertGames(listOf(NbaGameFactory.getFinalGame()))
        assertThat(
            dataSource.getGamesAfter(BASIC_TIME - 1).first(),
            hasItem(NbaGameFactory.getFinalGame())
        )
    }

    @Test
    fun existsGame_checksDataCorrect() = runTest {
        assertThat(dataSource.existsGame(), `is`(false))
        dataSource.insertGames(listOf(NbaGameFactory.getFinalGame()))
        assertThat(dataSource.existsGame(), `is`(true))
    }

    @Test
    fun insertGames_checksDataCorrect() = runTest {
        dataSource.insertGames(listOf(NbaGameFactory.getFinalGame()))
        assertThat(dataSource.games.first(), hasItem(NbaGameFactory.getFinalGame()))
    }

    @Test
    fun updateGames_checksDataCorrect() = runTest {
        val finalGame = NbaGameFactory.getFinalGame()
        dataSource.insertGames(listOf(finalGame))
        dataSource.updateGames(
            listOf(
                GameUpdateData(
                    gameId = finalGame.gameId,
                    gameStatus = GameStatusCode.COMING_SOON,
                    gameStatusText = finalGame.gameStatusText,
                    homeTeam = finalGame.homeTeam,
                    awayTeam = finalGame.awayTeam,
                    gameLeaders = finalGame.gameLeaders,
                    teamLeaders = finalGame.teamLeaders
                )
            )
        )
        val actual = dataSource.games.first().firstOrNull { it.gameId == finalGame.gameId }
        assertThat(actual?.gameStatus, `is`(GameStatusCode.COMING_SOON))
    }

    @Test
    fun updateGamesScore_checksDataCorrect() = runTest {
        val finalGame = NbaGameFactory.getFinalGame()
        dataSource.insertGames(listOf(finalGame))
        dataSource.updateGamesScore(
            listOf(
                GameScoreUpdateData(
                    gameId = finalGame.gameId,
                    gameStatus = finalGame.gameStatus,
                    gameStatusText = finalGame.gameStatusText,
                    homeTeam = finalGame.homeTeam,
                    awayTeam = finalGame.awayTeam,
                    pointsLeaders = listOf(GameLeaderFactory.getHomePointsLeader())
                )
            )
        )
        val actual = dataSource.games.first().firstOrNull { it.gameId == finalGame.gameId }
        assertThat(actual?.pointsLeaders, hasItem(GameLeaderFactory.getHomePointsLeader()))
    }

    @Test
    fun getGameBoxScore_checksDataCorrect() = runTest {
        assertThat(dataSource.getGameBoxScore(FINAL_GAME_ID).first(), nullValue())
        val finalBoxScore = BoxScoreFactory.getFinalGameBoxScore()
        dataSource.insertGameBoxScore(finalBoxScore)
        val actual = dataSource.getGameBoxScore(FINAL_GAME_ID).first()
        assertThat(actual, `is`(finalBoxScore))
    }

    @Test
    fun insertGameBoxScore_checksDataCorrect() = runTest {
        val finalBoxScore = BoxScoreFactory.getFinalGameBoxScore()
        dataSource.insertGameBoxScore(finalBoxScore)
        val actual = dataSource.getGameBoxScore(FINAL_GAME_ID).first()
        assertThat(actual, `is`(finalBoxScore))
    }

    @Test
    fun getTeamStats_checksDataCorrect() = runTest {
        assertThat(dataSource.getTeamStats().first(), `is`(emptyList()))
        val homeTeam = TeamStatsFactory.getHomeTeamStats()
        dataSource.updateTeamStats(homeTeam)
        val actual = dataSource.getTeamStats().first()
        assertThat(actual, hasItem(homeTeam))
    }

    @Test
    fun getTeamAndPlayerStats_checksDataCorrect() = runTest {
        assertThat(dataSource.getTeamAndPlayersStats(HOME_TEAM_ID).first(), nullValue())
        val homeTeam = TeamStatsFactory.getHomeTeamStats()
        dataSource.updateTeamStats(homeTeam)
        val actual = dataSource.getTeamAndPlayersStats(HOME_TEAM_ID).first()
        assertThat(actual?.teamStats, `is`(homeTeam))
    }

    @Test
    fun getTeamRank_checksDataCorrect() = runTest {
        val homeTeam = TeamStatsFactory.getHomeTeamStats()
        dataSource.updateTeamStats(homeTeam)
        val actual = dataSource.getTeamRank(HOME_TEAM_ID, DefaultTeam.Conference.EAST).first()
        assertThat(actual, `is`(1))
    }

    @Test
    fun getTeamPointsRank_checksDataCorrect() = runTest {
        val homeTeam = TeamStatsFactory.getHomeTeamStats()
        dataSource.updateTeamStats(homeTeam)
        val actual = dataSource.getTeamPointsRank(HOME_TEAM_ID).first()
        assertThat(actual, `is`(1))
    }

    @Test
    fun getTeamReboundsRank_checksDataCorrect() = runTest {
        val homeTeam = TeamStatsFactory.getHomeTeamStats()
        dataSource.updateTeamStats(homeTeam)
        val actual = dataSource.getTeamReboundsRank(HOME_TEAM_ID).first()
        assertThat(actual, `is`(1))
    }

    @Test
    fun getTeamAssistsRank_checksDataCorrect() = runTest {
        val homeTeam = TeamStatsFactory.getHomeTeamStats()
        dataSource.updateTeamStats(homeTeam)
        val actual = dataSource.getTeamAssistsRank(HOME_TEAM_ID).first()
        assertThat(actual, `is`(1))
    }

    @Test
    fun getTeamPlusMinusRank_checksDataCorrect() = runTest {
        val homeTeam = TeamStatsFactory.getHomeTeamStats()
        dataSource.updateTeamStats(homeTeam)
        val actual = dataSource.getTeamPlusMinusRank(HOME_TEAM_ID).first()
        assertThat(actual, `is`(1))
    }

    @Test
    fun updateTeamStats_checksDataCorrect() = runTest {
        val homeTeam = TeamStatsFactory.getHomeTeamStats()
        dataSource.updateTeamStats(homeTeam)
        val actual = dataSource.getTeamStats().first()
        assertThat(actual, hasItem(homeTeam))
    }

    @Test
    fun updateTeamStats_manyTeams_checksDataCorrect() = runTest {
        val homeTeam = TeamStatsFactory.getHomeTeamStats()
        val awayTeam = TeamStatsFactory.getAwayTeamStats()
        dataSource.updateTeamStats(listOf(homeTeam, awayTeam))
        val actual = dataSource.getTeamStats().first()
        assertThat(actual, hasItem(homeTeam))
        assertThat(actual, hasItem(awayTeam))
    }

    @Test
    fun updatePlayerStats_checksDataCorrect() = runTest {
        val homeTeam = TeamStatsFactory.getHomeTeamStats()
        val playerStats = PlayerStatsFactory.getHomePlayerStats()
        dataSource.updateTeamStats(homeTeam)
        dataSource.updatePlayerStats(listOf(playerStats))
        val actual = dataSource.getTeamAndPlayersStats(homeTeam.teamId).first()
        assertThat(actual?.playersStats, hasItem(playerStats))
    }

    @Test
    fun insertPlayerCareer_checksDataCorrect() = runTest {
        val playerCareer = PlayerCareerFactory.createHomePlayerCareer()
        dataSource.insertPlayerCareer(playerCareer)
        val actual = dataSource.getPlayerCareer(HOME_PLAYER_ID).first()
        assertThat(actual, `is`(playerCareer))
    }

    @Test
    fun updatePlayerCareerStats_checksDataCorrect() = runTest {
        val playerCareer = PlayerCareerFactory.createHomePlayerCareer()
        dataSource.insertPlayerCareer(playerCareer)
        dataSource.updatePlayerCareerStats(
            PlayerCareerStatsUpdate(
                personId = playerCareer.personId,
                stats = playerCareer.stats.copy(
                    careerStats = ArrayList(
                        playerCareer.stats.careerStats.map { stats ->
                            stats.copy(
                                win = 0
                            )
                        }
                    )
                )
            )
        )
        val actual = dataSource.getPlayerCareer(HOME_PLAYER_ID).first()
        assertThat(actual?.personId, `is`(playerCareer.personId))
        assertThat(actual?.stats?.careerStats?.first()?.win, `is`(0))
    }

    @Test
    fun deletePlayerStats_checksDataCorrect() = runTest {
        val team = TeamStatsFactory.getHomeTeamStats()
        val playerStats = PlayerStatsFactory.getHomePlayerStats()
        dataSource.updateTeamStats(team)
        dataSource.updatePlayerStats(listOf(playerStats))
        val stats = dataSource.getTeamAndPlayersStats(HOME_TEAM_ID).first()
        assertThat(stats?.playersStats, hasItem(playerStats))
        dataSource.deletePlayerStats(team.teamId, listOf(playerStats.playerId))
        val actual = dataSource.getTeamAndPlayersStats(HOME_TEAM_ID).first()
        assertThat(actual?.playersStats, `is`(emptyList()))
    }

    @Test
    fun updatePlayerCareerInfo_checksDataCorrect() = runTest {
        val info = PlayerCareerFactory.createHomePlayerCareer()
        dataSource.insertPlayerCareer(info)
        dataSource.updatePlayerCareerInfo(
            PlayerCareerInfoUpdate(
                personId = info.personId,
                info = info.info.copy(
                    playerAge = 0
                )
            )
        )
        val actual = dataSource.getPlayerCareer(HOME_PLAYER_ID).first()
        assertThat(actual?.info?.playerAge, `is`(0))
    }

    @Test
    fun getPlayerCareer_checksDataCorrect() = runTest {
        val info = PlayerCareerFactory.createHomePlayerCareer()
        dataSource.insertPlayerCareer(info)
        val actual = dataSource.getPlayerCareer(HOME_PLAYER_ID).first()
        assertThat(actual, `is`(info))
    }

    @Test
    fun insertBet_checksDataCorrect() = runTest {
        val bet = Bets(
            account = USER_ACCOUNT,
            gameId = FINAL_GAME_ID,
            homePoints = BASIC_NUMBER.toLong(),
            awayPoints = 0
        )
        val game = NbaGameFactory.getFinalGame()
        dataSource.insertGames(listOf(game))
        dataSource.insertBet(USER_ACCOUNT, FINAL_GAME_ID, BASIC_NUMBER.toLong(), 0L)
        val actual = dataSource.getBetsAndGames().first()
        assertThat(actual.map { it.bets }, hasItem(bet))
    }

    @Test
    fun getBetsAndGames_checksDataCorrect() = runTest {
        val bet = Bets(
            account = USER_ACCOUNT,
            gameId = FINAL_GAME_ID,
            homePoints = BASIC_NUMBER.toLong(),
            awayPoints = 0
        )
        val game = NbaGameFactory.getFinalGame()
        dataSource.insertGames(listOf(game))
        dataSource.insertBet(USER_ACCOUNT, FINAL_GAME_ID, BASIC_NUMBER.toLong(), 0L)
        val actual = dataSource.getBetsAndGames().first()
        assertThat(actual.map { it.game }, hasItem(game))
        assertThat(actual.map { it.bets }, hasItem(bet))
    }

    @Test
    fun getBetsAndGamesByUser_checksDataCorrect() = runTest {
        val bet = Bets(
            account = USER_ACCOUNT,
            gameId = FINAL_GAME_ID,
            homePoints = BASIC_NUMBER.toLong(),
            awayPoints = 0
        )
        val game = NbaGameFactory.getFinalGame()
        dataSource.insertGames(listOf(game))
        dataSource.insertBet(USER_ACCOUNT, FINAL_GAME_ID, BASIC_NUMBER.toLong(), 0L)
        val actual = dataSource.getBetsAndGamesByUser(USER_ACCOUNT).first()
        assertThat(actual.map { it.game }, hasItem(game))
        assertThat(actual.map { it.bets }, hasItem(bet))
    }

    @Test
    fun deleteBets_checksDataCorrect() = runTest {
        val bet = Bets(
            account = USER_ACCOUNT,
            gameId = FINAL_GAME_ID,
            homePoints = BASIC_NUMBER.toLong(),
            awayPoints = 0
        )
        val game = NbaGameFactory.getFinalGame()
        dataSource.insertGames(listOf(game))
        dataSource.insertBet(USER_ACCOUNT, FINAL_GAME_ID, BASIC_NUMBER.toLong(), 0L)
        dataSource.deleteBets(bet)
        val actual = dataSource.getBetsAndGames().first()
        assertThat(actual.map { it.bets }, `is`(emptyList()))
    }

    @Test
    fun existPlayer_checksDataCorrect() = runTest {
        val stats = PlayerStatsFactory.getHomePlayerStats()
        dataSource.updatePlayerStats(listOf(stats))
        val actual = dataSource.existPlayer(HOME_PLAYER_ID)
        assertThat(actual, `is`(true))
    }
}
