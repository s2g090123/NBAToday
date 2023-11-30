package com.jiachian.nbatoday.repository.user

import com.jiachian.nbatoday.datasource.remote.user.UserRemoteSource
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.models.remote.user.toUser
import com.jiachian.nbatoday.utils.getOrError
import com.jiachian.nbatoday.utils.showErrorToast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NbaUserRepository(
    private val userRemoteSource: UserRemoteSource,
    private val dataStore: BaseDataStore,
) : UserRepository() {
    override val user: Flow<User?> = dataStore.user

    override suspend fun login(account: String, password: String) {
        loading {
            val response = userRemoteSource.login(account, password)
            if (response.isError()) {
                showErrorToast()
                return@loading
            }
            response
                .body()
                ?.toUser()
                ?.also { user ->
                    dataStore.updateUser(user)
                }
        }
    }

    override suspend fun logout() {
        isProgressingImp.value = true
        dataStore.updateUser(null)
        isProgressingImp.value = false
    }

    override suspend fun register(account: String, password: String) {
        loading {
            val response = userRemoteSource.register(account, password)
            if (response.isError()) {
                showErrorToast()
                return@loading
            }
            response
                .body()
                ?.toUser()
                ?.also { user ->
                    dataStore.updateUser(user)
                }
        }
    }

    override suspend fun updatePassword(password: String) {
        user.firstOrNull()?.run {
            if (!this.isAvailable) return@run
            userRemoteSource.updatePassword(
                account.getOrError(),
                password,
                token.getOrError()
            )
            dataStore.updateUser(copy(password = password))
        }
    }

    override suspend fun updatePoints(points: Long) {
        user.firstOrNull()?.run {
            if (!this.isAvailable) return@run
            userRemoteSource.updatePoints(
                account.getOrError(),
                points,
                token.getOrError()
            )
            dataStore.updateUser(copy(points = points))
        }
    }

    override suspend fun addPoints(points: Long) {
        user.firstOrNull()?.run {
            if (!this.isAvailable) return@run
            val updatePoints = points + points
            userRemoteSource.updatePoints(
                account.getOrError(),
                updatePoints,
                token.getOrError()
            )
            dataStore.updateUser(copy(points = updatePoints))
        }
    }
}
