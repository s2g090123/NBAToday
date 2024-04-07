package com.jiachian.nbatoday.test.database.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScore
import com.jiachian.nbatoday.common.data.database.converter.BoxScoreTeamConverter
import com.jiachian.nbatoday.common.data.database.converter.typeAdapterGson
import com.jiachian.nbatoday.data.local.BoxScoreGenerator
import com.jiachian.nbatoday.utils.assertIs
import org.junit.Test

class BoxScoreTeamConverterTest {
    private val converter = BoxScoreTeamConverter()

    @Test
    fun `from(home) expects correct`() {
        val team = BoxScoreGenerator.getFinal().homeTeam
        val gson = typeAdapterGson
        val type = object : TypeToken<BoxScore.BoxScoreTeam>() {}.type
        val expected = gson.toJson(team, type)
        val actual = converter.from(team)
        assertIs(actual, expected)
    }

    @Test
    fun `to(home) expects correct`() {
        val team = BoxScoreGenerator.getFinal().homeTeam
        val gson = typeAdapterGson
        val type = object : TypeToken<BoxScore.BoxScoreTeam>() {}.type
        val actual = converter.to(gson.toJson(team, type))
        assertIs(actual, team)
    }
}
