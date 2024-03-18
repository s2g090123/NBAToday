package com.jiachian.nbatoday.test.datasource.local.boxscore

import com.jiachian.nbatoday.BaseUnitTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.PlayingGameId
import com.jiachian.nbatoday.data.local.BoxScoreGenerator
import com.jiachian.nbatoday.datasource.local.boxscore.NBABoxScoreLocalSource
import com.jiachian.nbatoday.utils.assertIs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.core.component.get

@OptIn(ExperimentalCoroutinesApi::class)
class NBABoxScoreLocalSourceTest : BaseUnitTest() {
    private lateinit var localSource: NBABoxScoreLocalSource

    @Before
    fun setup() = runTest {
        repositoryProvider.schedule.updateSchedule()
        localSource = NBABoxScoreLocalSource(get())
    }

    @Test
    fun `getBoxScoreAndGame(final) with inserting finalGame expects correct`() = launch {
        repositoryProvider.game.addBoxScore(FinalGameId)
        val actual =
            localSource.getBoxScoreAndGame(FinalGameId).stateIn(null).value?.boxScore
        assertIs(actual, BoxScoreGenerator.getFinal())
    }

    @Test
    fun `getBoxScoreAndGame(playing) with inserting playingGame expects correct`() = launch {
        repositoryProvider.game.addBoxScore(PlayingGameId)
        val actual =
            localSource.getBoxScoreAndGame(PlayingGameId).stateIn(null).value?.boxScore
        assertIs(actual, BoxScoreGenerator.getPlaying())
    }

    @Test
    fun `insertBoxScore(final) expects the boxScore is inserted`() = launch {
        localSource.insertBoxScore(BoxScoreGenerator.getFinal())
        val actual =
            localSource.getBoxScoreAndGame(FinalGameId).stateIn(null).value?.boxScore
        assertIs(actual, BoxScoreGenerator.getFinal())
    }

    @Test
    fun `insertBoxScore(playing) expects the boxScore is inserted`() = launch {
        localSource.insertBoxScore(BoxScoreGenerator.getPlaying())
        val actual =
            localSource.getBoxScoreAndGame(PlayingGameId).stateIn(null).value?.boxScore
        assertIs(actual, BoxScoreGenerator.getPlaying())
    }
}
