package com.jiachian.nbatoday.test.database.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.common.data.database.converter.NBATeamConverter
import com.jiachian.nbatoday.common.data.database.converter.typeAdapterGson
import com.jiachian.nbatoday.data.local.NBATeamGenerator
import com.jiachian.nbatoday.rule.NBATeamRule
import com.jiachian.nbatoday.team.data.model.local.teams.NBATeam
import com.jiachian.nbatoday.utils.assertIs
import org.junit.Rule
import org.junit.Test

class NBATeamConverterTest {
    private val converter = NBATeamConverter()

    @get:Rule
    val nbaTeamRule = NBATeamRule()

    @Test
    fun `from(home) expects correct`() {
        val team = NBATeamGenerator.getHome()
        val gson = typeAdapterGson
        val type = object : TypeToken<NBATeam>() {}.type
        val expected = gson.toJson(team, type)
        val actual = converter.from(team)
        assertIs(actual, expected)
    }

    @Test
    fun `to(home) expects correct`() {
        val team = NBATeamGenerator.getHome()
        val gson = typeAdapterGson
        val type = object : TypeToken<NBATeam>() {}.type
        val actual = converter.to(gson.toJson(team, type))
        assertIs(actual, team)
    }
}
