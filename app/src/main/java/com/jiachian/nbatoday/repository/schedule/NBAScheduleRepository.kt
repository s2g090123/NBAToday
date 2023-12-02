package com.jiachian.nbatoday.repository.schedule

import com.jiachian.nbatoday.NbaLeagueId
import com.jiachian.nbatoday.ScheduleDateRange
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.models.local.game.Game
import com.jiachian.nbatoday.models.local.game.GameUpdateData
import com.jiachian.nbatoday.models.local.game.toGameScoreUpdateData
import com.jiachian.nbatoday.models.remote.game.toGameUpdateData
import com.jiachian.nbatoday.models.remote.game.toGames
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.utils.NbaUtils
import com.jiachian.nbatoday.utils.showErrorToast
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.first

class NBAScheduleRepository(
    private val gameLocalSource: GameLocalSource,
    private val gameRemoteSource: GameRemoteSource,
    private val dataStore: BaseDataStore,
    private val teamRepository: TeamRepository,
) : ScheduleRepository() {
    override suspend fun updateSchedule() {
        loading {
            getGames()
                ?.let { games ->
                    val cal = getCalendarWithScheduleFirstDate()
                    updateGames(games, cal.time)
                    getGamesUpdateData(cal)?.let { updatedGames ->
                        updatedGames.forEach { gameUpdateData ->
                            gameLocalSource.updateGames(gameUpdateData)
                        }
                        updateLastAccessedDay()
                        teamRepository.updateTeamStats()
                    }
                }
                ?: showErrorToast()
        }
    }

    override suspend fun updateSchedule(year: Int, month: Int, day: Int) {
        loading {
            val gameDate = NbaUtils.formatScoreboardGameDate(year, month, day)
            gameRemoteSource.getGame(NbaLeagueId, gameDate)
                .takeIf { !it.isError() }
                ?.body()
                ?.scoreboard
                ?.toGameUpdateData()
                ?.let { gamesUpdateData ->
                    gameLocalSource.updateGames(gamesUpdateData)
                }
                ?: showErrorToast()
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
                NbaUtils.getCalendar().time.time - cal.time.time,
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
            NbaUtils.getCalendar().apply {
                val todayMs = NbaUtils.parseDate(
                    get(Calendar.YEAR),
                    get(Calendar.MONTH) + 1,
                    get(Calendar.DAY_OF_MONTH)
                )?.time ?: 0L
                val lastAccessedMs = NbaUtils.parseDate(
                    dataStore.lastAccessedDay.first()
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
            val gameExists = gameLocalSource.existsGame()
            val updatedGames = if (!gameExists) {
                games
            } else {
                val before = Date(currentDate.time - TimeUnit.DAYS.toMillis(1))
                val after = NbaUtils.parseDate(dataStore.lastAccessedDay.first()) ?: Date(0)
                games.filter { game ->
                    val gameDate = game.gameDateTime
                    gameDate.after(after) && gameDate.before(before)
                }
            }.map { game ->
                game.toGameScoreUpdateData()
            }
            gameLocalSource.updateGamesScore(updatedGames)
        }
    }

    private suspend fun updateLastAccessedDay() {
        loading {
            NbaUtils.getCalendar().apply {
                dataStore.updateLastAccessedDay(
                    get(Calendar.YEAR),
                    get(Calendar.MONTH) + 1,
                    get(Calendar.DAY_OF_MONTH)
                )
            }
        }
    }
}
