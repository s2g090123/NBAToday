package com.jiachian.nbatoday.test.repository.bet

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BetPoints
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserName
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.UserPoints
import com.jiachian.nbatoday.UserToken
import com.jiachian.nbatoday.bet.data.NBABetRepository
import com.jiachian.nbatoday.bet.data.model.local.Bet
import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.data.local.BetGenerator
import com.jiachian.nbatoday.datasource.local.bet.BetLocalSource
import com.jiachian.nbatoday.home.user.data.UserRepository
import com.jiachian.nbatoday.home.user.data.model.local.User
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsNull
import io.mockk.every
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class)
class NBABetRepositoryTest : BaseUnitTest() {
    private lateinit var localSource: BetLocalSource
    private lateinit var repository: NBABetRepository
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        localSource = get()
        userRepository = spyk(repositoryProvider.user) {
            login(UserAccount, UserPassword)
        }
        repository = NBABetRepository(
            betLocalSource = localSource,
            userRepository = userRepository
        )
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `insertBet() expects the bet is inserted and user's points are updated`() = launch {
        repository.addBet(FinalGameId, BetPoints, BetPoints)
        val expectedBet = Bet(
            account = UserAccount,
            gameId = FinalGameId,
            homePoints = BetPoints,
            awayPoints = BetPoints
        )
        localSource.getBetsAndGames(UserAccount)
            .stateIn(emptyList())
            .value
            .firstOrNull { it.game.gameId == FinalGameId }
            ?.bet
            .assertIs(expectedBet)
        repositoryProvider
            .user
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(UserPoints - 2 * BetPoints)
    }

    @Test
    fun `insertBet() with user is not login expects onError is trigger`() = launch {
        every { userRepository.user } answers {
            flowOf(null)
        }
        repository.addBet(FinalGameId, BetPoints, BetPoints)
        localSource.getBetsAndGames(UserAccount)
            .stateIn(emptyList())
            .value
            .firstOrNull { it.game.gameId == FinalGameId }
            ?.bet
            .assertIsNull()
        repositoryProvider
            .user
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(UserPoints)
    }

    @Test
    fun `insertBet() with user is unavailable expects onError is trigger`() = launch {
        every { userRepository.user } answers {
            flowOf(
                User(
                    account = UserAccount,
                    name = UserName,
                    points = UserPoints,
                    password = UserPassword,
                    token = UserToken,
                    available = false
                )
            )
        }
        repository.addBet(FinalGameId, BetPoints, BetPoints)
        localSource.getBetsAndGames(UserAccount)
            .stateIn(emptyList())
            .value
            .firstOrNull { it.game.gameId == FinalGameId }
            ?.bet
            .assertIsNull()
        repositoryProvider
            .user
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(UserPoints)
    }

    @Test
    fun `insertBet() with exceeding user's points expects onError is trigger`() = launch {
        repository.addBet(FinalGameId, UserPoints, UserPoints)
        localSource.getBetsAndGames(UserAccount)
            .stateIn(emptyList())
            .value
            .firstOrNull { it.game.gameId == FinalGameId }
            .assertIsNull()
        repositoryProvider
            .user
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(UserPoints)
    }

    @Test
    fun `deleteBet() expects bet is deleted`() = launch {
        localSource.apply {
            val bet = BetGenerator.getFinal()
            insertBet(bet)
            repository.deleteBet(bet)
            getBetsAndGames(UserAccount)
                .stateIn(emptyList())
                .value
                .firstOrNull { it.bet.betId == bet.betId }
                .assertIsNull()
        }
    }

    @Test
    fun `settleBet(final) expects rewardedPoints are correct and bet is deleted`() = launch {
        localSource.apply {
            val betAndGame = BetAndGameGenerator.getFinal()
            val wonPoints = betAndGame.getWonPoints() * 2
            val lostPoints = betAndGame.getLostPoints()
            repository
                .settleBet(betAndGame)
                .assertIs(wonPoints to lostPoints)
            localSource
                .getBetsAndGames(UserAccount)
                .stateIn(emptyList())
                .value
                .firstOrNull { it.bet.betId == betAndGame.bet.betId }
                .assertIsNull()
            repositoryProvider
                .user
                .user
                .stateIn(null)
                .value
                ?.points
                .assertIs(UserPoints + wonPoints)
        }
    }

    @Test
    fun `addPoints() expects user's points are updated`() = launch {
        repository.addPoints(BetPoints)
        repositoryProvider
            .user
            .user
            .stateIn(null)
            .value
            ?.points
            .assertIs(UserPoints + BetPoints)
    }

    @Test
    fun `getBetsAndGames() with not inserting bet expects correct`() {
        repository.getBetGames(UserAccount)
            .stateIn(emptyList())
            .value
            .assertIs(emptyList())
    }

    @Test
    fun `getBetsAndGames() with inserting bet expects correct`() = launch {
        val bet = BetGenerator.getFinal()
        localSource.insertBet(bet)
        repository.getBetGames(UserAccount)
            .stateIn(emptyList())
            .value
            .assertIs(listOf(BetAndGameGenerator.getFinal()))
    }
}
