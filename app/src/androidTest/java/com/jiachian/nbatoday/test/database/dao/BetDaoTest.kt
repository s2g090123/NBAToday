package com.jiachian.nbatoday.test.database.dao

import androidx.room.Room
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.UserAccount
import com.jiachian.nbatoday.data.local.BetAndGameGenerator
import com.jiachian.nbatoday.data.local.BetGenerator
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.database.NBADatabase
import com.jiachian.nbatoday.database.dao.BetDao
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
        insertGames()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun betDao_getBetsAndGames() = launch {
        dao.insertBet(BetGenerator.getFinal())
        dao.getBetsAndGames().collectOnce(this) {
            it.assertIs(listOf(BetAndGameGenerator.getFinal()))
        }
    }

    @Test
    fun betDao_getBetsAndGames_withAccounts() = launch {
        dao.insertBet(BetGenerator.getFinal())
        dao.getBetsAndGames(UserAccount).collectOnce(this) {
            it.assertIs(listOf(BetAndGameGenerator.getFinal()))
        }
    }

    @Test
    fun betDao_deleteBet() = launch {
        dao.insertBet(BetGenerator.getFinal())
        dao.deleteBet(BetGenerator.getFinal())
        dao.getBetsAndGames().collectOnce(this) {
            it.assertIs(emptyList())
        }
    }

    private suspend fun insertGames() {
        database.getGameDao().insertGames(
            listOf(
                GameGenerator.getFinal(),
                GameGenerator.getPlaying(),
                GameGenerator.getComingSoon()
            )
        )
    }
}
