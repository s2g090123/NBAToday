package com.jiachian.nbatoday.koin

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.bet.data.BetDao
import com.jiachian.nbatoday.bet.data.BetRepository
import com.jiachian.nbatoday.bet.data.NBABetRepository
import com.jiachian.nbatoday.bet.domain.AddBet
import com.jiachian.nbatoday.bet.domain.BetUseCase
import com.jiachian.nbatoday.bet.domain.DeleteBet
import com.jiachian.nbatoday.bet.domain.GetBetGames
import com.jiachian.nbatoday.bet.ui.main.BetViewModel
import com.jiachian.nbatoday.boxscore.data.BoxScoreDao
import com.jiachian.nbatoday.boxscore.domain.AddBoxScore
import com.jiachian.nbatoday.boxscore.domain.BoxScoreUseCase
import com.jiachian.nbatoday.boxscore.domain.GetBoxScore
import com.jiachian.nbatoday.boxscore.ui.main.BoxScoreViewModel
import com.jiachian.nbatoday.calendar.ui.CalendarViewModel
import com.jiachian.nbatoday.common.data.datastore.BaseDataStore
import com.jiachian.nbatoday.common.ui.bet.BetDialogViewModel
import com.jiachian.nbatoday.common.ui.login.LoginDialogViewModel
import com.jiachian.nbatoday.database.dao.TestBetDao
import com.jiachian.nbatoday.database.dao.TestBoxScoreDao
import com.jiachian.nbatoday.database.dao.TestGameDao
import com.jiachian.nbatoday.database.dao.TestPlayerDao
import com.jiachian.nbatoday.database.dao.TestTeamDao
import com.jiachian.nbatoday.datastore.data.TestDataStore
import com.jiachian.nbatoday.game.data.GameDao
import com.jiachian.nbatoday.game.data.GameRepository
import com.jiachian.nbatoday.game.data.GameService
import com.jiachian.nbatoday.game.data.NBAGameRepository
import com.jiachian.nbatoday.game.domain.GameUseCase
import com.jiachian.nbatoday.game.domain.GetFirstLastGameDate
import com.jiachian.nbatoday.game.domain.GetGame
import com.jiachian.nbatoday.game.domain.GetGamesAfter
import com.jiachian.nbatoday.game.domain.GetGamesBefore
import com.jiachian.nbatoday.game.domain.GetGamesDuring
import com.jiachian.nbatoday.home.schedule.data.NBAScheduleRepository
import com.jiachian.nbatoday.home.schedule.data.ScheduleRepository
import com.jiachian.nbatoday.home.schedule.domain.ScheduleUseCase
import com.jiachian.nbatoday.home.schedule.domain.UpdateSchedule
import com.jiachian.nbatoday.home.schedule.ui.SchedulePageViewModel
import com.jiachian.nbatoday.home.standing.ui.StandingPageViewModel
import com.jiachian.nbatoday.home.user.data.NBAUserRepository
import com.jiachian.nbatoday.home.user.data.UserRepository
import com.jiachian.nbatoday.home.user.data.UserService
import com.jiachian.nbatoday.home.user.domain.AddPoints
import com.jiachian.nbatoday.home.user.domain.GetTheme
import com.jiachian.nbatoday.home.user.domain.GetUser
import com.jiachian.nbatoday.home.user.domain.UpdateTheme
import com.jiachian.nbatoday.home.user.domain.UserLogin
import com.jiachian.nbatoday.home.user.domain.UserLogout
import com.jiachian.nbatoday.home.user.domain.UserRegister
import com.jiachian.nbatoday.home.user.domain.UserUseCase
import com.jiachian.nbatoday.home.user.ui.UserPageViewModel
import com.jiachian.nbatoday.player.data.NBAPlayerRepository
import com.jiachian.nbatoday.player.data.PlayerDao
import com.jiachian.nbatoday.player.data.PlayerRepository
import com.jiachian.nbatoday.player.data.PlayerService
import com.jiachian.nbatoday.player.domain.AddPlayer
import com.jiachian.nbatoday.player.domain.GetPlayer
import com.jiachian.nbatoday.player.domain.PlayerUseCase
import com.jiachian.nbatoday.player.ui.PlayerViewModel
import com.jiachian.nbatoday.service.TestGameService
import com.jiachian.nbatoday.service.TestPlayerService
import com.jiachian.nbatoday.service.TestTeamService
import com.jiachian.nbatoday.service.TestUserService
import com.jiachian.nbatoday.splash.ui.SplashViewModel
import com.jiachian.nbatoday.team.data.NBATeamRepository
import com.jiachian.nbatoday.team.data.TeamDao
import com.jiachian.nbatoday.team.data.TeamRepository
import com.jiachian.nbatoday.team.data.TeamService
import com.jiachian.nbatoday.team.domain.AddTeams
import com.jiachian.nbatoday.team.domain.GetTeamAndPlayers
import com.jiachian.nbatoday.team.domain.GetTeamRank
import com.jiachian.nbatoday.team.domain.GetTeams
import com.jiachian.nbatoday.team.domain.TeamUseCase
import com.jiachian.nbatoday.team.domain.UpdateTeamInfo
import com.jiachian.nbatoday.team.ui.main.TeamViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testModule = module {
    factory { TestGameDao(get()) as GameDao }
    factory { TestBoxScoreDao(get()) as BoxScoreDao }
    factory { TestTeamDao(get()) as TeamDao }
    factory { TestPlayerDao(get()) as PlayerDao }
    factory { TestBetDao(get()) as BetDao }
    factory { TestGameService() as GameService }
    factory { TestUserService() as UserService }
    factory { TestTeamService() as TeamService }
    factory { TestPlayerService() as PlayerService }

    // use case
    factory { BetUseCase(get(), get(), get()) }
    factory { GetBetGames(get()) }
    factory { AddBet(get(), get()) }
    factory { DeleteBet(get()) }
    factory { UserUseCase(get(), get(), get(), get(), get(), get(), get()) }
    factory { GetUser(get()) }
    factory { AddPoints(get()) }
    factory { UserLogin(get()) }
    factory { UserRegister(get()) }
    factory { UserLogout(get()) }
    factory { UpdateTheme(get()) }
    factory { GetTheme(get()) }
    factory { GameUseCase(get(), get(), get(), get(), get()) }
    factory { GetGame(get()) }
    factory { GetFirstLastGameDate(get()) }
    factory { GetGamesDuring(get()) }
    factory { GetGamesBefore(get()) }
    factory { GetGamesAfter(get()) }
    factory { ScheduleUseCase(get()) }
    factory { UpdateSchedule(get()) }
    factory { TeamUseCase(get(), get(), get(), get(), get()) }
    factory { GetTeams(get()) }
    factory { AddTeams(get()) }
    factory { UpdateTeamInfo(get()) }
    factory { GetTeamAndPlayers(get()) }
    factory { GetTeamRank(get()) }
    factory { PlayerUseCase(get(), get()) }
    factory { AddPlayer(get()) }
    factory { GetPlayer(get()) }
    factory { BoxScoreUseCase(get(), get()) }
    factory { AddBoxScore(get()) }
    factory { GetBoxScore(get()) }

    single { DataHolder() }
    single { TestDataStore(get()) as BaseDataStore }
    single { NBAScheduleRepository(get(), get(), get()) as ScheduleRepository }
    single { NBAGameRepository(get(), get(), get()) as GameRepository }
    single { NBATeamRepository(get(), get()) as TeamRepository }
    single { NBAPlayerRepository(get(), get()) as PlayerRepository }
    single { NBABetRepository(get()) as BetRepository }
    single { NBAUserRepository(get(), get()) as UserRepository }

    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { BoxScoreViewModel(get(), get()) }
    viewModel { TeamViewModel(get(), get(), get(), get()) }
    viewModel { PlayerViewModel(get(), get()) }
    viewModel { CalendarViewModel(get(), get(), get()) }
    viewModel { BetViewModel(get(), get(), get()) }
    viewModel { SchedulePageViewModel(get(), get(), get()) }
    viewModel { StandingPageViewModel(get()) }
    viewModel { UserPageViewModel(get()) }
    viewModel { LoginDialogViewModel(get()) }
    viewModel { BetDialogViewModel(get(), get(), get(), get()) }
}
