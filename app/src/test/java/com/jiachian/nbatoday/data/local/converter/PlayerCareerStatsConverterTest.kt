package com.jiachian.nbatoday.data.local.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.PlayerCareerFactory
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PlayerCareerStatsConverterTest {
    private val converter = PlayerCareerStatsConverter()

    @Test
    fun from_PlayerCareerStatsToString_isCorrect() {
        val careerStats = PlayerCareerFactory.createHomePlayerCareer().stats
        val gson = Gson()
        val type = object : TypeToken<PlayerCareer.PlayerCareerStats>() {}.type
        val expected = gson.toJson(careerStats, type)
        val actual = converter.from(careerStats)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToPlayerCareerStats_isCorrect() {
        val careerStats = PlayerCareerFactory.createHomePlayerCareer().stats
        val gson = Gson()
        val type = object : TypeToken<PlayerCareer.PlayerCareerStats>() {}.type
        val actual = converter.to(gson.toJson(careerStats, type))
        assertThat(actual, `is`(careerStats))
    }
}