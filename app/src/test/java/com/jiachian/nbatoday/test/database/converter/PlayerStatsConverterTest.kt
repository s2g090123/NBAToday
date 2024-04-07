package com.jiachian.nbatoday.test.database.converter

import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.common.data.database.converter.PlayerStatsConverter
import com.jiachian.nbatoday.common.data.database.converter.typeAdapterGson
import com.jiachian.nbatoday.data.local.PlayerGenerator
import com.jiachian.nbatoday.player.data.model.local.Player
import com.jiachian.nbatoday.utils.assertIs
import org.junit.Test

class PlayerStatsConverterTest {
    private val converter = PlayerStatsConverter()

    @Test
    fun `from(home) expects correct`() {
        val player = PlayerGenerator.getHome().stats
        val gson = typeAdapterGson
        val type = object : TypeToken<Player.PlayerStats>() {}.type
        val expected = gson.toJson(player, type)
        val actual = converter.from(player)
        assertIs(actual, expected)
    }

    @Test
    fun `to(home) expects correct`() {
        val player = PlayerGenerator.getHome().stats
        val gson = typeAdapterGson
        val type = object : TypeToken<Player.PlayerStats>() {}.type
        val actual = converter.to(gson.toJson(player, type))
        assertIs(actual, player)
    }
}
