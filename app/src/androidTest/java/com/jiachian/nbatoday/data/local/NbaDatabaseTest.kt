package com.jiachian.nbatoday.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
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

    private lateinit var database: NbaDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NbaDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        mockkStatic(Room::class)
        every {
            Room.databaseBuilder<NbaDatabase>(any(), any(), any()).build()
        } returns database
    }

    @After
    fun teardown() {
        database.close()
        NbaDatabase.reset()
        unmockkStatic(Room::class)
    }

    @Test
    fun getInstance_checksDatabaseCorrect() {
        val context = mockk<Context>()
        val actual = NbaDatabase.getInstance(context)
        assertThat(actual, `is`(database))
    }

    @Test
    fun getInstance_databaseExists_checksDatabaseCorrect() {
        val context = mockk<Context>()
        NbaDatabase.getInstance(context)
        val actual = NbaDatabase.getInstance(context)
        assertThat(actual, `is`(database))
    }
}
