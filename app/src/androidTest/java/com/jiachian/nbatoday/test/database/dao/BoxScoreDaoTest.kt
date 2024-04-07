package com.jiachian.nbatoday.test.database.dao

import androidx.room.Room
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.FinalGameId
import com.jiachian.nbatoday.boxscore.data.BoxScoreDao
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScoreAndGame
import com.jiachian.nbatoday.common.data.database.NBADatabase
import com.jiachian.nbatoday.data.local.BoxScoreGenerator
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.collectOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BoxScoreDaoTest : BaseAndroidTest() {
    private lateinit var database: NBADatabase
    private lateinit var dao: BoxScoreDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            context,
            NBADatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = database.getBoxScoreDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun boxScoreDao_getBoxScoreAndGame() = runTest {
        database.getGameDao().addGames(listOf(GameGenerator.getFinal()))
        dao.insertBoxScore(BoxScoreGenerator.getFinal())
        dao.getBoxScoreAndGame(FinalGameId).collectOnce(this) {
            it.assertIs(
                BoxScoreAndGame(
                    boxScore = BoxScoreGenerator.getFinal(),
                    game = GameGenerator.getFinal()
                )
            )
        }
    }
}
