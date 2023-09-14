package com.jiachian.nbatoday.data.local.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.BoxScoreFactory
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.utils.getOrAssert
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class BoxScoreTeamConverterTest {
    private val converter = BoxScoreTeamConverter(testGeneralGson)

    @Test
    fun from_BoxScoreTeamToString_isCorrect() {
        val boxScore = BoxScoreFactory.getFinalGameBoxScore()
        val scoreTeam = boxScore.homeTeam.getOrAssert()
        val type = object : TypeToken<GameBoxScore.BoxScoreTeam>() {}.type
        val expected = testGeneralGson.toJson(scoreTeam, type)
        val actual = converter.from(scoreTeam)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToBoxScoreTeam_isCorrect() {
        val boxScore = BoxScoreFactory.getFinalGameBoxScore()
        val scoreTeam = boxScore.homeTeam.getOrAssert()
        val type = object : TypeToken<GameBoxScore.BoxScoreTeam>() {}.type
        val actual = converter.to(testGeneralGson.toJson(scoreTeam, type))
        assertThat(actual, `is`(scoreTeam))
    }
}
