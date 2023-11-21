package com.jiachian.nbatoday.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseRepository {
    protected val isProgressingImp = MutableStateFlow(false)
    val isProgressing = isProgressingImp.asStateFlow()
}
