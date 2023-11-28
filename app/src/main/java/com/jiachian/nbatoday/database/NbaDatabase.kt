package com.jiachian.nbatoday.database

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jiachian.nbatoday.converter.BoxScoreTeamConverter
import com.jiachian.nbatoday.converter.DateConverter
import com.jiachian.nbatoday.converter.GameLeadersConverter
import com.jiachian.nbatoday.converter.GameTeamConverter
import com.jiachian.nbatoday.converter.NBATeamConverter
import com.jiachian.nbatoday.converter.PlayerCareerInfoConverter
import com.jiachian.nbatoday.converter.PlayerCareerStatsConverter
import com.jiachian.nbatoday.converter.PointsLeaderConverter
import com.jiachian.nbatoday.converter.TeamConferenceConverter
import com.jiachian.nbatoday.database.dao.BetDao
import com.jiachian.nbatoday.database.dao.BoxScoreDao
import com.jiachian.nbatoday.database.dao.GameDao
import com.jiachian.nbatoday.database.dao.PlayerDao
import com.jiachian.nbatoday.database.dao.TeamDao
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.player.PlayerCareer
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.team.TeamPlayerStats
import com.jiachian.nbatoday.models.local.team.TeamStats

@Database(
    entities = [
        Game::class,
        BoxScore::class,
        TeamStats::class,
        TeamPlayerStats::class,
        PlayerCareer::class,
        Bet::class
    ],
    version = 1
)
@TypeConverters(
    GameTeamConverter::class,
    PointsLeaderConverter::class,
    GameLeadersConverter::class,
    BoxScoreTeamConverter::class,
    TeamConferenceConverter::class,
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
