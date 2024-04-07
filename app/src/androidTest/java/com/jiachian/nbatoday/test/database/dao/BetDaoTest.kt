package com.jiachian.nbatoday.test.database.dao

import androidx.room.Room
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.bet.data.BetDao
import com.jiachian.nbatoday.common.data.database.NBADatabase
import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.data.local.BetGenerator
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.collectOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BetDaoTest : BaseAndroidTest() {
    private lateinit var database: NBADatabase
    private lateinit var dao: BetDao

    @Before
    fun setup() = runTest {
        database = Room.inMemoryDatabaseBuilder(
            context,
            NBADatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = database.getBetDao()
        database.getGameDao().addGames(
            listOf(
                GameGenerator.getFinal(),
                GameGenerator.getPlaying(),
                GameGenerator.getComingSoon()
            )
        )
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun betDao_getBetsAndGames() = runTest {
        dao.addBet(BetGenerator.getFinal())
        dao.getBetsAndGames(UserAccount).collectOnce(this) {
            it.assertIs(listOf(BetAndGameGenerator.getFinal()))
        }
    }

    @Test
    fun betDao_deleteBet() = runTest {
        dao.addBet(BetGenerator.getFinal())
        dao.deleteBet(BetGenerator.getFinal())
        dao.getBetsAndGames(UserAccount).collectOnce(this) {
            it.assertIs(emptyList())
        }
    }
}
