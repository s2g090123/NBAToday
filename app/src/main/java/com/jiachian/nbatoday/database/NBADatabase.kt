package com.jiachian.nbatoday.database

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jiachian.nbatoday.database.converter.BoxScoreTeamConverter
import com.jiachian.nbatoday.database.converter.ConferenceConverter
import com.jiachian.nbatoday.database.converter.DateConverter
import com.jiachian.nbatoday.database.converter.GameLeadersConverter
import com.jiachian.nbatoday.database.converter.GameTeamConverter
import com.jiachian.nbatoday.database.converter.NBATeamConverter
import com.jiachian.nbatoday.database.converter.PlayerInfoConverter
import com.jiachian.nbatoday.database.converter.PlayerStatsConverter
import com.jiachian.nbatoday.database.converter.PointsLeaderConverter
import com.jiachian.nbatoday.database.dao.BetDao
import com.jiachian.nbatoday.database.dao.BoxScoreDao
import com.jiachian.nbatoday.database.dao.GameDao
import com.jiachian.nbatoday.database.dao.PlayerDao
import com.jiachian.nbatoday.database.dao.TeamDao
import com.jiachian.nbatoday.models.local.bet.Bet
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.player.Player
import com.jiachian.nbatoday.models.local.score.BoxScore
import com.jiachian.nbatoday.models.local.team.Team
import com.jiachian.nbatoday.models.local.team.TeamPlayer

@Database(
    entities = [
        Game::class,
        BoxScore::class,
        Team::class,
        TeamPlayer::class,
        Player::class,
        Bet::class
    ],
    version = 1
)
@TypeConverters(
    GameTeamConverter::class,
    PointsLeaderConverter::class,
    GameLeadersConverter::class,
    BoxScoreTeamConverter::class,
    ConferenceConverter::class,
    PlayerInfoConverter::class,
    PlayerStatsConverter::class,
    NBATeamConverter::class,
    DateConverter::class
)
abstract class NBADatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "nba_database"

        @Volatile
        private var instance: NBADatabase? = null

        fun getInstance(context: Context): NBADatabase {
            return instance ?: synchronized(this) {
                buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): NBADatabase {
            return Room.databaseBuilder(context, NBADatabase::class.java, DATABASE_NAME).build()
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
