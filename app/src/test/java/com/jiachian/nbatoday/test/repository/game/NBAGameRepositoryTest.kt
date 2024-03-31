package com.jiachian.nbatoday.test.repository.game

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.ComingSoonGameTimeMs
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.FinalGameTimeMs
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScoreAndGame
import com.jiachian.nbatoday.boxscore.data.model.remote.BoxScoreDto
import com.jiachian.nbatoday.data.local.BoxScoreGenerator
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.game.data.NBAGameRepository
import com.jiachian.nbatoday.models.remote.score.extensions.toBoxScore
import com.jiachian.nbatoday.utils.assertIs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get
import retrofit2.Response
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class NBAGameRepositoryTest : BaseUnitTest() {
    private lateinit var gameRemoteSource: GameRemoteSource
    private lateinit var gameLocalSource: GameLocalSource
    private lateinit var boxScoreLocalSource: BoxScoreLocalSource
    private lateinit var repository: NBAGameRepository

    @Before
    fun setup() {
        gameRemoteSource = spyk(get<GameRemoteSource>())
        gameLocalSource = get()
        boxScoreLocalSource = get()
        repository = NBAGameRepository(
            gameLocalSource = gameLocalSource,
            boxScoreLocalSource = boxScoreLocalSource,
            gameRemoteSource = gameRemoteSource
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `insertBoxScore(final) expects boxScore is inserted`() = launch {
        repository.addBoxScore(FinalGameId)
        dataHolder
            .boxScores
            .value
            .assertIs(listOf(BoxScoreGenerator.getFinal()))
    }

    @Test
    fun `insertBoxScore(final) with remoteSource#getBoxScore is failed expects onError is triggered`() = launch {
        coEvery { gameRemoteSource.getBoxScore(any()) } answers {
            Response.error(404, "".toResponseBody())
        }
        repository.addBoxScore(FinalGameId)
        dataHolder
            .boxScores
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `insertBoxScore(final) with null data expects onError is triggered`() = launch {
        coEvery { gameRemoteSource.getBoxScore(any()) } answers {
            Response.success(BoxScoreDto(null))
        }
        repository.addBoxScore(FinalGameId)
        dataHolder
            .boxScores
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `insertBoxScore(final) with error boxScore expects onError is triggered`() = launch {
        mockkStatic("com.jiachian.nbatoday.models.remote.score.extensions.RemoteBoxScoreExtKt")
        every {
            any<BoxScoreDto.RemoteGame>().toBoxScore()
        } returns null
        repository.addBoxScore(FinalGameId)
        dataHolder
            .boxScores
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `getGamesAndBetsDuring(finalGameTime) expects correct`() = runTest {
        repositoryProvider.schedule.updateSchedule()
        repository
            .getGamesAndBetsDuring(FinalGameTimeMs, FinalGameTimeMs)
            .stateIn(emptyList())
            .value
            .assertIs(
                listOf(GameAndBetsGenerator.getFinal(false))
            )
    }

    @Test
    fun `getBoxScoreAndGame(final) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        repository.addBoxScore(FinalGameId)
        repository
            .getBoxScoreAndGame(FinalGameId)
            .stateIn(null)
            .value
            .assertIs(
                BoxScoreAndGame(
                    boxScore = BoxScoreGenerator.getFinal(),
                    game = GameGenerator.getFinal()
                )
            )
    }

    @Test
    fun `getGamesAndBets() expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        repository
            .getGamesAndBets()
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
    fun `getGamesAndBetsBefore(home, comingSoonGameTime) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        repository
            .getGamesAndBetsBefore(HomeTeamId, ComingSoonGameTimeMs)
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
    fun `getGamesAndBetsAfter(home, finalGameTime) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        repository
            .getGamesAndBetsAfter(HomeTeamId, FinalGameTimeMs)
            .stateIn(emptyList())
            .value
            .assertIs(
                listOf(
                    GameAndBetsGenerator.getPlaying(false),
                    GameAndBetsGenerator.getComingSoon(false)
                )
            )
    }

    @Test
    fun `getLastGameDateTime() expects the result is comingSoonGameDate`() = launch {
        repositoryProvider.schedule.updateSchedule()
        repository
            .getLastGame()
            .stateIn(Date())
            .value
            .assertIs(GameGenerator.getComingSoon().gameDateTime)
    }

    @Test
    fun `getFirstGameDateTime() expects the result is finalGameDate`() = launch {
        repositoryProvider.schedule.updateSchedule()
        repository
            .getFirstGameDateTime()
            .stateIn(Date())
            .value
            .assertIs(GameGenerator.getFinal().gameDateTime)
    }
}
