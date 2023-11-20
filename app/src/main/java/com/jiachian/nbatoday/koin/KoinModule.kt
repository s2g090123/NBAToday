package com.jiachian.nbatoday.koin

import com.jiachian.nbatoday.MainViewModel
import com.jiachian.nbatoday.data.NbaRepository
import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.data.datastore.NbaDataStore
import com.jiachian.nbatoday.data.local.NbaDatabase
import com.jiachian.nbatoday.data.local.NbaLocalDataSource
import com.jiachian.nbatoday.data.remote.NbaRemoteDataSource
import com.jiachian.nbatoday.utils.ScreenStateHelper
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {

    factory { (get() as NbaDatabase).getNbaDao() }
    factory { NbaRemoteDataSource(get()) }
    factory { NbaLocalDataSource(get()) }

    single { NbaDatabase.getInstance(androidContext()) }
    single { NbaRepository(get<NbaRemoteDataSource>(), get<NbaLocalDataSource>(), get()) }
    single { NbaDataStore(androidApplication()) as BaseDataStore }
    single { ScreenStateHelper(get<NbaRepository>(), get()) }

    viewModel { MainViewModel(get<NbaRepository>(), get(), get()) }
}
