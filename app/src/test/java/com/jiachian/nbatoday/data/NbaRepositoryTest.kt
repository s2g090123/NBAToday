package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.FINAL_GAME_ID
import com.jiachian.nbatoday.GAME_TIME_MS
import com.jiachian.nbatoday.HOME_PLAYER_ID
import com.jiachian.nbatoday.HOME_TEAM_ID
import com.jiachian.nbatoday.USER_ACCOUNT
import com.jiachian.nbatoday.USER_PASSWORD
import com.jiachian.nbatoday.USER_POINTS
import com.jiachian.nbatoday.data.local.NbaGameAndBet
import com.jiachian.nbatoday.data.local.TestLocalDataSource
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.team.NBATeam
import com.jiachian.nbatoday.data.remote.RemoteGameFactory
import com.jiachian.nbatoday.data.remote.RemotePlayerFactory
import com.jiachian.nbatoday.data.remote.RemoteTeamFactory
import com.jiachian.nbatoday.data.remote.TestRemoteDataSource
import com.jiachian.nbatoday.rule.CalendarRule
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.getOrAssert
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
        repository.refreshGameBoxScore(FINAL_GAME_ID)
        val expected = RemoteGameFactory.getRemoteGameBoxScore().game?.toLocal()
        val actual = localDataSource.getGameBoxScore(FINAL_GAME_ID).first()
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
        repository.refreshTeamStats(HOME_TEAM_ID)
        val expected = RemoteTeamFactory.getRemoteTeamStats().toLocal()
        val actual = localDataSource.getTeamStats().first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun refreshTeamPlayersStats_checksDataStoreCorrect() = runTest {
        repository.refreshTeamPlayersStats(HOME_TEAM_ID)
        val expected = RemoteTeamFactory.getRemoteTeamPlayerStats().toLocal()
        val actual = localDataSource.getTeamAndPlayersStats(HOME_TEAM_ID).first()?.playersStats
        assertThat(actual, `is`(expected))
    }

    @Test
    fun refreshPlayerStats_checksDataStoreCorrect() = runTest {
        repository.refreshPlayerStats(HOME_PLAYER_ID)
        val expectedStats = RemotePlayerFactory.getRemotePlayerDetail().stats?.toLocal()?.stats
        val expectedInfo = RemotePlayerFactory.getRemotePlayerDetail().info?.toUpdateData()?.info
        val actualStats = localDataSource.getPlayerCareer(HOME_PLAYER_ID).first()?.stats
        val actualInfo = localDataSource.getPlayerCareer(HOME_PLAYER_ID).first()?.info
        assertThat(actualStats, `is`(expectedStats))
        assertThat(actualInfo, `is`(expectedInfo))
    }

    @Test
    fun refreshPlayerStats_playerExists_checksDataStoreCorrect() = runTest {
        localDataSource.insertPlayerCareer(PlayerCareerFactory.createHomePlayerCareer())
        repository.refreshPlayerStats(HOME_PLAYER_ID)
        val expectedStats = RemotePlayerFactory.getRemotePlayerDetail().stats?.toLocal()?.stats
        val expectedInfo = RemotePlayerFactory.getRemotePlayerDetail().info?.toUpdateData()?.info
        val actualStats = localDataSource.getPlayerCareer(HOME_PLAYER_ID).first()?.stats
        val actualInfo = localDataSource.getPlayerCareer(HOME_PLAYER_ID).first()?.info
        assertThat(actualStats, `is`(expectedStats))
        assertThat(actualInfo, `is`(expectedInfo))
    }

    @Test
    fun getGamesAt_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = repository.getGamesAt(GAME_TIME_MS).first()
        val expected = localDataSource.getGamesAt(GAME_TIME_MS).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGamesDuring_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = repository.getGamesDuring(GAME_TIME_MS - 1L, GAME_TIME_MS + 1L).first()
        val expected = localDataSource.getGamesDuring(GAME_TIME_MS - 1L, GAME_TIME_MS + 1L).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGamesAndBetsDuring_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        repository.bet(FINAL_GAME_ID, USER_POINTS, 0)
        val actual = repository.getGamesAndBetsDuring(GAME_TIME_MS - 1L, GAME_TIME_MS + 1L).first()
        val expected =
            localDataSource.getGamesAndBetsDuring(GAME_TIME_MS - 1L, GAME_TIME_MS + 1L).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGamesBefore_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = repository.getGamesBefore(GAME_TIME_MS + 1L).first()
        val expected = localDataSource.getGamesBefore(GAME_TIME_MS + 1L).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGamesAfter_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = repository.getGamesAfter(GAME_TIME_MS - 1L).first()
        val expected = localDataSource.getGamesAfter(GAME_TIME_MS - 1L).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getGameBoxScore_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        val actual = repository.getGameBoxScore(FINAL_GAME_ID).first()
        val expected = localDataSource.getGameBoxScore(FINAL_GAME_ID).first()
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
        repository.refreshTeamStats(HOME_TEAM_ID)
        repository.refreshTeamPlayersStats(HOME_TEAM_ID)
        val actual = repository.getTeamAndPlayersStats(HOME_TEAM_ID).first()
        val expected = localDataSource.getTeamAndPlayersStats(HOME_TEAM_ID).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamRank_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HOME_TEAM_ID)
        repository.refreshTeamPlayersStats(HOME_TEAM_ID)
        val actual = repository.getTeamRank(HOME_TEAM_ID, NBATeam.Conference.EAST).first()
        val expected =
            localDataSource.getTeamRank(HOME_TEAM_ID, NBATeam.Conference.EAST).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamPointsRank_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HOME_TEAM_ID)
        repository.refreshTeamPlayersStats(HOME_TEAM_ID)
        val actual = repository.getTeamPointsRank(HOME_TEAM_ID).first()
        val expected = localDataSource.getTeamPointsRank(HOME_TEAM_ID).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamReboundsRank_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HOME_TEAM_ID)
        repository.refreshTeamPlayersStats(HOME_TEAM_ID)
        val actual = repository.getTeamReboundsRank(HOME_TEAM_ID).first()
        val expected = localDataSource.getTeamReboundsRank(HOME_TEAM_ID).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamAssistsRank_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HOME_TEAM_ID)
        repository.refreshTeamPlayersStats(HOME_TEAM_ID)
        val actual = repository.getTeamAssistsRank(HOME_TEAM_ID).first()
        val expected = localDataSource.getTeamAssistsRank(HOME_TEAM_ID).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getTeamPlusMinusRank_checksDataStoreCorrect() = runTest {
        repository.refreshTeamStats()
        repository.refreshTeamStats(HOME_TEAM_ID)
        repository.refreshTeamPlayersStats(HOME_TEAM_ID)
        val actual = repository.getTeamPlusMinusRank(HOME_TEAM_ID).first()
        val expected = localDataSource.getTeamPlusMinusRank(HOME_TEAM_ID).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getPlayerCareer_checksDataStoreCorrect() = runTest {
        repository.refreshPlayerStats(HOME_PLAYER_ID)
        val actual = repository.getPlayerCareer(HOME_PLAYER_ID).first()
        val expected = localDataSource.getPlayerCareer(HOME_PLAYER_ID).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun login_checksDataStoreCorrect() = runTest {
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        assertThat(dataStore.userData.value?.account, `is`(USER_ACCOUNT))
        assertThat(dataStore.userData.value?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun logout_checksDataStoreCorrect() = runTest {
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        repository.logout()
        assertThat(dataStore.userData.value, nullValue())
    }

    @Test
    fun register_checksDataStoreCorrect() = runTest {
        repository.register(USER_ACCOUNT, USER_PASSWORD)
        assertThat(dataStore.userData.value?.account, `is`(USER_ACCOUNT))
        assertThat(dataStore.userData.value?.password, `is`(USER_PASSWORD))
    }

    @Test
    fun updatePassword_checksDataStoreCorrect() = runTest {
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        val newPassword = "newPassword"
        repository.updatePassword(newPassword)
        assertThat(dataStore.userData.value?.password, `is`(newPassword))
    }

    @Test
    fun updatePoints_checksDataStoreCorrect() = runTest {
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        repository.updatePoints(USER_POINTS * 2)
        assertThat(dataStore.userData.value?.points, `is`(USER_POINTS * 2))
    }

    @Test
    fun addPoints_checksDataStoreCorrect() = runTest {
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        val originalPoint = dataStore.userData.value?.points.getOrAssert()
        repository.addPoints(USER_POINTS)
        assertThat(dataStore.userData.value?.points, `is`(originalPoint + USER_POINTS))
    }

    @Test
    fun bet_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        val originalPoint = dataStore.userData.value?.points.getOrAssert()
        repository.bet(FINAL_GAME_ID, USER_POINTS, 0)
        val expectedGame = NbaGameAndBet(
            game = localDataSource.games.value.firstOrNull { it.gameId == FINAL_GAME_ID }
                .getOrAssert(),
            bets = listOf(
                Bets(
                    account = USER_ACCOUNT,
                    gameId = FINAL_GAME_ID,
                    homePoints = USER_POINTS,
                    awayPoints = 0
                )
            )
        )
        val actualGame = localDataSource.gamesAndBets.value
        assertThat(actualGame, `is`(listOf(expectedGame)))
        assertThat(dataStore.userData.value?.points, `is`(originalPoint - USER_POINTS))
    }

    @Test
    fun getGamesAndBets_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        repository.bet(FINAL_GAME_ID, USER_POINTS, 0)
        val expected = localDataSource.gamesAndBets.value
        val actual = repository.getGamesAndBets().first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getBetsAndGames_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        repository.bet(FINAL_GAME_ID, USER_POINTS, 0)
        val expected = localDataSource.getBetsAndGames().first()
        val actual = repository.getBetsAndGames().first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun getBetsAndGames_specificAccount_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        repository.bet(FINAL_GAME_ID, USER_POINTS, 0)
        val expected = localDataSource.getBetsAndGamesByUser(USER_ACCOUNT).first()
        val actual = repository.getBetsAndGames(USER_ACCOUNT).first()
        assertThat(actual, `is`(expected))
    }

    @Test
    fun deleteBets_checksDataStoreCorrect() = runTest {
        repository.refreshSchedule()
        repository.login(USER_ACCOUNT, USER_PASSWORD)
        repository.bet(FINAL_GAME_ID, USER_POINTS, 0)
        val bets = localDataSource.gamesAndBets.value.first().bets.first()
        repository.deleteBets(bets)
        assertThat(localDataSource.gamesAndBets.value.first().bets, `is`(emptyList()))
    }
}
