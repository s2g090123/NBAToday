package com.jiachian.nbatoday.test.repository.game

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.ComingSoonGameTimeMs
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.FinalGameTimeMs
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameTimeMs
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScoreAndGame
import com.jiachian.nbatoday.data.local.BoxScoreGenerator
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.database.dao.TestBoxScoreDao
import com.jiachian.nbatoday.database.dao.TestGameDao
import com.jiachian.nbatoday.game.data.NBAGameRepository
import com.jiachian.nbatoday.service.TestGameService
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NBAGameRepositoryTest : BaseUnitTest() {
    private lateinit var repository: NBAGameRepository

    @Before
    fun setup() {
        repository = NBAGameRepository(
            gameDao = TestGameDao(dataHolder),
            boxScoreDao = TestBoxScoreDao(dataHolder),
            gameService = TestGameService(),
        )
    }

    @Test
    fun `addBoxScore and check boxScore is added`() = runTest {
        val expected = BoxScoreGenerator.getFinal()
        repository.addBoxScore(expected.gameId)
        dataHolder.boxScores.value.assertIsTrue { it.contains(expected) }
    }

    @Test
    fun `getGamesAndBetsDuring and check games are correct`() = runTest {
        repositoryProvider.schedule.updateSchedule()
        repository.getGamesAndBetsDuring(FinalGameTimeMs, ComingSoonGameTimeMs)
            .stateIn(emptyList())
            .value
            .assertIs(
                listOf(
                    GameAndBetsGenerator.getFinal(false),
                    GameAndBetsGenerator.getPlaying(false),
                    GameAndBetsGenerator.getComingSoon(false)
                )
            )
    }

    @Test
    fun `getBoxScoreAndGame and check game is correct`() = runTest {
        repositoryProvider.schedule.updateSchedule()
        repository.addBoxScore(FinalGameId)
        repository.getBoxScoreAndGame(FinalGameId)
            .stateIn(null)
            .value
            .assertIs(
                BoxScoreAndGame(
                    boxScore = BoxScoreGenerator.getFinal(),
                    game = GameGenerator.getFinal(),
                )
            )
    }

    @Test
    fun `getGamesAndBets and check list is correct`() = runTest {
        repositoryProvider.schedule.updateSchedule()
        repository.getGamesAndBets()
            .stateIn(emptyList())
            .value
            .assertIs(
                listOf(
                    GameAndBetsGenerator.getFinal(false),
                    GameAndBetsGenerator.getPlaying(false),
                    GameAndBetsGenerator.getComingSoon(false)
                )
            )
    }

    @Test
    fun `getGameAndBets and check game is correct`() = runTest {
        repositoryProvider.schedule.updateSchedule()
        repository.getGameAndBets(FinalGameId).assertIs(GameAndBetsGenerator.getFinal(false))
    }

    @Test
    fun `getGamesAndBetsBefore and list is correct`() = runTest {
        repositoryProvider.schedule.updateSchedule()
        repository.getGamesAndBetsBefore(HomeTeamId, ComingSoonGameTimeMs)
            .stateIn(emptyList())
            .value
            .assertIs(
                listOf(
                    GameAndBetsGenerator.getFinal(false),
                    GameAndBetsGenerator.getPlaying(false),
                    GameAndBetsGenerator.getComingSoon(false)
                )
            )
    }

    @Test
    fun `getGamesAndBetsAfter and list is correct`() = runTest {
        repositoryProvider.schedule.updateSchedule()
        repository.getGamesAndBetsAfter(HomeTeamId, PlayingGameTimeMs)
            .stateIn(emptyList())
            .value
            .assertIs(listOf(GameAndBetsGenerator.getComingSoon(false)))
    }
}
