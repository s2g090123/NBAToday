package com.itrustmachines.nbatoday.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itrustmachines.nbatoday.data.local.converter.AwayTeamConverter
import com.itrustmachines.nbatoday.data.local.converter.GameDateConverter
import com.itrustmachines.nbatoday.data.local.converter.HomeTeamConverter
import com.itrustmachines.nbatoday.data.local.converter.PointsLeaderConverter

@Database(
    entities = [NbaGame::class],
    version = 1
)
@TypeConverters(
    GameDateConverter::class,
    AwayTeamConverter::class,
    HomeTeamConverter::class,
    PointsLeaderConverter::class
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