package com.jiachian.nbatoday.data.repository.schedule

import com.jiachian.nbatoday.NBA_LEAGUE_ID
import com.jiachian.nbatoday.SCHEDULE_DATE_RANGE
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.data.local.datasource.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.data.local.datasource.game.GameLocalSource
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import com.jiachian.nbatoday.data.repository.team.TeamRepository
import com.jiachian.nbatoday.utils.NbaUtils
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first

class NbaScheduleRepository(
    private val gameLocalSource: GameLocalSource,
    private val boxScoreLocalSource: BoxScoreLocalSource,
    private val remoteDataSource: RemoteDataSource,
    private val dataStore: BaseDataStore,
    private val teamRepository: TeamRepository,
) : ScheduleRepository() {
    override suspend fun refreshSchedule() {
        try {
            isProgressingImp.value = true
            val schedule = remoteDataSource.getSchedule() ?: return
            val leagueSchedule = schedule.leagueSchedule ?: return
            val nbaGames = leagueSchedule.toNbaGames()

            val cal = NbaUtils.getCalendar()
            val todayYear = cal.get(Calendar.YEAR)
            val todayMonth = cal.get(Calendar.MONTH) + 1
            val todayDay = cal.get(Calendar.DAY_OF_MONTH)
            val today = NbaUtils.parseDate(todayYear, todayMonth, todayDay)?.time ?: 0L
            val recordDay = NbaUtils.parseDate(dataStore.recordScheduleToday.first())
            val record = recordDay?.time ?: 0L
            val betweenDays = TimeUnit.DAYS.convert(today - record, TimeUnit.MILLISECONDS) + 1
            val offset = betweenDays.toInt().coerceAtMost(SCHEDULE_DATE_RANGE)
            cal.add(Calendar.DAY_OF_MONTH, -offset)

            if (!gameLocalSource.existsGame()) {
                gameLocalSource.insertGames(nbaGames)
            } else {
                val filterGames = if (recordDay == null) {
                    nbaGames
                } else {
                    cal.add(Calendar.DAY_OF_MONTH, -1)
                    val current = cal.time
                    cal.add(Calendar.DAY_OF_MONTH, 1)
                    nbaGames.filter {
                        val gameDate = it.gameDateTime
                        gameDate.after(recordDay) && gameDate.before(current)
                    }
                }
                val updateGames = filterGames.map { it.toGameScoreUpdateData() }
                boxScoreLocalSource.updateGamesScore(updateGames)
            }

            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val scoreboards = remoteDataSource.getScoreboard(
                NBA_LEAGUE_ID,
                year,
                month,
                day,
                offset + SCHEDULE_DATE_RANGE + 1
            )
            scoreboards?.also {
                val update = scoreboards.map { it.toGameUpdateData() }
                update.forEach {
                    gameLocalSource.updateGames(it)
                }
            }
            dataStore.updateRecordScheduleToday(todayYear, todayMonth, todayDay)
            teamRepository.refreshTeamStats(null)
        } finally {
            isProgressingImp.value = false
        }
    }

    override suspend fun refreshSchedule(year: Int, month: Int, day: Int) {
        try {
            isProgressingImp.value = true
            val gameDate = NbaUtils.formatScoreboardGameDate(year, month, day)
            val scoreboard = remoteDataSource.getScoreboard(NBA_LEAGUE_ID, gameDate)
            val updateData = scoreboard?.toGameUpdateData()
            updateData?.also {
                gameLocalSource.updateGames(updateData)
            }
        } finally {
            isProgressingImp.value = false
        }
    }
}
