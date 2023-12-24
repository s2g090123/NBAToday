package com.jiachian.nbatoday.koin

import com.jiachian.nbatoday.DataHolder
import com.jiachian.nbatoday.database.dao.BetDao
import com.jiachian.nbatoday.database.dao.BoxScoreDao
import com.jiachian.nbatoday.database.dao.GameDao
import com.jiachian.nbatoday.database.dao.PlayerDao
import com.jiachian.nbatoday.database.dao.TeamDao
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
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.datastore.data.TestDataStore
import com.jiachian.nbatoday.navigation.NavigationController
import com.jiachian.nbatoday.repository.RepositoryProvider
import com.jiachian.nbatoday.repository.bet.BetRepository
import com.jiachian.nbatoday.repository.data.TestBetRepository
import com.jiachian.nbatoday.repository.data.TestGameRepository
import com.jiachian.nbatoday.repository.data.TestPlayerRepository
import com.jiachian.nbatoday.repository.data.TestScheduleRepository
import com.jiachian.nbatoday.repository.data.TestTeamRepository
import com.jiachian.nbatoday.repository.data.TestUserRepository
import com.jiachian.nbatoday.repository.game.GameRepository
import com.jiachian.nbatoday.repository.player.PlayerRepository
import com.jiachian.nbatoday.repository.schedule.ScheduleRepository
import com.jiachian.nbatoday.repository.team.TeamRepository
import com.jiachian.nbatoday.repository.user.UserRepository
import com.jiachian.nbatoday.utils.ComposeViewModelProvider
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
    factory { ComposeViewModelProvider(get(), get(), get()) }
    factory { RepositoryProvider(get(), get(), get(), get(), get(), get()) }

    single { DataHolder() }
    single { TestDataStore(get()) as BaseDataStore }
    single { TestScheduleRepository(get(), get(), get(), get()) as ScheduleRepository }
    single { TestGameRepository(get(), get(), get()) as GameRepository }
    single { TestTeamRepository(get(), get()) as TeamRepository }
    single { TestPlayerRepository(get(), get()) as PlayerRepository }
    single { TestBetRepository(get(), get()) as BetRepository }
    single { TestUserRepository(get(), get()) as UserRepository }
    single { NavigationController() }
}
