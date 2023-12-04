package com.jiachian.nbatoday.koin

import com.jiachian.nbatoday.MainViewModel
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
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.RepositoryProvider
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
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.ScreenStateHelper
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
    factory { NBAGameRemoteSource() as GameRemoteSource }
    factory { NBATeamRemoteSource() as TeamRemoteSource }
    factory { NBAPlayerRemoteSource() as PlayerRemoteSource }
    factory { NBAUserRemoteSource() as UserRemoteSource }
    factory { NBAGameLocalSource(get()) as GameLocalSource }
    factory { NBABoxScoreLocalSource(get()) as BoxScoreLocalSource }
    factory { NBATeamLocalSource(get()) as TeamLocalSource }
    factory { NBAPlayerLocalSource(get()) as PlayerLocalSource }
    factory { NBABetLocalSource(get()) as BetLocalSource }
    factory { ComposeViewModelProvider(get(), get(), get()) }
    factory { RepositoryProvider(get(), get(), get(), get(), get(), get()) }
    factory { NavigationController(get()) }

    single { NBADatabase.getInstance(androidContext()) }
    single { NBADataStore(androidApplication()) as BaseDataStore }
    single { ScreenStateHelper(get(), get()) }
    single { NBAScheduleRepository(get(), get(), get(), get()) as ScheduleRepository }
    single { NBAGameRepository(get(), get(), get()) as GameRepository }
    single { NBATeamRepository(get(), get()) as TeamRepository }
    single { NBAPlayerRepository(get(), get()) as PlayerRepository }
    single { NBABetRepository(get(), get()) as BetRepository }
    single { NBAUserRepository(get(), get()) as UserRepository }

    viewModel { MainViewModel(get(), get(), get(), get()) }
}
