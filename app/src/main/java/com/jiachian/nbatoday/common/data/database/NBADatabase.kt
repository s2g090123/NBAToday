package com.jiachian.nbatoday.common.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jiachian.nbatoday.bet.data.BetDao
import com.jiachian.nbatoday.bet.data.model.local.Bet
import com.jiachian.nbatoday.boxscore.data.BoxScoreDao
import com.jiachian.nbatoday.boxscore.data.model.local.BoxScore
import com.jiachian.nbatoday.common.data.database.converter.BoxScoreTeamConverter
import com.jiachian.nbatoday.common.data.database.converter.ConferenceConverter
import com.jiachian.nbatoday.common.data.database.converter.DateConverter
import com.jiachian.nbatoday.common.data.database.converter.GameLeadersConverter
import com.jiachian.nbatoday.common.data.database.converter.GameTeamConverter
import com.jiachian.nbatoday.common.data.database.converter.NBATeamConverter
import com.jiachian.nbatoday.common.data.database.converter.PlayerInfoConverter
import com.jiachian.nbatoday.common.data.database.converter.PlayerStatsConverter
import com.jiachian.nbatoday.common.data.database.converter.PointsLeaderConverter
import com.jiachian.nbatoday.game.data.GameDao
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.player.data.PlayerDao
import com.jiachian.nbatoday.player.data.model.local.Player
import com.jiachian.nbatoday.team.data.TeamDao
import com.jiachian.nbatoday.team.data.model.local.Team
import com.jiachian.nbatoday.team.data.model.local.TeamPlayer

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
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): NBADatabase {
            return Room.databaseBuilder(
                context,
                NBADatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }

    abstract fun getGameDao(): GameDao
    abstract fun getBoxScoreDao(): BoxScoreDao
    abstract fun getTeamDao(): TeamDao
    abstract fun getPlayerDao(): PlayerDao
    abstract fun getBetDao(): BetDao
}
