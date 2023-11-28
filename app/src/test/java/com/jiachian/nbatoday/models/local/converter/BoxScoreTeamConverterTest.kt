package com.jiachian.nbatoday.models.local.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.converter.BoxScoreTeamConverter
import com.jiachian.nbatoday.models.BoxScoreFactory
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.utils.getOrError
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class BoxScoreTeamConverterTest {
    private val converter = BoxScoreTeamConverter(testGeneralGson)

    @Test
    fun from_BoxScoreTeamToString_isCorrect() {
        val boxScore = BoxScoreFactory.getFinalGameBoxScore()
        val scoreTeam = boxScore.homeTeam.getOrError()
        val type = object : TypeToken<BoxScore.BoxScoreTeam>() {}.type
        val expected = testGeneralGson.toJson(scoreTeam, type)
        val actual = converter.from(scoreTeam)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToBoxScoreTeam_isCorrect() {
        val boxScore = BoxScoreFactory.getFinalGameBoxScore()
        val scoreTeam = boxScore.homeTeam.getOrError()
        val type = object : TypeToken<BoxScore.BoxScoreTeam>() {}.type
        val actual = converter.to(testGeneralGson.toJson(scoreTeam, type))
        assertThat(actual, `is`(scoreTeam))
    }
}
