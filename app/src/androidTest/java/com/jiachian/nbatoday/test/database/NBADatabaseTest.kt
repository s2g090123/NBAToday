package com.jiachian.nbatoday.test.database

import androidx.room.Room
import com.jiachian.nbatoday.BaseAndroidTest
import com.jiachian.nbatoday.database.NBADatabase
import com.jiachian.nbatoday.utils.assertIs
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Test

class NBADatabaseTest : BaseAndroidTest() {
    private lateinit var database: NBADatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            context,
            NBADatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun database_getInstance() {
        mockkStatic(Room::class)
        every {
            Room.databaseBuilder<NBADatabase>(any(), any(), any()).build()
        } returns database
        NBADatabase
            .getInstance(context)
            .assertIs(database)
        NBADatabase
            .getInstance(context)
            .assertIs(database)
        unmockkStatic(Room::class)
    }
}
