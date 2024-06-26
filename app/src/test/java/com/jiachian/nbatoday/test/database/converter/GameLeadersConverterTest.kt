package com.jiachian.nbatoday.test.database.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.common.data.database.converter.GameLeadersConverter
import com.jiachian.nbatoday.common.data.database.converter.typeAdapterGson
import com.jiachian.nbatoday.data.local.GameGenerator
import com.jiachian.nbatoday.game.data.model.local.GameLeaders
import com.jiachian.nbatoday.utils.assertIs
import org.junit.Test

class GameLeadersConverterTest {
    private val converter = GameLeadersConverter()

    @Test
    fun `from(final) expects correct`() {
        val leaders = GameGenerator.getFinal().gameLeaders
        val gson = typeAdapterGson
        val type = object : TypeToken<GameLeaders>() {}.type
        val expected = gson.toJson(leaders, type)
        val actual = converter.from(leaders)
        assertIs(actual, expected)
    }

    @Test
    fun `to(final) expects correct`() {
        val leaders = GameGenerator.getFinal().gameLeaders
        val gson = typeAdapterGson
        val type = object : TypeToken<GameLeaders>() {}.type
        val actual = converter.to(gson.toJson(leaders, type))
        assertIs(actual, leaders)
    }
}
