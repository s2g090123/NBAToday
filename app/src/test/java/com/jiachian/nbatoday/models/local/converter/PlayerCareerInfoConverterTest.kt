package com.jiachian.nbatoday.models.local.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.database.converter.PlayerCareerInfoConverter
import com.jiachian.nbatoday.models.PlayerCareerFactory
import com.jiachian.nbatoday.models.local.player.PlayerCareer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PlayerCareerInfoConverterTest {
    private val converter = PlayerCareerInfoConverter(testGeneralGson)

    @Test
    fun from_PlayerCareerInfoToString_isCorrect() {
        val careerInfo = PlayerCareerFactory.createHomePlayerCareer().info
        val type = object : TypeToken<PlayerCareer.PlayerCareerInfo>() {}.type
        val expected = testGeneralGson.toJson(careerInfo, type)
        val actual = converter.from(careerInfo)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToPlayerCareerInfo_isCorrect() {
        val careerInfo = PlayerCareerFactory.createHomePlayerCareer().info
        val type = object : TypeToken<PlayerCareer.PlayerCareerInfo>() {}.type
        val actual = converter.to(testGeneralGson.toJson(careerInfo, type))
        assertThat(actual, `is`(careerInfo))
    }
}
