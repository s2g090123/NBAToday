package com.jiachian.nbatoday.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jiachian.nbatoday.data.local.converter.*

@Database(
    entities = [NbaGame::class],
    version = 1
)
@TypeConverters(
    GameTeamConverter::class,
    PointsLeaderConverter::class,
    GameLeaderConverter::class,
    GameLeadersConverter::class,
    GameStatusCodeConverter::class,
    DateConverter::class
)
abstract class NbaDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "nba_database"

        @Volatile
        private var instance: NbaDatabase? = null

        fun getInstance(context: Context): NbaDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): NbaDatabase {
            return Room.databaseBuilder(context, NbaDatabase::class.java, DATABASE_NAME).build()
        }
    }

    abstract fun getNbaDao(): NbaDao
}