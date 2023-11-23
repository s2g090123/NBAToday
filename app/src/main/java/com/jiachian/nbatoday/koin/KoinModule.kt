package com.jiachian.nbatoday.koin

import com.jiachian.nbatoday.MainViewModel
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.data.datastore.NbaDataStore
import com.jiachian.nbatoday.data.local.NbaDatabase
import com.jiachian.nbatoday.data.local.datasource.bet.BetLocalSource
import com.jiachian.nbatoday.data.local.datasource.bet.NbaBetLocalSource
import com.jiachian.nbatoday.data.local.datasource.boxscore.BoxScoreLocalSource
import com.jiachian.nbatoday.data.local.datasource.boxscore.NbaBoxScoreLocalSource
import com.jiachian.nbatoday.data.local.datasource.game.GameLocalSource
import com.jiachian.nbatoday.data.local.datasource.game.NbaGameLocalSource
import com.jiachian.nbatoday.data.local.datasource.player.NbaPlayerLocalSource
import com.jiachian.nbatoday.data.local.datasource.player.PlayerLocalSource
import com.jiachian.nbatoday.data.local.datasource.team.NbaTeamLocalSource
import com.jiachian.nbatoday.data.local.datasource.team.TeamLocalSource
import com.jiachian.nbatoday.data.remote.datasource.game.GameRemoteSource
import com.jiachian.nbatoday.data.remote.datasource.game.NbaGameRemoteSource
import com.jiachian.nbatoday.data.remote.datasource.player.NbaPlayerRemoteSource
import com.jiachian.nbatoday.data.remote.datasource.player.PlayerRemoteSource
import com.jiachian.nbatoday.data.remote.datasource.team.NbaTeamRemoteSource
import com.jiachian.nbatoday.data.remote.datasource.team.TeamRemoteSource
import com.jiachian.nbatoday.data.remote.datasource.user.NbaUserRemoteSource
import com.jiachian.nbatoday.data.remote.datasource.user.UserRemoteSource
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

    factory { (get() as NbaDatabase).getGameDao() }
    factory { (get() as NbaDatabase).getBoxScoreDao() }
    factory { (get() as NbaDatabase).getTeamDao() }
    factory { (get() as NbaDatabase).getPlayerDao() }
    factory { (get() as NbaDatabase).getBetDao() }
    factory { NbaGameRemoteSource() as GameRemoteSource }
    factory { NbaTeamRemoteSource() as TeamRemoteSource }
    factory { NbaPlayerRemoteSource() as PlayerRemoteSource }
    factory { NbaUserRemoteSource() as UserRemoteSource }
    factory { NbaGameLocalSource(get()) as GameLocalSource }
    factory { NbaBoxScoreLocalSource(get()) as BoxScoreLocalSource }
    factory { NbaTeamLocalSource(get()) as TeamLocalSource }
    factory { NbaPlayerLocalSource(get()) as PlayerLocalSource }
    factory { NbaBetLocalSource(get()) as BetLocalSource }
    factory { ComposeViewModelProvider(get(), get(), get()) }
    factory { RepositoryProvider(get(), get(), get(), get(), get(), get()) }

    single { NbaDatabase.getInstance(androidContext()) }
    single { NbaDataStore(androidApplication()) as BaseDataStore }
    single { ScreenStateHelper(get(), get()) }
    single { NbaScheduleRepository(get(), get(), get(), get(), get()) as ScheduleRepository }
    single { NbaGameRepository(get(), get(), get()) as GameRepository }
    single { NbaTeamRepository(get(), get(), get()) as TeamRepository }
    single { NbaPlayerRepository(get(), get()) as PlayerRepository }
    single { NbaBetRepository(get(), get()) as BetRepository }
    single { NbaUserRepository(get(), get()) as UserRepository }

    viewModel { MainViewModel(get(), get(), get()) }
}
