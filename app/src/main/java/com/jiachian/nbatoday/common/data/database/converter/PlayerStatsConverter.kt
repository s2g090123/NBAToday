package com.jiachian.nbatoday.common.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jiachian.nbatoday.player.data.model.local.Player

class PlayerStatsConverter(private val gson: Gson = typeAdapterGson) {

    private val type = object : TypeToken<Player.PlayerStats>() {}.type

    @TypeConverter
    fun from(value: Player.PlayerStats): String {
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun to(value: String): Player.PlayerStats {
        return gson.fromJson(value, type)
    }
}
