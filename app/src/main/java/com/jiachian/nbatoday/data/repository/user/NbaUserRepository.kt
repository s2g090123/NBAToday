package com.jiachian.nbatoday.data.repository.user

import com.jiachian.nbatoday.data.datastore.BaseDataStore
import com.jiachian.nbatoday.data.remote.RemoteDataSource
import com.jiachian.nbatoday.data.remote.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NbaUserRepository(
    private val remoteDataSource: RemoteDataSource,
    private val dataStore: BaseDataStore,
) : UserRepository() {
    override val user: Flow<User?> = dataStore.userData

    override suspend fun login(account: String, password: String) {
        isProgressingImp.value = true
        val userData = remoteDataSource.login(account, password) ?: return
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
        val userData = remoteDataSource.register(account, password) ?: return
        dataStore.updateUser(userData)
        isProgressingImp.value = false
    }

    override suspend fun updatePassword(password: String) {
        val user = user.firstOrNull() ?: return
        val account = user.account ?: return
        val token = user.token ?: return
        remoteDataSource.updatePassword(account, password, token)
        dataStore.updateUser(
            User(
                account = account,
                name = user.name,
                points = user.points,
                password = password,
                token = token
            )
        )
    }

    override suspend fun updatePoints(points: Long) {
        val user = user.firstOrNull() ?: return
        val account = user.account ?: return
        val token = user.token ?: return
        remoteDataSource.updatePoints(account, points, token)
        dataStore.updateUser(
            User(
                account = account,
                name = user.name,
                points = points,
                password = user.password,
                token = token
            )
        )
    }

    override suspend fun addPoints(points: Long) {
        val user = user.firstOrNull() ?: return
        val account = user.account ?: return
        val token = user.token ?: return
        val currentPoint = user.points ?: return
        val updatePoints = currentPoint + points
        remoteDataSource.updatePoints(account, updatePoints, token)
        dataStore.updateUser(
            User(
                account = account,
                name = user.name,
                points = updatePoints,
                password = user.password,
                token = token
            )
        )
    }
}
