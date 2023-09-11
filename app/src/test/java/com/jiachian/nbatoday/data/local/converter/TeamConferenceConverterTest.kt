package com.jiachian.nbatoday.data.local.converter

import com.jiachian.nbatoday.data.local.team.DefaultTeam
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TeamConferenceConverterTest {
    private val converter = TeamConferenceConverter()

    @Test
    fun from_ConferenceToString_isCorrect() {
        val actual = converter.from(DefaultTeam.Conference.EAST)
        assertThat(actual, `is`(DefaultTeam.Conference.EAST.name))
    }

    @Test
    fun to_StringToEastConference_isCorrect() {
        val actual = converter.to(DefaultTeam.Conference.EAST.name)
        assertThat(actual, `is`(DefaultTeam.Conference.EAST))
    }

    @Test
    fun to_StringToWestConference_isCorrect() {
        val actual = converter.to(DefaultTeam.Conference.WEST.name)
        assertThat(actual, `is`(DefaultTeam.Conference.WEST))
    }

    @Test
    fun to_ExceptionalStringToConference_returnsWest() {
        val actual = converter.to("")
        assertThat(actual, `is`(DefaultTeam.Conference.WEST))
    }
}
