package com.jiachian.nbatoday.koin

import com.jiachian.nbatoday.SplashViewModel
import com.jiachian.nbatoday.compose.screen.account.LoginDialogViewModel
import com.jiachian.nbatoday.compose.screen.bet.BetViewModel
import com.jiachian.nbatoday.compose.screen.bet.dialog.BetDialogViewModel
import com.jiachian.nbatoday.compose.screen.calendar.CalendarViewModel
import com.jiachian.nbatoday.compose.screen.home.schedule.SchedulePageViewModel
import com.jiachian.nbatoday.compose.screen.home.standing.StandingPageViewModel
import com.jiachian.nbatoday.compose.screen.home.user.UserPageViewModel
import com.jiachian.nbatoday.compose.screen.player.PlayerViewModel
import com.jiachian.nbatoday.compose.screen.score.BoxScoreViewModel
import com.jiachian.nbatoday.compose.screen.team.TeamViewModel
import com.jiachian.nbatoday.database.NBADatabase
import com.jiachian.nbatoday.datasource.local.bet.BetLocalSource
import com.jiachian.nbatoday.datasource.local.bet.NBABetLocalSource
import com.jiachian.nbatoday.datasource.local.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.datasource.local.boxscore.NBABoxScoreLocalSource
import com.jiachian.nbatoday.datasource.local.game.GameLocalSource
import com.jiachian.nbatoday.datasource.local.game.NBAGameLocalSource
import com.jiachian.nbatoday.datasource.local.player.NBAPlayerLocalSource
import com.jiachian.nbatoday.datasource.local.player.PlayerLocalSource
import com.jiachian.nbatoday.datasource.local.team.NBATeamLocalSource
import com.jiachian.nbatoday.datasource.local.team.TeamLocalSource
import com.jiachian.nbatoday.datasource.remote.RemoteSource
import com.jiachian.nbatoday.datasource.remote.game.GameRemoteSource
import com.jiachian.nbatoday.datasource.remote.game.NBAGameRemoteSource
import com.jiachian.nbatoday.datasource.remote.player.NBAPlayerRemoteSource
import com.jiachian.nbatoday.datasource.remote.player.PlayerRemoteSource
import com.jiachian.nbatoday.datasource.remote.team.NBATeamRemoteSource
import com.jiachian.nbatoday.datasource.remote.team.TeamRemoteSource
import com.jiachian.nbatoday.datasource.remote.user.NBAUserRemoteSource
import com.jiachian.nbatoday.datasource.remote.user.UserRemoteSource
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.datastore.NBADataStore
import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.bet.NBABetRepository
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.game.NBAGameRepository
import com.jiachian.nbatoday.repository.player.NBAPlayerRepository
import com.jiachian.nbatoday.repository.player.PlayerRepository
import com.jiachian.nbatoday.repository.schedule.NBAScheduleRepository
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.repository.team.NBATeamRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.repository.user.NBAUserRepository
import com.jiachian.nbatoday.repository.user.UserRepository
import com.jiachian.nbatoday.service.GameService
import com.jiachian.nbatoday.service.PlayerService
import com.jiachian.nbatoday.service.TeamService
import com.jiachian.nbatoday.service.UserService
import com.jiachian.nbatoday.usecase.bet.AddBet
import com.jiachian.nbatoday.usecase.bet.BetUseCase
import com.jiachian.nbatoday.usecase.bet.DeleteBet
import com.jiachian.nbatoday.usecase.bet.GetBetGames
import com.jiachian.nbatoday.usecase.game.GameUseCase
import com.jiachian.nbatoday.usecase.game.GetFirstLastGameDate
import com.jiachian.nbatoday.usecase.game.GetGame
import com.jiachian.nbatoday.usecase.game.GetGamesDuring
import com.jiachian.nbatoday.usecase.schedule.ScheduleUseCase
import com.jiachian.nbatoday.usecase.schedule.UpdateSchedule
import com.jiachian.nbatoday.usecase.user.AddPoints
import com.jiachian.nbatoday.usecase.user.GetUser
import com.jiachian.nbatoday.usecase.user.UserLogin
import com.jiachian.nbatoday.usecase.user.UserRegister
import com.jiachian.nbatoday.usecase.user.UserUseCase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    factory { (get() as NBADatabase).getGameDao() }
    factory { (get() as NBADatabase).getBoxScoreDao() }
    factory { (get() as NBADatabase).getTeamDao() }
    factory { (get() as NBADatabase).getPlayerDao() }
    factory { (get() as NBADatabase).getBetDao() }
    factory { RemoteSource.createService(GameService::class.java) }
    factory { RemoteSource.createService(UserService::class.java) }
    factory { NBAGameRemoteSource(RemoteSource.createService(GameService::class.java)) as GameRemoteSource }
    factory { NBATeamRemoteSource(RemoteSource.createService(TeamService::class.java)) as TeamRemoteSource }
    factory { NBAPlayerRemoteSource(RemoteSource.createService(PlayerService::class.java)) as PlayerRemoteSource }
    factory { NBAUserRemoteSource(RemoteSource.createService(UserService::class.java)) as UserRemoteSource }
    factory { NBAGameLocalSource(get()) as GameLocalSource }
    factory { NBABoxScoreLocalSource(get()) as BoxScoreLocalSource }
    factory { NBATeamLocalSource(get()) as TeamLocalSource }
    factory { NBAPlayerLocalSource(get()) as PlayerLocalSource }
    factory { NBABetLocalSource(get()) as BetLocalSource }

    // use case
    factory { BetUseCase(get(), get(), get()) }
    factory { GetBetGames(get()) }
    factory { AddBet(get(), get()) }
    factory { DeleteBet(get()) }
    factory { UserUseCase(get(), get(), get(), get()) }
    factory { GetUser(get()) }
    factory { AddPoints(get()) }
    factory { UserLogin(get()) }
    factory { UserRegister(get()) }
    factory { GameUseCase(get(), get(), get()) }
    factory { GetGame(get()) }
    factory { GetFirstLastGameDate(get()) }
    factory { GetGamesDuring(get()) }
    factory { ScheduleUseCase(get()) }
    factory { UpdateSchedule(get()) }

    single { NBADatabase.getInstance(androidContext()) }
    single { NBADataStore(androidApplication()) as BaseDataStore }
    single { NBAScheduleRepository(get(), get(), get()) as ScheduleRepository }
    single { NBAGameRepository(get(), get(), get()) as GameRepository }
    single { NBATeamRepository(get(), get()) as TeamRepository }
    single { NBAPlayerRepository(get(), get()) as PlayerRepository }
    single { NBABetRepository(get()) as BetRepository }
    single { NBAUserRepository(get(), get()) as UserRepository }

    viewModel { SplashViewModel(get(), get(), get(), get()) }
    viewModel { BoxScoreViewModel(get(), get()) }
    viewModel { TeamViewModel(get(), get(), get(), get()) }
    viewModel { PlayerViewModel(get(), get()) }
    viewModel { CalendarViewModel(get(), get(), get()) }
    viewModel { BetViewModel(get(), get(), get()) }
    viewModel { SchedulePageViewModel(get(), get(), get()) }
    viewModel { StandingPageViewModel(get()) }
    viewModel { UserPageViewModel(get(), get()) }
    viewModel { LoginDialogViewModel(get()) }
    viewModel { BetDialogViewModel(get(), get(), get(), get()) }
}
