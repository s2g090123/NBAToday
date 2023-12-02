package com.jiachian.nbatoday.models.local.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.database.converter.PlayerInfoConverter
import com.jiachian.nbatoday.models.PlayerCareerFactory
import com.jiachian.nbatoday.models.local.player.Player
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PlayerCareerInfoConverterTest {
    private val converter = PlayerInfoConverter(testGeneralGson)

    @Test
    fun from_PlayerCareerInfoToString_isCorrect() {
        val careerInfo = PlayerCareerFactory.createHomePlayerCareer().info
        val type = object : TypeToken<Player.PlayerInfo>() {}.type
        val expected = testGeneralGson.toJson(careerInfo, type)
        val actual = converter.from(careerInfo)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToPlayerCareerInfo_isCorrect() {
        val careerInfo = PlayerCareerFactory.createHomePlayerCareer().info
        val type = object : TypeToken<Player.PlayerInfo>() {}.type
        val actual = converter.to(testGeneralGson.toJson(careerInfo, type))
        assertThat(actual, `is`(careerInfo))
    }
}
