package com.jiachian.nbatoday.models

import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.GameTimeMs
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.models.local.TestLocalDataSource
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.game.NbaGameAndBet
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.models.remote.RemoteGameFactory
import com.jiachian.nbatoday.models.remote.RemotePlayerFactory
import com.jiachian.nbatoday.models.remote.RemoteTeamFactory
import com.jiachian.nbatoday.models.remote.TestRemoteDataSource
import com.jiachian.nbatoday.rule.CalendarRule
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.getOrError
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NbaRepositoryTest {

    private lateinit var repository: NbaRepository
    private lateinit var localDataSource: TestLocalDataSource
    private lateinit var remoteDataSource: TestRemoteDataSource
    private lateinit var dataStore: TestDataStore

    @get:Rule
    val calendarRule = CalendarRule()

    @Before
    fun setup() {
        localDataSource = TestLocalDataSource()
        remoteDataSource = TestRemoteDataSource()
        dataStore = TestDataStore()
        repository = NbaRepository(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            dataStore = dataStore
        )
    }

    @Test
    fun refreshSchedule_doesNotExistGame_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = localDataSource.games.value
        val updateData = RemoteGameFactory.getRemoteGameScoreboard().toGameUpdateData()[0]
        val expected = RemoteGameFactory.getRemoteSchedule().leagueSchedule?.toNbaGames()?.map {
            it.copy(
                gameStatus = updateData.gameStatus,
                gameStatusText = updateData.gameStatusText,
                homeTeam = updateData.homeTeam,
                awayTeam = updateData.awayTeam,
                gameLeaders = updateData.gameLeaders,
                teamLeaders = updateData.teamLeaders
            )
        }
        assertThat(actual, `is`(expected))
        assertThat(dataStore.recordScheduleToday.value, `is`(NbaUtils.formatDate(2023, 1, 1)))
    }

    @Test
    fun refreshSchedule_recordDayError_checksDataStoreCorrect() = runTest {
        every {
            NbaUtils.parseDate(any())
        } answers {
            null
        }
        localDataSource.insertGames(
            RemoteGameFactory.getRemoteSchedule().leagueSchedule?.toNbaGames() ?: emptyList()
        )
        repository.refreshSchedule()
        val actual = localDataSource.games.value
        val updateData = RemoteGameFactory.getRemoteGameScoreboard().toGameUpdateData()[0]
        val expected = RemoteGameFactory.getRemoteSchedule().leagueSchedule?.toNbaGames()?.map {
            it.copy(
                gameStatus = updateData.gameStatus,
                gameStatusText = updateData.gameStatusText,
                homeTeam = updateData.homeTeam,
                awayTeam = updateData.awayTeam,
                gameLeaders = updateData.gameLeaders,
                teamLeaders = updateData.teamLeaders
            )
        }
        assertThat(actual, `is`(expected))
        assertThat(dataStore.recordScheduleToday.value, `is`(NbaUtils.formatDate(2023, 1, 1)))
    }

    @Test
    fun refreshSchedule_gameExists_checksDataStoreCorrect() = runTest {
        localDataSource.insertGames(
            RemoteGameFactory.getRemoteSchedule().leagueSchedule?.toNbaGames() ?: emptyList()
        )
        repository.refreshSchedule()
        val actual = localDataSource.games.value
        val updateData = RemoteGameFactory.getRemoteGameScoreboard().toGameUpdateData()[0]
        val expected = RemoteGameFactory.getRemoteSchedule().leagueSchedule?.toNbaGames()?.map {
            it.copy(
                gameStatus = updateData.gameStatus,
                gameStatusText = updateData.gameStatusText,
                homeTeam = updateData.homeTeam,
                awayTeam = updateData.awayTeam,
                gameLeaders = updateData.gameLeaders,
                teamLeaders = updateData.teamLeaders
            )
        }
        assertThat(actual, `is`(expected))
        assertThat(dataStore.recordScheduleToday.value, `is`(NbaUtils.formatDate(2023, 1, 1)))
    }

    @Test
    fun refreshSchedule_specificDate_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.refreshSchedule(2023, 1, 1)
        val actual = localDataSource.games.value
        val updateData = RemoteGameFactory.getRemoteGameScoreboard().toGameUpdateData()[0]
        val expected = RemoteGameFactory.getRemoteSchedule().leagueSchedule?.toNbaGames()?.map {
            it.copy(
                gameStatus = updateData.gameStatus,
                gameStatusText = updateData.gameStatusText,
                homeTeam = updateData.homeTeam,
                awayTeam = updateData.awayTeam,
                gameLeaders = updateData.gameLeaders,
                teamLeaders = updateData.teamLeaders
            )
        }
        assertThat(actual, `is`(expected))
    }

    @Test
    fun refreshGameBoxScore_checksDataStoreCorrect() = runTest {
        repository.refreshGameBoxScore(FinalGameId)
        val expected = RemoteGameFactory.getRemoteGameBoxScore().game?.toLocal()
        val actual = localDataSource.getGameBoxScore(FinalGameId).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun refreshTeamStats_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        val expected = RemoteTeamFactory.getRemoteTeamStats().toLocal()
        val actual = localDataSource.getTeamStats().first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun refreshTeamStats_specificTeam_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats(HomeTeamId)
        val expected = RemoteTeamFactory.getRemoteTeamStats().toLocal()
        val actual = localDataSource.getTeamStats().first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun refreshTeamPlayersStats_checksDataStoreCorrect() = runTest {
        repository.refreshTeamPlayersStats(HomeTeamId)
        val expected = RemoteTeamFactory.getRemoteTeamPlayerStats().toLocal()
        val actual = localDataSource.getTeamAndPlayersStats(HomeTeamId).first()?.playersStats
        assertThat(actual, `is`(expected))
    }

    @Test
    fun refreshPlayerStats_checksDataStoreCorrect() = runTest {
        repository.refreshPlayerStats(HomePlayerId)
        val expectedStats = RemotePlayerFactory.getRemotePlayerDetail().stats?.toLocal()?.stats
        val expectedInfo = RemotePlayerFactory.getRemotePlayerDetail().info?.toUpdateData()?.info
        val actualStats = localDataSource.getPlayerCareer(HomePlayerId).first()?.stats
        val actualInfo = localDataSource.getPlayerCareer(HomePlayerId).first()?.info
        assertThat(actualStats, `is`(expectedStats))
        assertThat(actualInfo, `is`(expectedInfo))
    }

    @Test
    fun refreshPlayerStats_playerExists_checksDataStoreCorrect() = runTest {
        localDataSource.insertPlayerCareer(PlayerCareerFactory.createHomePlayerCareer())
        repository.refreshPlayerStats(HomePlayerId)
        val expectedStats = RemotePlayerFactory.getRemotePlayerDetail().stats?.toLocal()?.stats
        val expectedInfo = RemotePlayerFactory.getRemotePlayerDetail().info?.toUpdateData()?.info
        val actualStats = localDataSource.getPlayerCareer(HomePlayerId).first()?.stats
        val actualInfo = localDataSource.getPlayerCareer(HomePlayerId).first()?.info
        assertThat(actualStats, `is`(expectedStats))
        assertThat(actualInfo, `is`(expectedInfo))
    }

    @Test
    fun getGamesAt_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = repository.getGamesAt(GameTimeMs).first()
        val expected = localDataSource.getGamesAt(GameTimeMs).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGamesDuring_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = repository.getGamesDuring(GameTimeMs - 1L, GameTimeMs + 1L).first()
        val expected = localDataSource.getGamesDuring(GameTimeMs - 1L, GameTimeMs + 1L).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGamesAndBetsDuring_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(UserAccount, UserPassword)
        repository.bet(FinalGameId, UserPoints, 0)
        val actual = repository.getGamesAndBetsDuring(GameTimeMs - 1L, GameTimeMs + 1L).first()
        val expected =
            localDataSource.getGamesAndBetsDuring(GameTimeMs - 1L, GameTimeMs + 1L).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGamesBefore_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = repository.getGamesBefore(GameTimeMs + 1L).first()
        val expected = localDataSource.getGamesBefore(GameTimeMs + 1L).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGamesAfter_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = repository.getGamesAfter(GameTimeMs - 1L).first()
        val expected = localDataSource.getGamesAfter(GameTimeMs - 1L).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGameBoxScore_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = repository.getGameBoxScore(FinalGameId).first()
        val expected = localDataSource.getGameBoxScore(FinalGameId).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamStats_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        val actual = repository.getTeamStats().first()
        val expected = localDataSource.getTeamStats().first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamAndPlayersStats_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HomeTeamId)
        repository.refreshTeamPlayersStats(HomeTeamId)
        val actual = repository.getTeamAndPlayersStats(HomeTeamId).first()
        val expected = localDataSource.getTeamAndPlayersStats(HomeTeamId).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamRank_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HomeTeamId)
        repository.refreshTeamPlayersStats(HomeTeamId)
        val actual = repository.getTeamRank(HomeTeamId, NBATeam.Conference.EAST).first()
        val expected =
            localDataSource.getTeamRank(HomeTeamId, NBATeam.Conference.EAST).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamPointsRank_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HomeTeamId)
        repository.refreshTeamPlayersStats(HomeTeamId)
        val actual = repository.getTeamPointsRank(HomeTeamId).first()
        val expected = localDataSource.getTeamPointsRank(HomeTeamId).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamReboundsRank_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HomeTeamId)
        repository.refreshTeamPlayersStats(HomeTeamId)
        val actual = repository.getTeamReboundsRank(HomeTeamId).first()
        val expected = localDataSource.getTeamReboundsRank(HomeTeamId).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamAssistsRank_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HomeTeamId)
        repository.refreshTeamPlayersStats(HomeTeamId)
        val actual = repository.getTeamAssistsRank(HomeTeamId).first()
        val expected = localDataSource.getTeamAssistsRank(HomeTeamId).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamPlusMinusRank_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HomeTeamId)
        repository.refreshTeamPlayersStats(HomeTeamId)
        val actual = repository.getTeamPlusMinusRank(HomeTeamId).first()
        val expected = localDataSource.getTeamPlusMinusRank(HomeTeamId).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getPlayerCareer_checksDataStoreCorrect() = runTest {
        repository.refreshPlayerStats(HomePlayerId)
        val actual = repository.getPlayerCareer(HomePlayerId).first()
        val expected = localDataSource.getPlayerCareer(HomePlayerId).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun login_checksDataStoreCorrect() = runTest {
        repository.login(UserAccount, UserPassword)
        assertThat(dataStore.userData.value?.account, `is`(UserAccount))
        assertThat(dataStore.userData.value?.password, `is`(UserPassword))
    }

    @Test
    fun logout_checksDataStoreCorrect() = runTest {
        repository.login(UserAccount, UserPassword)
        repository.logout()
        assertThat(dataStore.userData.value, nullValue())
    }

    @Test
    fun register_checksDataStoreCorrect() = runTest {
        repository.register(UserAccount, UserPassword)
        assertThat(dataStore.userData.value?.account, `is`(UserAccount))
        assertThat(dataStore.userData.value?.password, `is`(UserPassword))
    }

    @Test
    fun updatePassword_checksDataStoreCorrect() = runTest {
        repository.login(UserAccount, UserPassword)
        val newPassword = "newPassword"
        repository.updatePassword(newPassword)
        assertThat(dataStore.userData.value?.password, `is`(newPassword))
    }

    @Test
    fun updatePoints_checksDataStoreCorrect() = runTest {
        repository.login(UserAccount, UserPassword)
        repository.updatePoints(UserPoints * 2)
        assertThat(dataStore.userData.value?.points, `is`(UserPoints * 2))
    }

    @Test
    fun addPoints_checksDataStoreCorrect() = runTest {
        repository.login(UserAccount, UserPassword)
        val originalPoint = dataStore.userData.value?.points.getOrError()
        repository.addPoints(UserPoints)
        assertThat(dataStore.userData.value?.points, `is`(originalPoint + UserPoints))
    }

    @Test
    fun bet_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(UserAccount, UserPassword)
        val originalPoint = dataStore.userData.value?.points.getOrError()
        repository.bet(FinalGameId, UserPoints, 0)
        val expectedGame = NbaGameAndBet(
            game = localDataSource.games.value.firstOrNull { it.gameId == FinalGameId }
                .getOrError(),
            bets = listOf(
                Bet(
                    account = UserAccount,
                    gameId = FinalGameId,
                    homePoints = UserPoints,
                    awayPoints = 0
                )
            )
        )
        val actualGame = localDataSource.gamesAndBets.value
        assertThat(actualGame, `is`(listOf(expectedGame)))
        assertThat(dataStore.userData.value?.points, `is`(originalPoint - UserPoints))
    }

    @Test
    fun getGamesAndBets_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(UserAccount, UserPassword)
        repository.bet(FinalGameId, UserPoints, 0)
        val expected = localDataSource.gamesAndBets.value
        val actual = repository.getGamesAndBets().first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getBetsAndGames_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(UserAccount, UserPassword)
        repository.bet(FinalGameId, UserPoints, 0)
        val expected = localDataSource.getBetsAndGames().first()
        val actual = repository.getBetsAndGames().first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getBetsAndGames_specificAccount_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(UserAccount, UserPassword)
        repository.bet(FinalGameId, UserPoints, 0)
        val expected = localDataSource.getBetsAndGamesByUser(UserAccount).first()
        val actual = repository.getBetsAndGames(UserAccount).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun deleteBets_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(UserAccount, UserPassword)
        repository.bet(FinalGameId, UserPoints, 0)
        val bets = localDataSource.gamesAndBets.value.first().bets.first()
        repository.deleteBets(bets)
        assertThat(localDataSource.gamesAndBets.value.first().bets, `is`(emptyList()))
    }
}
