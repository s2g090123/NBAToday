package com.jiachian.nbatoday.data.local.converter

import com.jiachian.nbatoday.data.local.team.DefaultTeam
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TeamDivisionConverterTest {
    private val converter = TeamDivisionConverter()

    @Test
    fun from_DivisionToString_isCorrect() {
        val actual = converter.from(DefaultTeam.Division.SOUTHWEST)
        assertThat(actual, `is`(DefaultTeam.Division.SOUTHWEST.name))
    }

    @Test
    fun to_StringToAtlanticDivision_isCorrect() {
        val actual = converter.to(DefaultTeam.Division.ATLANTIC.name)
        assertThat(actual, `is`(DefaultTeam.Division.ATLANTIC))
    }

    @Test
    fun to_StringToCentralDivision_isCorrect() {
        val actual = converter.to(DefaultTeam.Division.CENTRAL.name)
        assertThat(actual, `is`(DefaultTeam.Division.CENTRAL))
    }

    @Test
    fun to_StringToSouthEastDivision_isCorrect() {
        val actual = converter.to(DefaultTeam.Division.SOUTHEAST.name)
        assertThat(actual, `is`(DefaultTeam.Division.SOUTHEAST))
    }

    @Test
    fun to_StringToNorthWestDivision_isCorrect() {
        val actual = converter.to(DefaultTeam.Division.NORTHWEST.name)
        assertThat(actual, `is`(DefaultTeam.Division.NORTHWEST))
    }

    @Test
    fun to_StringToPacificDivision_isCorrect() {
        val actual = converter.to(DefaultTeam.Division.PACIFIC.name)
        assertThat(actual, `is`(DefaultTeam.Division.PACIFIC))
    }

    @Test
    fun to_StringToSouthWestDivision_isCorrect() {
        val actual = converter.to(DefaultTeam.Division.SOUTHWEST.name)
        assertThat(actual, `is`(DefaultTeam.Division.SOUTHWEST))
    }

    @Test
    fun to_ExceptionalStringToPacificDivision_isCorrect() {
        val actual = converter.to("")
        assertThat(actual, `is`(DefaultTeam.Division.PACIFIC))
    }
}