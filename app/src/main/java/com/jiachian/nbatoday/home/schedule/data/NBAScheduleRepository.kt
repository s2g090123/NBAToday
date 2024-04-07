package com.jiachian.nbatoday.home.schedule.data

import com.jiachian.nbatoday.common.data.NBALeagueId
import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.data.ScheduleDateRange
import com.jiachian.nbatoday.common.data.datastore.BaseDataStore
import com.jiachian.nbatoday.game.data.GameDao
import com.jiachian.nbatoday.game.data.GameService
import com.jiachian.nbatoday.game.data.model.local.Game
import com.jiachian.nbatoday.game.data.model.local.GameUpdateData
import com.jiachian.nbatoday.game.data.model.toGameScoreUpdateData
import com.jiachian.nbatoday.game.data.model.toGameUpdateData
import com.jiachian.nbatoday.game.data.model.toGames
import com.jiachian.nbatoday.utils.DateUtils
import com.jiachian.nbatoday.utils.isError
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first

class NBAScheduleRepository(
    private val dao: GameDao,
    private val service: GameService,
    private val dataStore: BaseDataStore,
) : ScheduleRepository {
    override suspend fun updateSchedule(): Response<Unit> {
        return getGames()
            ?.let { games ->
                val cal = getCalendarWithScheduleFirstDate()
                updateGames(games, cal.time)
                getGamesUpdateData(cal)?.let { updatedGames ->
                    updatedGames.forEach { gameUpdateData ->
                        dao.updateGames(gameUpdateData)
                    }
                    updateLastAccessedDay()
                }
            }
            ?.let { Response.Success(Unit) }
            ?: Response.Error()
    }

    override suspend fun updateSchedule(year: Int, month: Int, day: Int): Response<Unit> {
        val gameDate = DateUtils.formatScoreboardGameDate(year, month, day)
        return service.getGame(NBALeagueId, gameDate)
            .takeIf { !it.isError() }
            ?.body()
            ?.scoreboard
            ?.toGameUpdateData()
            ?.also { gamesUpdateData ->
                dao.updateGames(gamesUpdateData)
            }
            ?.let { Response.Success(Unit) }
            ?: Response.Error()
    }

    private suspend fun getGames(): List<Game>? {
        return service
            .getSchedule()
            .takeIf { !it.isError() }
            ?.body()
            ?.leagueSchedule
            ?.toGames()
    }

    private suspend fun getGamesUpdateData(cal: Calendar): List<List<GameUpdateData>>? {
        val difference = TimeUnit.DAYS.convert(
            DateUtils.getCalendar().time.time - cal.time.time,
            TimeUnit.MILLISECONDS
        ).toInt()
        return service.getGames(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH),
            difference + ScheduleDateRange + 1
        )
            .takeIf { !it.isError() }
            ?.body()
            ?.mapNotNull { remoteGame ->
                remoteGame.scoreboard?.toGameUpdateData()
            }
    }

    private suspend fun getCalendarWithScheduleFirstDate(): Calendar {
        return DateUtils.getCalendar().apply {
            val todayMs = DateUtils.parseDate(
                get(Calendar.YEAR),
                get(Calendar.MONTH) + 1,
                get(Calendar.DAY_OF_MONTH)
            )?.time ?: 0L
            val lastAccessedMs = DateUtils.parseDate(
                dataStore.lastAccessedDate.first()
            )?.time ?: 0L
            val differenceDays = TimeUnit.DAYS.convert(
                todayMs - lastAccessedMs,
                TimeUnit.MILLISECONDS
            ) + 1
            val offset = differenceDays.toInt().coerceAtMost(ScheduleDateRange)
            add(Calendar.DAY_OF_MONTH, -offset)
        }
    }

    private suspend fun updateGames(games: List<Game>, currentDate: Date) {
        when (dao.gameExists()) {
            true -> {
                val before = Date(currentDate.time - TimeUnit.DAYS.toMillis(1))
                val after = DateUtils.parseDate(dataStore.lastAccessedDate.first()) ?: Date(0)
                games
                    .filter { game ->
                        val gameDate = game.gameDateTime
                        gameDate.after(after) && gameDate.before(before)
                    }
                    .map { game ->
                        game.toGameScoreUpdateData()
                    }
                    .also { updatedGames ->
                        dao.updateGameScores(updatedGames)
                    }
            }
            false -> {
                dao.addGames(games)
            }
        }
    }

    private suspend fun updateLastAccessedDay() {
        DateUtils.getCalendar().apply {
            dataStore.updateLastAccessedDate(
                get(Calendar.YEAR),
                get(Calendar.MONTH) + 1,
                get(Calendar.DAY_OF_MONTH)
            )
        }
    }
}
