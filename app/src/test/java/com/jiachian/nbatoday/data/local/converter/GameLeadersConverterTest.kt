package com.jiachian.nbatoday.data.local.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.GameLeaderFactory
import com.jiachian.nbatoday.data.remote.leader.GameLeaders
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class GameLeadersConverterTest {
    private val converter = GameLeadersConverter()

    @Test
    fun from_GameLeadersToString_isCorrect() {
        val gameLeaders = GameLeaderFactory.getGameLeaders()
        val gson = Gson()
        val type = object : TypeToken<GameLeaders>() {}.type
        val expected = gson.toJson(gameLeaders, type)
        val actual = converter.from(gameLeaders)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToGameLeaders_isCorrect() {
        val gameLeaders = GameLeaderFactory.getGameLeaders()
        val gson = Gson()
        val type = object : TypeToken<GameLeaders>() {}.type
        val actual = converter.to(gson.toJson(gameLeaders, type))
        assertThat(actual, `is`(gameLeaders))
    }
}
