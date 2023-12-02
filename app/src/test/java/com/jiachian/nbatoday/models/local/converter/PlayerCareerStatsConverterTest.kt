package com.jiachian.nbatoday.models.local.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.database.converter.PlayerStatsConverter
import com.jiachian.nbatoday.models.PlayerCareerFactory
import com.jiachian.nbatoday.models.local.player.Player
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PlayerCareerStatsConverterTest {
    private val converter = PlayerStatsConverter(testGeneralGson)

    @Test
    fun from_PlayerCareerStatsToString_isCorrect() {
        val careerStats = PlayerCareerFactory.createHomePlayerCareer().stats
        val type = object : TypeToken<Player.PlayerStats>() {}.type
        val expected = testGeneralGson.toJson(careerStats, type)
        val actual = converter.from(careerStats)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToPlayerCareerStats_isCorrect() {
        val careerStats = PlayerCareerFactory.createHomePlayerCareer().stats
        val type = object : TypeToken<Player.PlayerStats>() {}.type
        val actual = converter.to(testGeneralGson.toJson(careerStats, type))
        assertThat(actual, `is`(careerStats))
    }
}
