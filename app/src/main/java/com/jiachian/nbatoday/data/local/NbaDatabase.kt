package com.jiachian.nbatoday.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.converter.*
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.TeamStats

@Database(
    entities = [NbaGame::class, GameBoxScore::class, TeamStats::class, PlayerStats::class, PlayerCareer::class, Bets::class],
    version = 1
)
@TypeConverters(
    GameTeamConverter::class,
    PointsLeaderConverter::class,
    GameLeadersConverter::class,
    BoxScoreTeamConverter::class,
    TeamConferenceConverter::class,
    TeamDivisionConverter::class,
    PlayerCareerInfoConverter::class,
    PlayerCareerStatsConverter::class,
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