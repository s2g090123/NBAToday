package com.jiachian.nbatoday.models.local.converter

import com.jiachian.nbatoday.database.converter.ConferenceConverter
import com.jiachian.nbatoday.models.local.team.NBATeam
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TeamConferenceConverterTest {
    private val converter = ConferenceConverter()

    @Test
    fun from_ConferenceToString_isCorrect() {
        val actual = converter.from(NBATeam.Conference.EAST)
        assertThat(actual, `is`(NBATeam.Conference.EAST.name))
    }

    @Test
    fun to_StringToEastConference_isCorrect() {
        val actual = converter.to(NBATeam.Conference.EAST.name)
        assertThat(actual, `is`(NBATeam.Conference.EAST))
    }

    @Test
    fun to_StringToWestConference_isCorrect() {
        val actual = converter.to(NBATeam.Conference.WEST.name)
        assertThat(actual, `is`(NBATeam.Conference.WEST))
    }

    @Test
    fun to_ExceptionalStringToConference_returnsWest() {
        val actual = converter.to("")
        assertThat(actual, `is`(NBATeam.Conference.WEST))
    }
}
