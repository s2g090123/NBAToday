package com.jiachian.nbatoday.data

import com.jiachian.nbatoday.NBA_LEAGUE_ID
import com.jiachian.nbatoday.SCHEDULE_DATE_RANGE
import com.jiachian.nbatoday.data.local.LocalDataSource
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import com.jiachian.nbatoday.utils.NbaUtils
import kotlinx.coroutines.flow.Flow
import java.util.*

class NbaRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : BaseRepository {

    override val dates: Flow<List<Date>> = localDataSource.dates
    override val games: Flow<List<NbaGame>> = localDataSource.games

    override suspend fun refreshSchedule() {
        val schedule = remoteDataSource.getSchedule() ?: return
        val leagueSchedule = schedule.leagueSchedule ?: return
        if (!localDataSource.existsData()) {
            val nbaGames = leagueSchedule.toNbaGames()
            localDataSource.insertGames(nbaGames)
        }
        val cal = NbaUtils.getCalendar()
        cal.add(Calendar.DAY_OF_MONTH, -SCHEDULE_DATE_RANGE)
        repeat(SCHEDULE_DATE_RANGE * 2 + 1) {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val gameDate = NbaUtils.formatScoreboardGameDate(year, month, day)
            val scoreboard = remoteDataSource.getScoreboard(NBA_LEAGUE_ID, gameDate)
            val updateData = scoreboard?.toGameUpdateData()
            if (updateData != null) {
                localDataSource.updateGame(updateData)
            }
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    override fun getGamesDuring(from: Long, to: Long): Flow<List<NbaGame>> {
        return localDataSource.getGamesDuring(from, to)
    }
}