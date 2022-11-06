package com.itrustmachines.nbatoday.koin

import com.itrustmachines.nbatoday.MainViewModel
import com.itrustmachines.nbatoday.data.NbaRepository
import com.itrustmachines.nbatoday.data.local.NbaDatabase
import com.itrustmachines.nbatoday.data.local.NbaLocalDataSource
import com.itrustmachines.nbatoday.data.remote.NbaRemoteDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {

    factory { (get() as NbaDatabase).getNbaDao() }
    factory { NbaRemoteDataSource() }
    factory { NbaLocalDataSource(get()) }

    single { NbaDatabase.getInstance(androidContext()) }
    single { NbaRepository(get<NbaRemoteDataSource>(), get<NbaLocalDataSource>()) }

    viewModel { MainViewModel(get<NbaRepository>()) }
}