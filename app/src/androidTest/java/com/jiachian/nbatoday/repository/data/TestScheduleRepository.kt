package com.jiachian.nbatoday.repository.data

import com.jiachian.nbatoday.NBALeagueId
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import com.jiachian.nbatoday.models.local.game.toGameScoreUpdateData
import com.jiachian.nbatoday.models.remote.game.extensions.toGameUpdateData
import com.jiachian.nbatoday.models.remote.game.extensions.toGames
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.utils.DateUtils
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first

class TestScheduleRepository(
    private val gameLocalSource: GameLocalSource,
    private val gameRemoteSource: GameRemoteSource,
    private val dataStore: BaseDataStore,
    private val teamRepository: TeamRepository,
) : ScheduleRepository() {
    override suspend fun updateSchedule() {
        getGames()
            ?.let { games ->
                val cal = getCalendarWithScheduleFirstDate()
                updateGames(games, cal.time)
                getGamesUpdateData(cal)?.let { updatedGames ->
                    updatedGames.forEach { gameUpdateData ->
                        gameLocalSource.updateGames(gameUpdateData)
                    }
                    updateLastAccessedDay()
                    teamRepository.insertTeams()
                }
            }
    }

    override suspend fun updateSchedule(year: Int, month: Int, day: Int) {
        val gameDate = DateUtils.formatScoreboardGameDate(year, month, day)
        gameRemoteSource.getGame(NBALeagueId, gameDate)
            .takeIf { !it.isError() }
            ?.body()
            ?.scoreboard
            ?.toGameUpdateData()
            ?.let { gamesUpdateData ->
                gameLocalSource.updateGames(gamesUpdateData)
            }
    }

    private suspend fun getGames(): List<Game>? {
        return loading {
            gameRemoteSource.getSchedule()
                .takeIf { !it.isError() }
                ?.body()
                ?.leagueSchedule
                ?.toGames()
        }
    }

    private suspend fun getGamesUpdateData(cal: Calendar): List<List<GameUpdateData>>? {
        return loading {
            val difference = TimeUnit.DAYS.convert(
                DateUtils.getCalendar().time.time - cal.time.time,
                TimeUnit.MILLISECONDS
            ).toInt()
            gameRemoteSource.getGames(
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
    }

    private suspend fun getCalendarWithScheduleFirstDate(): Calendar {
        return loading {
            DateUtils.getCalendar().apply {
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
    }

    private suspend fun updateGames(games: List<Game>, currentDate: Date) {
        loading {
            when (gameLocalSource.gameExists()) {
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
                            gameLocalSource.updateGameScores(updatedGames)
                        }
                }
                false -> {
                    gameLocalSource.insertGames(games)
                }
            }
        }
    }

    private suspend fun updateLastAccessedDay() {
        loading {
            DateUtils.getCalendar().apply {
                dataStore.updateLastAccessedDate(
                    get(Calendar.YEAR),
                    get(Calendar.MONTH) + 1,
                    get(Calendar.DAY_OF_MONTH)
                )
            }
        }
    }
}
