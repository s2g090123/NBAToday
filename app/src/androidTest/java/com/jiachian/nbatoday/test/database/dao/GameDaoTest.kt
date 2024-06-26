package com.jiachian.nbatoday.test.database.dao

import androidx.room.Room
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.BasicNumber
import com.jiachian.nbatoday.FinalGameTimeMs
import com.jiachian.nbatoday.GameStatusFinal
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.HomeTeamId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.PlayingGameTimeMs
import com.jiachian.nbatoday.common.data.database.NBADatabase
import com.jiachian.nbatoday.data.local.BetGenerator
import com.jiachian.nbatoday.data.local.GameAndBetsGenerator
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.data.local.GameTeamGenerator
import com.jiachian.nbatoday.game.data.GameDao
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.game.data.model.local.GameScoreUpdateData
import com.jiachian.nbatoday.game.data.model.local.GameStatus
import com.jiachian.nbatoday.game.data.model.local.GameUpdateData
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.assertIsTrue
import com.jiachian.nbatoday.utils.collectOnce
import com.jiachian.nbatoday.utils.getOrAssert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameDaoTest : BaseAndroidTest() {
    private lateinit var database: NBADatabase
    private lateinit var dao: GameDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            context,
            NBADatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = database.getGameDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun gameDao_getGamesAndBets() = runTest {
        dao.addGames(listOf(GameGenerator.getFinal()))
        dao.getGamesAndBets().collectOnce(this) {
            it.assertIs(listOf(GameAndBetsGenerator.getFinal(false)))
        }
    }

    @Test
    fun gameDao_getGamesAndBets_withBets() = runTest {
        dao.addGames(listOf(GameGenerator.getFinal()))
        database.getBetDao().addBet(BetGenerator.getFinal())
        dao.getGamesAndBets().collectOnce(this) {
            it.assertIs(listOf(GameAndBetsGenerator.getFinal(true)))
        }
    }

    @Test
    fun gameDao_getGamesAndBetsBefore() = runTest {
        insertGames()
        dao.getGamesAndBetsBefore(HomeTeamId, PlayingGameTimeMs).collectOnce(this) {
            it.assertIs(
                listOf(
                    GameAndBetsGenerator.getFinal(false),
                )
            )
        }
    }

    @Test
    fun gameDao_getGamesAndBetsAfter() = runTest {
        insertGames()
        dao.getGamesAndBetsAfter(HomeTeamId, PlayingGameTimeMs).collectOnce(this) {
            it.assertIs(
                listOf(
                    GameAndBetsGenerator.getPlaying(false),
                    GameAndBetsGenerator.getComingSoon(false)
                )
            )
        }
    }

    @Test
    fun gameDao_getGamesAndBetsDuring() = runTest {
        insertGames()
        dao.getGamesAndBetsDuring(FinalGameTimeMs, PlayingGameTimeMs).collectOnce(this) {
            it.assertIs(
                listOf(
                    GameAndBetsGenerator.getFinal(false),
                    GameAndBetsGenerator.getPlaying(false)
                )
            )
        }
    }

    @Test
    fun gameDao_gameExists() = runTest {
        insertGames()
        dao.gameExists().assertIsTrue()
    }

    @Test
    fun gameDao_updateGames() = runTest {
        insertGames()
        dao.updateGames(
            listOf(
                GameUpdateData(
                    gameId = PlayingGameId,
                    gameStatus = GameStatus.FINAL,
                    gameStatusText = GameStatusFinal,
                    homeTeam = GameTeamGenerator.getHome(),
                    awayTeam = GameTeamGenerator.getAway(),
                    gameLeaders = GameGenerator.getPlaying().gameLeaders.getOrAssert(),
                    teamLeaders = GameGenerator.getPlaying().teamLeaders.getOrAssert()
                )
            )
        )
        dao.getGamesAndBets().collectOnce(this) { games ->
            val game = games.first { it.game.gameId == PlayingGameId }
            game.game
                .assertIsTrue { it.gameStatus == GameStatus.FINAL }
                .assertIsTrue { it.gameStatusText == GameStatusFinal }
                .assertIsTrue { it.gameLeaders == GameGenerator.getPlaying().gameLeaders }
                .assertIsTrue { it.teamLeaders == GameGenerator.getPlaying().teamLeaders }
        }
    }

    @Test
    fun gameDao_updateGameScores() = runTest {
        insertGames()
        val pointsLeaders = listOf(
            Game.PointsLeader(
                playerId = HomePlayerId,
                points = BasicNumber.toDouble(),
                teamId = HomeTeamId,
            )
        )
        dao.updateGameScores(
            listOf(
                GameScoreUpdateData(
                    gameId = PlayingGameId,
                    gameStatus = GameStatus.FINAL,
                    gameStatusText = GameStatusFinal,
                    homeTeam = GameTeamGenerator.getHome(),
                    awayTeam = GameTeamGenerator.getAway(),
                    pointsLeaders = pointsLeaders
                )
            )
        )
        dao.getGamesAndBets().collectOnce(this) { games ->
            val game = games.first { it.game.gameId == PlayingGameId }
            game.game
                .assertIsTrue { it.gameStatus == GameStatus.FINAL }
                .assertIsTrue { it.gameStatusText == GameStatusFinal }
                .assertIsTrue { it.pointsLeaders == pointsLeaders }
        }
    }

    private suspend fun insertGames() {
        dao.addGames(
            listOf(
                GameGenerator.getFinal(),
                GameGenerator.getPlaying(),
                GameGenerator.getComingSoon()
            )
        )
    }
}
