package com.jiachian.nbatoday.test.database.dao

import androidx.room.Room
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.HomePlayerId
import com.jiachian.nbatoday.data.local.PlayerGenerator
import com.jiachian.nbatoday.database.NBADatabase
import com.jiachian.nbatoday.database.dao.PlayerDao
import com.jiachian.nbatoday.utils.assertIs
import com.jiachian.nbatoday.utils.collectOnce
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlayerDaoTest : BaseAndroidTest() {
    private lateinit var database: NBADatabase
    private lateinit var dao: PlayerDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            context,
            NBADatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao = database.getPlayerDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun playerDao_getPlayer() = launch {
        dao.addPlayer(PlayerGenerator.getHome())
        dao.getPlayer(HomePlayerId).collectOnce(this) {
            it.assertIs(PlayerGenerator.getHome())
        }
    }
}
