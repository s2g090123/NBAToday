package com.jiachian.nbatoday.test.repository.bet

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.bet.data.NBABetRepository
import com.jiachian.nbatoday.data.local.BetGenerator
import com.jiachian.nbatoday.database.dao.TestBetDao
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NBABetRepositoryTest : BaseUnitTest() {
    private lateinit var repository: NBABetRepository

    @Before
    fun setup() {
        repository = NBABetRepository(
            dao = TestBetDao(dataHolder),
        )
    }

    @Test
    fun `addBet and check bet is added`() = runTest {
        val bet = BetGenerator.getFinal()
        repository.addBet(bet)
        dataHolder.bets.value.assertIsTrue { it.contains(bet) }
    }

    @Test
    fun `deleteBet and check bet is deleted`() = runTest {
        val bet = BetGenerator.getFinal()
        repository.addBet(bet)
        repository.deleteBet(bet)
        dataHolder.bets.value.assertIsTrue { it.isEmpty() }
    }

    @Test
    fun `getBetGames and check betAndGames is correct`() = runTest {
        val bet = BetGenerator.getFinal()
        repositoryProvider.schedule.updateSchedule()
        repository.addBet(bet)
        val flow = repository.getBetGames(UserAccount).stateIn(emptyList())
        flow.value[0].bet.assertIs(bet)
    }
}
