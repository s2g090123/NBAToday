package com.jiachian.nbatoday.data.local

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jiachian.nbatoday.data.local.bet.Bets
import com.jiachian.nbatoday.data.local.converter.BoxScoreTeamConverter
import com.jiachian.nbatoday.data.local.converter.DateConverter
import com.jiachian.nbatoday.data.local.converter.GameLeadersConverter
import com.jiachian.nbatoday.data.local.converter.GameTeamConverter
import com.jiachian.nbatoday.data.local.converter.NBATeamConverter
import com.jiachian.nbatoday.data.local.converter.PlayerCareerInfoConverter
import com.jiachian.nbatoday.data.local.converter.PlayerCareerStatsConverter
import com.jiachian.nbatoday.data.local.converter.PointsLeaderConverter
import com.jiachian.nbatoday.data.local.converter.TeamConferenceConverter
import com.jiachian.nbatoday.data.local.converter.TeamDivisionConverter
import com.jiachian.nbatoday.data.local.dao.BetDao
import com.jiachian.nbatoday.data.local.dao.BoxScoreDao
import com.jiachian.nbatoday.data.local.dao.GameDao
import com.jiachian.nbatoday.data.local.dao.PlayerDao
import com.jiachian.nbatoday.data.local.dao.TeamDao
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
    NBATeamConverter::class,
    DateConverter::class
)
abstract class NbaDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "nba_database"

        @Volatile
        private var instance: NbaDatabase? = null

        fun getInstance(context: Context): NbaDatabase {
            return instance ?: synchronized(this) {
                buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): NbaDatabase {
            return Room.databaseBuilder(context, NbaDatabase::class.java, DATABASE_NAME).build()
        }

        @VisibleForTesting
        fun reset() {
            instance = null
        }
    }

    abstract fun getGameDao(): GameDao
    abstract fun getBoxScoreDao(): BoxScoreDao
    abstract fun getTeamDao(): TeamDao
    abstract fun getPlayerDao(): PlayerDao
    abstract fun getBetDao(): BetDao
}
