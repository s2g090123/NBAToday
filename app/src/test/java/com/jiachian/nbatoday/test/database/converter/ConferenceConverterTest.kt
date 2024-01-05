package com.jiachian.nbatoday.test.database.converter

import com.jiachian.nbatoday.database.converter.ConferenceConverter
import com.jiachian.nbatoday.models.local.team.NBATeam
import com.jiachian.nbatoday.utils.assertIs
import org.junit.Test

class ConferenceConverterTest {
    private val converter = ConferenceConverter()

    @Test
    fun `from(EAST) expects correct`() {
        val actual = converter.from(NBATeam.Conference.EAST)
        val expected = NBATeam.Conference.EAST.toString()
        assertIs(actual, expected)
    }

    @Test
    fun `from(WEST) expects correct`() {
        val actual = converter.from(NBATeam.Conference.WEST)
        val expected = NBATeam.Conference.WEST.toString()
        assertIs(actual, expected)
    }

    @Test
    fun `to(EAST) expects correct`() {
        val actual = converter.to(NBATeam.Conference.EAST.toString())
        val expected = NBATeam.Conference.EAST
        assertIs(actual, expected)
    }

    @Test
    fun `to(WEST) expects correct`() {
        val actual = converter.to(NBATeam.Conference.WEST.toString())
        val expected = NBATeam.Conference.WEST
        assertIs(actual, expected)
    }

    @Test
    fun `to(others) expects correct`() {
        val actual = converter.to("")
        val expected = NBATeam.Conference.WEST
        assertIs(actual, expected)
    }
}
