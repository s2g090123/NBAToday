package com.jiachian.nbatoday.models.local.converter

import com.jiachian.nbatoday.models.local.team.NBATeam
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TeamDivisionConverterTest {
    private val converter = TeamDivisionConverter()

    @Test
    fun from_DivisionToString_isCorrect() {
        val actual = converter.from(NBATeam.Division.SOUTHWEST)
        assertThat(actual, `is`(NBATeam.Division.SOUTHWEST.name))
    }

    @Test
    fun to_StringToAtlanticDivision_isCorrect() {
        val actual = converter.to(NBATeam.Division.ATLANTIC.name)
        assertThat(actual, `is`(NBATeam.Division.ATLANTIC))
    }

    @Test
    fun to_StringToCentralDivision_isCorrect() {
        val actual = converter.to(NBATeam.Division.CENTRAL.name)
        assertThat(actual, `is`(NBATeam.Division.CENTRAL))
    }

    @Test
    fun to_StringToSouthEastDivision_isCorrect() {
        val actual = converter.to(NBATeam.Division.SOUTHEAST.name)
        assertThat(actual, `is`(NBATeam.Division.SOUTHEAST))
    }

    @Test
    fun to_StringToNorthWestDivision_isCorrect() {
        val actual = converter.to(NBATeam.Division.NORTHWEST.name)
        assertThat(actual, `is`(NBATeam.Division.NORTHWEST))
    }

    @Test
    fun to_StringToPacificDivision_isCorrect() {
        val actual = converter.to(NBATeam.Division.PACIFIC.name)
        assertThat(actual, `is`(NBATeam.Division.PACIFIC))
    }

    @Test
    fun to_StringToSouthWestDivision_isCorrect() {
        val actual = converter.to(NBATeam.Division.SOUTHWEST.name)
        assertThat(actual, `is`(NBATeam.Division.SOUTHWEST))
    }

    @Test
    fun to_ExceptionalStringToPacificDivision_isCorrect() {
        val actual = converter.to("")
        assertThat(actual, `is`(NBATeam.Division.PACIFIC))
    }
}
