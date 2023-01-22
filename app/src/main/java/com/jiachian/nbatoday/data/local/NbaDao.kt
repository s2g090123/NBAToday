package com.jiachian.nbatoday.data.local

import androidx.room.*
import com.jiachian.nbatoday.data.local.player.PlayerCareer
import com.jiachian.nbatoday.data.local.player.PlayerCareerInfoUpdate
import com.jiachian.nbatoday.data.local.player.PlayerCareerStatsUpdate
import com.jiachian.nbatoday.data.local.player.PlayerStats
import com.jiachian.nbatoday.data.local.score.GameBoxScore
import com.jiachian.nbatoday.data.local.team.DefaultTeam
import com.jiachian.nbatoday.data.local.team.TeamStats
import com.jiachian.nbatoday.data.remote.game.GameScoreUpdateData
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

    @Query("SELECT * FROM nba_game WHERE game_date <= :from")
    fun getGamesBefore(from: Long): Flow<List<NbaGame>>

    @Query("SELECT * FROM nba_game WHERE game_date > :from")
    fun getGamesAfter(from: Long): Flow<List<NbaGame>>

    @Query("SELECT DISTINCT game_date FROM nba_game")
    fun getDates(): Flow<List<Date>>

    @Query("SELECT EXISTS (SELECT 1 FROM nba_game)")
    fun exitsGames(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<NbaGame>)

    @Update(entity = NbaGame::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGames(games: List<GameUpdateData>)

    @Update(entity = NbaGame::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGamesScore(games: List<GameScoreUpdateData>)

    /** Game Box Score */
    @Query("SELECT * FROM nba_game_box_score WHERE game_id == :gameId")
    fun getGameBoxScore(gameId: String): Flow<GameBoxScore?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameBoxScore(boxScore: GameBoxScore)

    /** Team and Player Stats */
    @Query("SELECT * FROM nba_team_stats")
    fun getTeamStats(): Flow<List<TeamStats>>

    @Query("SELECT * FROM nba_team_stats WHERE team_id == :teamId")
    fun getTeamAndPlayerStats(teamId: Int): Flow<TeamAndPlayers?>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM nba_team_stats b 
                WHERE b.win_percentage > a.win_percentage AND team_conference == :conference
            )
            FROM nba_team_stats a
            WHERE team_id == :teamId AND team_conference == :conference
            ORDER BY win_percentage
        """
    )
    fun getRank(teamId: Int, conference: DefaultTeam.Conference): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM nba_team_stats b 
                WHERE 
                    CASE
                        WHEN (b.points * 1.0 / b.game_played) == (a.points * 1.0 / a.game_played) THEN b.win_percentage > a.win_percentage
                        ELSE (b.points * 1.0 / b.game_played) > (a.points * 1.0 / a.game_played)
                    END
            )
            FROM nba_team_stats a
            WHERE team_id == :teamId
            ORDER BY points
        """
    )
    fun getPointsRank(teamId: Int): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM nba_team_stats b 
                WHERE 
                    CASE
                        WHEN (b.rebounds_total * 1.0 / b.game_played) == (a.rebounds_total * 1.0 / a.game_played) THEN b.win_percentage > a.win_percentage
                        ELSE (b.rebounds_total * 1.0 / b.game_played) > (a.rebounds_total * 1.0 / a.game_played)
                    END
            )
            FROM nba_team_stats a
            WHERE team_id == :teamId
            ORDER BY rebounds_total
        """
    )
    fun getReboundsRank(teamId: Int): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM nba_team_stats b 
                WHERE 
                    CASE
                        WHEN (b.assists * 1.0 / b.game_played) == (a.assists * 1.0 / a.game_played) THEN b.win_percentage > a.win_percentage
                        ELSE (b.assists * 1.0 / b.game_played) > (a.assists * 1.0 / a.game_played)
                    END
            )
            FROM nba_team_stats a
            WHERE team_id == :teamId
            ORDER BY assists
        """
    )
    fun getAssistsRank(teamId: Int): Flow<Int>

    @Query(
        """
            SELECT (
                SELECT COUNT(team_id) + 1
                FROM nba_team_stats b 
                WHERE 
                    CASE
                        WHEN b.plus_minus == a.plus_minus THEN b.win_percentage > a.win_percentage
                        ELSE b.plus_minus > a.plus_minus
                    END

            )
            FROM nba_team_stats a
            WHERE team_id == :teamId
            ORDER BY plus_minus
        """
    )
    fun getPlusMinusRank(teamId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTeamStats(stats: List<TeamStats>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTeamStats(stats: TeamStats)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayerStats(stats: List<PlayerStats>)

    /** Player */
    @Query("SELECT EXISTS (SELECT 1 FROM nba_player_career_stats WHERE person_id == :playerId)")
    fun exitsPlayer(playerId: Int): Boolean

    @Query("SELECT * FROM nba_player_career_stats WHERE person_id == :playerId")
    fun getPlayerCareer(playerId: Int): Flow<PlayerCareer?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerStats(stats: PlayerCareer)

    @Update(entity = PlayerCareer::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayerInfo(info: PlayerCareerInfoUpdate)

    @Update(entity = PlayerCareer::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayerStats(stats: PlayerCareerStatsUpdate)
}