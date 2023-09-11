package com.jiachian.nbatoday.data.local.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.PlayerCareerFactory
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PlayerCareerInfoConverterTest {
    private val converter = PlayerCareerInfoConverter()

    @Test
    fun from_PlayerCareerInfoToString_isCorrect() {
        val careerInfo = PlayerCareerFactory.createHomePlayerCareer().info
        val gson = Gson()
        val type = object : TypeToken<PlayerCareer.PlayerCareerInfo>() {}.type
        val expected = gson.toJson(careerInfo, type)
        val actual = converter.from(careerInfo)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToPlayerCareerInfo_isCorrect() {
        val careerInfo = PlayerCareerFactory.createHomePlayerCareer().info
        val gson = Gson()
        val type = object : TypeToken<PlayerCareer.PlayerCareerInfo>() {}.type
        val actual = converter.to(gson.toJson(careerInfo, type))
        assertThat(actual, `is`(careerInfo))
    }
}
