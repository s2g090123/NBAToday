package com.jiachian.nbatoday.data.local.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.GameLeaderFactory
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class GameLeadersConverterTest {
    private val converter = GameLeadersConverter(testGeneralGson)

    @Test
    fun from_GameLeadersToString_isCorrect() {
        val gameLeaders = GameLeaderFactory.getGameLeaders()
        val type = object : TypeToken<GameLeaders>() {}.type
        val expected = testGeneralGson.toJson(gameLeaders, type)
        val actual = converter.from(gameLeaders)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToGameLeaders_isCorrect() {
        val gameLeaders = GameLeaderFactory.getGameLeaders()
        val type = object : TypeToken<GameLeaders>() {}.type
        val actual = converter.to(testGeneralGson.toJson(gameLeaders, type))
        assertThat(actual, `is`(gameLeaders))
    }
}
