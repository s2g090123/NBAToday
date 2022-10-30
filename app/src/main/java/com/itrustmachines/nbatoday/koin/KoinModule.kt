package com.itrustmachines.nbatoday.koin

import com.itrustmachines.nbatoday.MainViewModel
import com.itrustmachines.nbatoday.data.NbaRepository
import com.itrustmachines.nbatoday.data.remote.NbaRemoteDataSource
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {

    factory {
        NbaRemoteDataSource()
    }

    single {
        NbaRepository(get<NbaRemoteDataSource>())
    }

    viewModel {
        MainViewModel(get<NbaRepository>())
    }
}