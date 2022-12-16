package com.jiachian.nbatoday.data.local.converter

import androidx.room.TypeConverter
import com.jiachian.nbatoday.data.remote.game.GameStatusCode

class GameStatusCodeConverter {
    @TypeConverter
    fun to(value: Int): GameStatusCode {
        return when (value) {
            GameStatusCode.COMING_SOON.status -> GameStatusCode.COMING_SOON
            GameStatusCode.PLAYING.status -> GameStatusCode.PLAYING
            GameStatusCode.FINAL.status -> GameStatusCode.FINAL
            else -> GameStatusCode.COMING_SOON
        }
    }

    @TypeConverter
    fun from(value: GameStatusCode): Int {
        return value.status
    }
}