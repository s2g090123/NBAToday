package com.jiachian.nbatoday.test.repository.schedule

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.database.dao.TestGameDao
import com.jiachian.nbatoday.home.schedule.data.NBAScheduleRepository
import com.jiachian.nbatoday.service.TestGameService
import com.jiachian.nbatoday.utils.assertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NBAScheduleRepositoryTest : BaseUnitTest() {
    private lateinit var repository: NBAScheduleRepository

    @Before
    fun setup() {
        repository = NBAScheduleRepository(
            dao = TestGameDao(dataHolder),
            service = TestGameService(),
            dataStore = dataStore,
        )
    }

    @Test
    fun `updateSchedule and check games is added`() = runTest {
        repository.updateSchedule()
        dataHolder.gamesAndBets
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
    fun `updateSchedule with date and check games is added`() = runTest {
        repository.updateSchedule()
        repository.updateSchedule(0, 0, 0)
        dataHolder.gamesAndBets
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
}
