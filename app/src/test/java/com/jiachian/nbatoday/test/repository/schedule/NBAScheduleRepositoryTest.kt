package com.jiachian.nbatoday.test.repository.schedule

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.game.data.model.local.GameAndBets
import com.jiachian.nbatoday.game.data.model.remote.GameDto
import com.jiachian.nbatoday.game.data.model.remote.ScheduleDto
import com.jiachian.nbatoday.home.schedule.data.NBAScheduleRepository
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.assertIs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NBAScheduleRepositoryTest : BaseUnitTest() {
    private lateinit var remoteSource: GameRemoteSource
    private lateinit var localSource: GameLocalSource
    private lateinit var repository: NBAScheduleRepository

    @Before
    fun setup() {
        remoteSource = spyk(get<GameRemoteSource>())
        localSource = get()
        repository = NBAScheduleRepository(
            gameLocalSource = localSource,
            gameRemoteSource = remoteSource,
            dataStore = dataStore
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `updateSchedule() expects the schedules are updated`() = launch {
        repository.updateSchedule()
        localSource
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
    fun `updateSchedule() with remoteSource#getSchedule is failed expects the error is triggered`() = launch {
        coEvery { remoteSource.getSchedule() } returns Response.error(404, "".toResponseBody())
        repository.updateSchedule()
        localSource
            .getGamesAndBets()
            .stateIn(emptyList())
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `updateSchedule() with null data expects the error is triggered`() = launch {
        coEvery { remoteSource.getSchedule() } returns Response.success(ScheduleDto(null))
        repository.updateSchedule()
        localSource
            .getGamesAndBets()
            .stateIn(emptyList())
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `updateSchedule() with remoteSource#getGames is failed expects the error is triggered`() = launch {
        coEvery {
            remoteSource.getGames(any(), any(), any(), any())
        } returns Response.error(404, "".toResponseBody())
        repository.updateSchedule()
        localSource
            .getGamesAndBets()
            .stateIn(emptyList())
            .value
            .assertIs(
                listOf(
                    GameAndBets(
                        game = GameGenerator.getFinal().copy(gameLeaders = null, teamLeaders = null),
                        bets = emptyList()
                    ),
                    GameAndBets(
                        game = GameGenerator.getPlaying().copy(gameLeaders = null, teamLeaders = null),
                        bets = emptyList()
                    ),
                    GameAndBets(
                        game = GameGenerator.getComingSoon().copy(gameLeaders = null, teamLeaders = null),
                        bets = emptyList()
                    )
                )
            )
    }

    @Test
    fun `updateSchedule() with DateUtils#parseDate is null expects the error is triggered`() = launch {
        every { DateUtils.parseDate(any()) } returns null
        every { DateUtils.parseDate(any(), any(), any()) } returns null
        repository.updateSchedule()
        localSource
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
    fun `updateSchedule() with gameExists expects the schedule is updated`() = launch {
        localSource.insertGames(
            listOf(
                GameGenerator.getFinal(),
                GameGenerator.getPlaying(),
                GameGenerator.getComingSoon()
            )
        )
        unmockkObject(DateUtils)
        repository.updateSchedule()
        localSource
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
    fun `updateSchedule(date) expects the schedules are updated`() = launch {
        localSource.insertGames(listOf(GameGenerator.getFinal()))
        repository.updateSchedule(0, 0, 0)
        localSource
            .getGamesAndBets()
            .stateIn(emptyList())
            .value
            .assertIs(listOf(GameAndBetsGenerator.getFinal(false)))
    }

    @Test
    fun `updateSchedule(date) with remoteSource#getGame is failed expects onError is triggered`() = launch {
        coEvery { remoteSource.getGame(any(), any()) } returns Response.error(404, "".toResponseBody())
        localSource.insertGames(listOf(GameGenerator.getFinal()))
        repository.updateSchedule(0, 0, 0)
        localSource
            .getGamesAndBets()
            .stateIn(emptyList())
            .value
            .assertIs(listOf(GameAndBetsGenerator.getFinal(false)))
    }

    @Test
    fun `updateSchedule(date) with null data expects onError is triggered`() = launch {
        coEvery { remoteSource.getGame(any(), any()) } returns Response.success(GameDto(null))
        localSource.insertGames(listOf(GameGenerator.getFinal()))
        repository.updateSchedule(0, 0, 0)
        localSource
            .getGamesAndBets()
            .stateIn(emptyList())
            .value
            .assertIs(
                listOf(GameAndBetsGenerator.getFinal(false))
            )
    }
}
