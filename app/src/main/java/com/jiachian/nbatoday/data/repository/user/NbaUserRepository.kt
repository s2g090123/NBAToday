package com.jiachian.nbatoday.data.repository.user

import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.data.remote.datasource.user.UserRemoteSource
import com.jiachian.nbatoday.data.remote.user.User
import com.jiachian.nbatoday.utils.getOrError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NbaUserRepository(
    private val userRemoteSource: UserRemoteSource,
    private val dataStore: BaseDataStore,
) : UserRepository() {
    override val user: Flow<User?> = dataStore.userData

    override suspend fun login(account: String, password: String) {
        isProgressingImp.value = true
        val userData = userRemoteSource.login(account, password) ?: return
        dataStore.updateUser(userData)
        isProgressingImp.value = false
    }

    override suspend fun logout() {
        isProgressingImp.value = true
        dataStore.updateUser(null)
        isProgressingImp.value = false
    }

    override suspend fun register(account: String, password: String) {
        isProgressingImp.value = true
        val userData = userRemoteSource.register(account, password) ?: return
        dataStore.updateUser(userData)
        isProgressingImp.value = false
    }

    override suspend fun updatePassword(password: String) {
        user.firstOrNull()?.run {
            if (!this.isAvailable()) return@run
            userRemoteSource.updatePassword(
                account.getOrError(),
                password,
                token.getOrError()
            )
            dataStore.updateUser(
                User(
                    account = account,
                    name = name,
                    points = points,
                    password = password,
                    token = token
                )
            )
        }
    }

    override suspend fun updatePoints(points: Long) {
        user.firstOrNull()?.run {
            if (!this.isAvailable()) return@run
            userRemoteSource.updatePoints(
                account.getOrError(),
                points,
                token.getOrError()
            )
            dataStore.updateUser(
                User(
                    account = account,
                    name = name,
                    points = points,
                    password = password,
                    token = token
                )
            )
        }
    }

    override suspend fun addPoints(points: Long) {
        user.firstOrNull()?.run {
            if (!this.isAvailable()) return@run
            val updatePoints = points + points
            userRemoteSource.updatePoints(
                account.getOrError(),
                updatePoints,
                token.getOrError()
            )
            dataStore.updateUser(
                User(
                    account = account,
                    name = name,
                    points = updatePoints,
                    password = password,
                    token = token
                )
            )
        }

    }
}
