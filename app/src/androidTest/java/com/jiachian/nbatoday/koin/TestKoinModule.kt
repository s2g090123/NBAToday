package com.jiachian.nbatoday.koin

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.bet.data.BetDao
import com.jiachian.nbatoday.bet.data.BetRepository
import com.jiachian.nbatoday.bet.ui.main.BetViewModel
import com.jiachian.nbatoday.boxscore.data.BoxScoreDao
import com.jiachian.nbatoday.boxscore.ui.main.BoxScoreViewModel
import com.jiachian.nbatoday.calendar.ui.CalendarViewModel
import com.jiachian.nbatoday.common.data.datastore.BaseDataStore
import com.jiachian.nbatoday.database.dao.TestBetDao
import com.jiachian.nbatoday.database.dao.TestBoxScoreDao
import com.jiachian.nbatoday.database.dao.TestGameDao
import com.jiachian.nbatoday.database.dao.TestPlayerDao
import com.jiachian.nbatoday.database.dao.TestTeamDao
import com.jiachian.nbatoday.datasource.local.bet.BetLocalSource
import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.datasource.local.data.TestBetLocalSource
import com.jiachian.nbatoday.datasource.local.data.TestBoxScoreLocalSource
import com.jiachian.nbatoday.datasource.local.data.TestGameLocalSource
import com.jiachian.nbatoday.datasource.local.data.TestPlayerLocalSource
import com.jiachian.nbatoday.datasource.local.data.TestTeamLocalSource
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.local.player.PlayerLocalSource
import com.jiachian.nbatoday.datasource.local.team.TeamLocalSource
import com.jiachian.nbatoday.datasource.remote.data.TestGameRemoteSource
import com.jiachian.nbatoday.datasource.remote.data.TestPlayerRemoteSource
import com.jiachian.nbatoday.datasource.remote.data.TestTeamRemoteSource
import com.jiachian.nbatoday.datasource.remote.data.TestUserRemoteSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.datasource.remote.player.PlayerRemoteSource
import com.jiachian.nbatoday.datasource.remote.team.TeamRemoteSource
import com.jiachian.nbatoday.datasource.remote.user.UserRemoteSource
import com.jiachian.nbatoday.datastore.data.TestDataStore
import com.jiachian.nbatoday.game.data.GameDao
import com.jiachian.nbatoday.game.data.GameRepository
import com.jiachian.nbatoday.home.schedule.data.ScheduleRepository
import com.jiachian.nbatoday.home.schedule.ui.SchedulePageViewModel
import com.jiachian.nbatoday.home.standing.ui.StandingPageViewModel
import com.jiachian.nbatoday.home.user.data.UserRepository
import com.jiachian.nbatoday.home.user.ui.UserPageViewModel
import com.jiachian.nbatoday.player.data.PlayerDao
import com.jiachian.nbatoday.player.data.PlayerRepository
import com.jiachian.nbatoday.player.ui.PlayerViewModel
import com.jiachian.nbatoday.repository.data.TestBetRepository
import com.jiachian.nbatoday.repository.data.TestGameRepository
import com.jiachian.nbatoday.repository.data.TestPlayerRepository
import com.jiachian.nbatoday.repository.data.TestScheduleRepository
import com.jiachian.nbatoday.repository.data.TestTeamRepository
import com.jiachian.nbatoday.repository.data.TestUserRepository
import com.jiachian.nbatoday.splash.ui.SplashViewModel
import com.jiachian.nbatoday.team.data.TeamDao
import com.jiachian.nbatoday.team.data.TeamRepository
import com.jiachian.nbatoday.team.ui.main.TeamViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testModule = module {
    factory { TestGameDao(get()) as GameDao }
    factory { TestBoxScoreDao(get()) as BoxScoreDao }
    factory { TestTeamDao(get()) as TeamDao }
    factory { TestPlayerDao(get()) as PlayerDao }
    factory { TestBetDao(get()) as BetDao }
    factory { TestGameRemoteSource() as GameRemoteSource }
    factory { TestTeamRemoteSource() as TeamRemoteSource }
    factory { TestPlayerRemoteSource() as PlayerRemoteSource }
    factory { TestUserRemoteSource() as UserRemoteSource }
    factory { TestGameLocalSource(get()) as GameLocalSource }
    factory { TestBoxScoreLocalSource(get()) as BoxScoreLocalSource }
    factory { TestTeamLocalSource(get()) as TeamLocalSource }
    factory { TestPlayerLocalSource(get()) as PlayerLocalSource }
    factory { TestBetLocalSource(get()) as BetLocalSource }

    single { DataHolder() }
    single { TestDataStore(get()) as BaseDataStore }
    single { TestScheduleRepository(get(), get(), get(), get()) as ScheduleRepository }
    single { TestGameRepository(get(), get(), get()) as GameRepository }
    single { TestTeamRepository(get(), get()) as TeamRepository }
    single { TestPlayerRepository(get(), get()) as PlayerRepository }
    single { TestBetRepository(get(), get()) as BetRepository }
    single { TestUserRepository(get(), get()) as UserRepository }

    viewModel { SplashViewModel(get(), get(), get(), get()) }
    viewModel { BoxScoreViewModel(get(), get()) }
    viewModel { TeamViewModel(get(), get(), get()) }
    viewModel { PlayerViewModel(get(), get()) }
    viewModel { CalendarViewModel(get(), get()) }
    viewModel { BetViewModel(get(), get()) }
    viewModel { SchedulePageViewModel(get(), get()) }
    viewModel { StandingPageViewModel(get()) }
    viewModel { UserPageViewModel(get(), get()) }
}
