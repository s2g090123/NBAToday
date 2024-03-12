package com.jiachian.nbatoday.test.datasource.local.bet

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.data.local.BetGenerator
import com.jiachian.nbatoday.datasource.local.bet.NBABetLocalSource
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.utils.assertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class)
class NBABetLocalSourceTest : BaseUnitTest() {
    private lateinit var localSource: NBABetLocalSource

    @Before
    fun setup() = runTest {
        get<ScheduleRepository>().updateSchedule()
        localSource = NBABetLocalSource(get())
    }

    @Test
    fun `getBetsAndGames(account) with empty expect empty`() {
        val actual = localSource.getBetsAndGames(UserAccount).stateIn(emptyList()).value
        assertIs(actual, emptyList())
    }

    @Test
    fun `insertBet(final) expects the bet has been inserted`() = launch {
        val bet = BetGenerator.getFinal()
        localSource.insertBet(bet)
        val actual = localSource.getBetsAndGames(UserAccount).stateIn(emptyList()).value
        val expected = listOf(BetAndGameGenerator.get(bet))
        assertIs(actual, expected)
    }

    @Test
    fun `deleteBet(final) expects the bet has been deleted`() = launch {
        val bet = BetGenerator.getFinal()
        localSource.insertBet(bet)
        localSource.deleteBet(bet)
        val actual = localSource.getBetsAndGames(UserAccount).stateIn(emptyList()).value
        assertIs(actual, emptyList())
    }
}
