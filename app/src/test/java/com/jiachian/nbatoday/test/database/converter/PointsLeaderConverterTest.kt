package com.jiachian.nbatoday.test.database.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.common.data.database.converter.PointsLeaderConverter
import com.jiachian.nbatoday.common.data.database.converter.typeAdapterGson
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.utils.assertIs
import org.junit.Test

class PointsLeaderConverterTest {
    private val converter = PointsLeaderConverter()

    @Test
    fun `from(final) expects correct`() {
        val leaders = GameGenerator.getFinal().pointsLeaders
        val gson = typeAdapterGson
        val type = object : TypeToken<List<Game.PointsLeader>>() {}.type
        val expected = gson.toJson(leaders, type)
        val actual = converter.from(leaders)
        assertIs(actual, expected)
    }

    @Test
    fun `to(final) expects correct`() {
        val leaders = GameGenerator.getFinal().pointsLeaders
        val gson = typeAdapterGson
        val type = object : TypeToken<List<Game.PointsLeader>>() {}.type
        val actual = converter.to(gson.toJson(leaders, type))
        assertIs(actual, leaders)
    }
}
