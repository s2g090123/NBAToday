package com.jiachian.nbatoday.test.datasource.local.game

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.UserPassword
import com.jiachian.nbatoday.data.local.BetGenerator
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.datasource.local.game.NBAGameLocalSource
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameAndBets
import com.jiachian.nbatoday.models.local.game.GameScoreUpdateData
import com.jiachian.nbatoday.models.local.game.GameStatus
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsFalse
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.getOrAssert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class)
class NBAGameLocalSourceTest : BaseUnitTest() {
    private lateinit var localSource: NBAGameLocalSource

    @Before
    fun setup() {
        localSource = NBAGameLocalSource(get())
    }

    @Test
    fun `getGamesAndBets() with nonIncluding bets expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val actual =
            localSource.getGamesAndBets().stateIn(emptyList()).value
        val expected = listOf(
            GameAndBetsGenerator.getFinal(includeBet = false),
            GameAndBetsGenerator.getPlaying(includeBet = false),
            GameAndBetsGenerator.getComingSoon(includeBet = false)
        )
        assertIs(actual, expected)
    }

    @Test
    fun `getGamesAndBets() with including bets expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        repositoryProvider.user.login(UserAccount, UserPassword)
        BetGenerator.getFinal().also {
            repositoryProvider.bet.insertBet(it.gameId, it.homePoints, it.awayPoints)
        }
        BetGenerator.getPlaying().also {
            repositoryProvider.bet.insertBet(it.gameId, it.homePoints, it.awayPoints)
        }
        BetGenerator.getComingSoon().also {
            repositoryProvider.bet.insertBet(it.gameId, it.homePoints, it.awayPoints)
        }
        val actual =
            localSource.getGamesAndBets().stateIn(emptyList()).value
        val expected = listOf(
            GameAndBetsGenerator.getFinal(),
            GameAndBetsGenerator.getPlaying(),
            GameAndBetsGenerator.getComingSoon()
        )
        assertIs(actual, expected)
    }

    @Test
    fun `getGamesAndBetsDuring(final) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getFinal()
        val actual =
            localSource.getGamesAndBetsDuring(game.gameDate.time, game.gameDate.time)
                .stateIn(emptyList())
                .value
        val expected = listOf(GameAndBetsGenerator.getFinal(false))
        assertIs(actual, expected)
    }

    @Test
    fun `getGamesAndBetsDuring(playing) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getPlaying()
        val actual =
            localSource.getGamesAndBetsDuring(game.gameDate.time, game.gameDate.time)
                .stateIn(emptyList())
                .value
        val expected = listOf(GameAndBetsGenerator.getPlaying(false))
        assertIs(actual, expected)
    }

    @Test
    fun `getGamesAndBetsDuring(comingSoon) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getComingSoon()
        val actual =
            localSource.getGamesAndBetsDuring(game.gameDate.time, game.gameDate.time)
                .stateIn(emptyList())
                .value
        val expected = listOf(GameAndBetsGenerator.getComingSoon(false))
        assertIs(actual, expected)
    }

    @Test
    fun `getGamesAndBetsBefore(home, final) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getFinal()
        val actual =
            localSource.getGamesAndBetsBefore(HomeTeamId, game.gameDate.time)
                .stateIn(emptyList())
                .value
        val expected = listOf(GameAndBetsGenerator.getFinal(false))
        assertIs(actual, expected)
    }

    @Test
    fun `getGamesAndBetsBefore(home, playing) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getPlaying()
        val actual =
            localSource.getGamesAndBetsBefore(HomeTeamId, game.gameDate.time)
                .stateIn(emptyList())
                .value
        val expected = listOf(
            GameAndBetsGenerator.getFinal(false),
            GameAndBetsGenerator.getPlaying(false)
        )
        assertIs(actual, expected)
    }

    @Test
    fun `getGamesAndBetsBefore(home, comingSoon) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getComingSoon()
        val actual =
            localSource.getGamesAndBetsBefore(HomeTeamId, game.gameDate.time)
                .stateIn(emptyList())
                .value
        val expected = listOf(
            GameAndBetsGenerator.getFinal(false),
            GameAndBetsGenerator.getPlaying(false),
            GameAndBetsGenerator.getComingSoon(false)
        )
        assertIs(actual, expected)
    }

    @Test
    fun `getGamesAndBetsAfter(home, final) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getFinal()
        val actual =
            localSource.getGamesAndBetsAfter(HomeTeamId, game.gameDate.time)
                .stateIn(emptyList())
                .value
        val expected = listOf(
            GameAndBetsGenerator.getPlaying(false),
            GameAndBetsGenerator.getComingSoon(false)
        )
        assertIs(actual, expected)
    }

    @Test
    fun `getGamesAndBetsAfter(home, playing) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getPlaying()
        val actual =
            localSource.getGamesAndBetsAfter(HomeTeamId, game.gameDate.time)
                .stateIn(emptyList())
                .value
        val expected = listOf(GameAndBetsGenerator.getComingSoon(false))
        assertIs(actual, expected)
    }

    @Test
    fun `getGamesAndBetsAfter(home, comingSoon) expects correct`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val game = GameGenerator.getComingSoon()
        val actual =
            localSource.getGamesAndBetsAfter(HomeTeamId, game.gameDate.time)
                .stateIn(emptyList())
                .value
        val expected = emptyList<GameAndBets>()
        assertIs(actual, expected)
    }

    @Test
    fun `getLastGameDateTime() expects last game is comingSoon`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val actual =
            localSource.getLastGameDateTime().stateIn(null).value
        val expected = GameGenerator.getComingSoon().gameDateTime
        assertIs(actual, expected)
    }

    @Test
    fun `getFirstGameDateTime() expects first game is final`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val actual =
            localSource.getFirstGameDateTime().stateIn(null).value
        val expected = GameGenerator.getFinal().gameDateTime
        assertIs(actual, expected)
    }

    @Test
    fun `insertGames() expects games are inserted`() = launch {
        val games = listOf(
            GameGenerator.getFinal(),
            GameGenerator.getPlaying(),
            GameGenerator.getComingSoon()
        )
        localSource.insertGames(games)
        val actual = localSource.getGamesAndBets().stateIn(emptyList()).value
        val expected = listOf(
            GameAndBetsGenerator.getFinal(false),
            GameAndBetsGenerator.getPlaying(false),
            GameAndBetsGenerator.getComingSoon(false)
        )
        assertIs(actual, expected)
    }

    @Test
    fun `updateGames(finalUpdateData) expects playing becomes final`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val playingGame = GameGenerator.getPlaying()
        val updateData = GameUpdateData(
            gameId = playingGame.gameId,
            gameStatus = GameStatus.FINAL,
            gameStatusText = GameStatusFinal,
            homeTeam = playingGame.homeTeam,
            awayTeam = playingGame.awayTeam,
            gameLeaders = playingGame.gameLeaders.getOrAssert(),
            teamLeaders = playingGame.teamLeaders.getOrAssert()
        )
        localSource.updateGames(listOf(updateData))
        val actual = localSource
            .getGamesAndBets()
            .stateIn(emptyList()).value
            .firstOrNull { it.game.gameId == PlayingGameId }
            ?.game
        assertIs(actual?.gameStatus, GameStatus.FINAL)
        assertIs(actual?.gameStatusText, GameStatusFinal)
    }

    @Test
    fun `updateGameScores(playing) expects playingGame is updated`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val playingGame = GameGenerator.getPlaying()
        val pointsLeader = Game.PointsLeader(
            playerId = HomePlayerId,
            points = BasicNumber.toDouble(),
            teamId = HomeTeamId,
        )
        val updateData = GameScoreUpdateData(
            gameId = playingGame.gameId,
            gameStatus = GameStatus.FINAL,
            gameStatusText = GameStatusFinal,
            homeTeam = playingGame.homeTeam,
            awayTeam = playingGame.awayTeam,
            pointsLeaders = listOf(pointsLeader)
        )
        localSource.updateGameScores(listOf(updateData))
        val actual = localSource
            .getGamesAndBets()
            .stateIn(emptyList()).value
            .firstOrNull { it.game.gameId == PlayingGameId }
            ?.game
        assertIs(actual?.gameStatus, GameStatus.FINAL)
        assertIs(actual?.gameStatusText, GameStatusFinal)
        assertIs(actual?.pointsLeaders, listOf(pointsLeader))
    }

    @Test
    fun `gameExists() with updateSchedule expects true`() = launch {
        repositoryProvider.schedule.updateSchedule()
        val actual = localSource.gameExists()
        assertIsTrue(actual)
    }

    @Test
    fun `gameExists() with not updateSchedule expects false`() = launch {
        val actual = localSource.gameExists()
        assertIsFalse(actual)
    }
}
