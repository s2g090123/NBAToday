package com.jiachian.nbatoday.models.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.jiachian.nbatoday.database.NBADatabase
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

class NbaDatabaseTest {

    private lateinit var database: NBADatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NBADatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        mockkStatic(Room::class)
        every {
            Room.databaseBuilder<NBADatabase>(any(), any(), any()).build()
        } returns database
    }

    @After
    fun teardown() {
        database.close()
        NBADatabase.reset()
        unmockkStatic(Room::class)
    }

    @Test
    fun getInstance_checksDatabaseCorrect() {
        val context = mockk<Context>()
        val actual = NBADatabase.getInstance(context)
        assertThat(actual, `is`(database))
    }

    @Test
    fun getInstance_databaseExists_checksDatabaseCorrect() {
        val context = mockk<Context>()
        NBADatabase.getInstance(context)
        val actual = NBADatabase.getInstance(context)
        assertThat(actual, `is`(database))
    }
}
