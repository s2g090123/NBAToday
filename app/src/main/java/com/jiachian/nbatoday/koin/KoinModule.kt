package com.jiachian.nbatoday.koin

import com.jiachian.nbatoday.MainViewModel
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.data.datastore.NbaDataStore
import com.jiachian.nbatoday.data.local.LocalDataSource
import com.jiachian.nbatoday.data.local.NbaDatabase
import com.jiachian.nbatoday.data.local.NbaLocalDataSource
import com.jiachian.nbatoday.data.remote.NbaRemoteDataSource
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import com.jiachian.nbatoday.data.repository.RepositoryProvider
import com.jiachian.nbatoday.data.repository.bet.BetRepository
import com.jiachian.nbatoday.data.repository.bet.NbaBetRepository
import com.jiachian.nbatoday.data.repository.game.GameRepository
import com.jiachian.nbatoday.data.repository.game.NbaGameRepository
import com.jiachian.nbatoday.data.repository.player.NbaPlayerRepository
import com.jiachian.nbatoday.data.repository.player.PlayerRepository
import com.jiachian.nbatoday.data.repository.schedule.NbaScheduleRepository
import com.jiachian.nbatoday.data.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.data.repository.team.NbaTeamRepository
import com.jiachian.nbatoday.data.repository.team.TeamRepository
import com.jiachian.nbatoday.data.repository.user.NbaUserRepository
import com.jiachian.nbatoday.data.repository.user.UserRepository
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
import com.jiachian.nbatoday.utils.ScreenStateHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {

    factory { (get() as NbaDatabase).getNbaDao() }
    factory { NbaRemoteDataSource(get()) as RemoteDataSource }
    factory { NbaLocalDataSource(get()) as LocalDataSource }
    factory { ComposeViewModelProvider(get(), get(), get()) }
    factory { RepositoryProvider(get(), get(), get(), get(), get(), get()) }

    single { NbaDatabase.getInstance(androidContext()) }
    single { NbaDataStore(androidApplication()) as BaseDataStore }
    single { ScreenStateHelper(get(), get()) }
    single { NbaScheduleRepository(get(), get(), get(), get()) as ScheduleRepository }
    single { NbaGameRepository(get(), get()) as GameRepository }
    single { NbaTeamRepository(get(), get(), get()) as TeamRepository }
    single { NbaPlayerRepository(get(), get()) as PlayerRepository }
    single { NbaBetRepository(get(), get()) as BetRepository }
    single { NbaUserRepository(get(), get()) as UserRepository }

    viewModel { MainViewModel(get(), get(), get()) }
}
