package com.jiachian.nbatoday.data.local

import androidx.room.*
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameUpdateData
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface NbaDao {

    /** NBA Game */
    @Query("SELECT * FROM nba_game")
    fun getGames(): Flow<List<NbaGame>>

    @Query("SELECT * FROM nba_game WHERE game_date >= :from AND game_date <= :to")
    fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>>

    @Query("SELECT DISTINCT game_date FROM nba_game")
    fun getDates(): Flow<List<Date>>

    @Query("SELECT EXISTS (SELECT 1 FROM nba_game)")
    fun exitsData(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<NbaGame>)

    @Update(entity = NbaGame::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGame(status: List<GameUpdateData>)

    /** Game Box Score */
    @Query("SELECT * FROM nba_game_box_score WHERE game_id == :gameId")
    fun getGameBoxScore(gameId: String): Flow<GameBoxScore?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameBoxScore(boxScore: GameBoxScore)

    /** Team Stats */
    @Query("SELECT * FROM nba_team_stats")
    fun getTeamStats(): Flow<List<TeamStats>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTeamStats(stats: List<TeamStats>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTeamStats(stats: TeamStats)
}