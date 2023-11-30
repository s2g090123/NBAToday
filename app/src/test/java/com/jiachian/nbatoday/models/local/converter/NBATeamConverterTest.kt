package com.jiachian.nbatoday.models.local.converter

import com.jiachian.nbatoday.HomeTeam
import com.jiachian.nbatoday.database.converter.NBATeamConverter
import com.jiachian.nbatoday.models.local.team.NBATeam
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class NBATeamConverterTest {
    private val converter = NBATeamConverter(testNBATeamGson)

    @Test
    fun from_NBATeamToString_isCorrect() {
        val team = HomeTeam
        val expected = testNBATeamGson.toJson(team, NBATeam::class.java)
        val actual = converter.from(team)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToNBATeam_isCorrect() {
        val team = HomeTeam
        val actual = converter.to(testNBATeamGson.toJson(team, NBATeam::class.java))
        assertThat(actual, `is`(team))
    }
}
