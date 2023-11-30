package com.jiachian.nbatoday.repository.schedule

import com.jiachian.nbatoday.NbaLeagueId
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.models.local.game.toGameScoreUpdateData
import com.jiachian.nbatoday.models.remote.game.toGameUpdateData
import com.jiachian.nbatoday.models.remote.game.toGames
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.utils.NbaUtils
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first

class NbaScheduleRepository(
    private val gameLocalSource: GameLocalSource,
    private val gameRemoteSource: GameRemoteSource,
    private val dataStore: BaseDataStore,
    private val teamRepository: TeamRepository,
) : ScheduleRepository() {
    override suspend fun refreshSchedule() {
        try {
            isProgressingImp.value = true
            val schedule = gameRemoteSource.getSchedule() ?: return
            val leagueSchedule = schedule.leagueSchedule ?: return
            val nbaGames = leagueSchedule.toGames()

            val cal = NbaUtils.getCalendar()
            val todayYear = cal.get(Calendar.YEAR)
            val todayMonth = cal.get(Calendar.MONTH) + 1
            val todayDay = cal.get(Calendar.DAY_OF_MONTH)
            val today = NbaUtils.parseDate(todayYear, todayMonth, todayDay)?.time ?: 0L
            val recordDay = NbaUtils.parseDate(dataStore.lastAccessedDay.first())
            val record = recordDay?.time ?: 0L
            val betweenDays = TimeUnit.DAYS.convert(today - record, TimeUnit.MILLISECONDS) + 1
            val offset = betweenDays.toInt().coerceAtMost(ScheduleDateRange)
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
                gameLocalSource.updateGamesScore(updateGames)
            }

            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH) + 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val scoreboards = gameRemoteSource.getScoreboard(
                NbaLeagueId,
                year,
                month,
                day,
                offset + ScheduleDateRange + 1
            )
            scoreboards?.also {
                val update = scoreboards.mapNotNull { it.scoreboard?.toGameUpdateData() }
                update.forEach {
                    gameLocalSource.updateGames(it)
                }
            }
            dataStore.updateLastAccessedDay(todayYear, todayMonth, todayDay)
            teamRepository.refreshTeamStats(null)
        } finally {
            isProgressingImp.value = false
        }
    }

    override suspend fun refreshSchedule(year: Int, month: Int, day: Int) {
        try {
            isProgressingImp.value = true
            val gameDate = NbaUtils.formatScoreboardGameDate(year, month, day)
            val scoreboard = gameRemoteSource.getScoreboard(NbaLeagueId, gameDate)
            val updateData = scoreboard?.scoreboard?.toGameUpdateData()
            updateData?.also {
                gameLocalSource.updateGames(updateData)
            }
        } finally {
            isProgressingImp.value = false
        }
    }
}
