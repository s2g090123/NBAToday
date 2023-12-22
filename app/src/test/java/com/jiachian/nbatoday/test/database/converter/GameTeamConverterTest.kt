package com.jiachian.nbatoday.test.database.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.data.local.GameTeamGenerator
import com.jiachian.nbatoday.database.converter.GameTeamConverter
import com.jiachian.nbatoday.database.converter.typeAdapterGson
import com.jiachian.nbatoday.models.local.game.GameTeam
import com.jiachian.nbatoday.utils.assertIs
import org.junit.Test

class GameTeamConverterTest {
    private val converter = GameTeamConverter()

    @Test
    fun `from(home) expects correct`() {
        val team = GameTeamGenerator.getHome()
        val gson = typeAdapterGson
        val type = object : TypeToken<GameTeam>() {}.type
        val expected = gson.toJson(team, type)
        val actual = converter.from(team)
        assertIs(actual, expected)
    }

    @Test
    fun `to(home) expects correct`() {
        val team = GameTeamGenerator.getHome()
        val gson = typeAdapterGson
        val type = object : TypeToken<GameTeam>() {}.type
        val actual = converter.to(gson.toJson(team, type))
        assertIs(actual, team)
    }
}
