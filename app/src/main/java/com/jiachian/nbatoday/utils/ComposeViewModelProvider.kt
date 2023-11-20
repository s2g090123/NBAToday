package com.jiachian.nbatoday.utils

import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.calendar.CalendarViewModel
import com.jiachian.nbatoday.compose.screen.home.HomeViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.data.BaseRepository
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.data.local.NbaGame
import com.jiachian.nbatoday.data.local.team.NBATeam
import java.util.Date

class ComposeViewModelProvider(
    private val repository: BaseRepository,
    private val dataStore: BaseDataStore,
    private val screenStateHelper: ScreenStateHelper,
) {
    fun getHomeViewModel(): HomeViewModel {
        return HomeViewModel(
            repository = repository,
            dataStore = dataStore,
            screenStateHelper = screenStateHelper,
        )
    }

    fun getBoxScoreViewModel(game: NbaGame): BoxScoreViewModel {
        return BoxScoreViewModel(
            game = game,
            repository = repository,
            screenStateHelper = screenStateHelper,
        )
    }

    fun getTeamViewModel(team: NBATeam): TeamViewModel {
        return TeamViewModel(
            team = team,
            repository = repository,
            screenStateHelper = screenStateHelper,
        )
    }

    fun getPlayerViewModel(playerId: Int): PlayerViewModel {
        return PlayerViewModel(
            playerId = playerId,
            repository = repository,
        )
    }

    fun getCalendarViewModel(date: Date): CalendarViewModel {
        return CalendarViewModel(
            date = date,
            repository = repository,
            screenStateHelper = screenStateHelper,
        )
    }

    fun getBetViewModel(account: String): BetViewModel {
        return BetViewModel(
            account = account,
            repository = repository,
            screenStateHelper = screenStateHelper
        )
    }
}
