package com.jiachian.nbatoday.data.local.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.GameTeamFactory
import com.jiachian.nbatoday.data.remote.team.GameTeam
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class GameTeamConverterTest {
    private val converter = GameTeamConverter(testGeneralGson)

    @Test
    fun from_GameTeamToString_isCorrect() {
        val gameTeam = GameTeamFactory.getDefaultHomeTeam()
        val type = object : TypeToken<GameTeam>() {}.type
        val expected = testGeneralGson.toJson(gameTeam, type)
        val actual = converter.from(gameTeam)
        assertThat(actual, `is`(expected))
    }

    @Test
    fun to_StringToGameTeam_isCorrect() {
        val gameTeam = GameTeamFactory.getDefaultHomeTeam()
        val type = object : TypeToken<GameTeam>() {}.type
        val actual = converter.to(testGeneralGson.toJson(gameTeam, type))
        assertThat(actual, `is`(gameTeam))
    }
}
