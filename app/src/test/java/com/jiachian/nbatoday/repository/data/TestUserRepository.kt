package com.jiachian.nbatoday.repository.data

import com.jiachian.nbatoday.datasource.remote.data.TestUserRemoteSource
import com.jiachian.nbatoday.datastore.data.TestDataStore
import com.jiachian.nbatoday.models.remote.user.extensions.toUser
import com.jiachian.nbatoday.repository.user.UserRepository
import kotlinx.coroutines.flow.firstOrNull

class TestUserRepository(
    private val userRemoteSource: TestUserRemoteSource,
    private val dataStore: TestDataStore,
) : UserRepository() {

    companion object {
        fun get(): TestUserRepository {
            return TestUserRepository(
                userRemoteSource = TestUserRemoteSource(),
                dataStore = TestDataStore()
            )
        }
    }

    override val user = dataStore.user

    override suspend fun login(account: String, password: String) {
        userRemoteSource
            .login(account, password)
            .takeIf { !it.isError() }
            ?.body()
            ?.toUser()
            ?.takeIf { user -> user.available }
            ?.let { user ->
                dataStore.updateUser(user)
            }
    }

    override suspend fun logout() {
        dataStore.updateUser(null)
    }

    override suspend fun register(account: String, password: String) {
        userRemoteSource
            .register(account, password)
            .takeIf { !it.isError() }
            ?.body()
            ?.toUser()
            ?.let { user ->
                dataStore.updateUser(user)
            }
    }

    override suspend fun updatePoints(points: Long) {
        user
            .firstOrNull()
            ?.takeIf { user -> user.available }
            ?.let { user ->
                userRemoteSource
                    .updatePoints(user.account, points, user.token)
                    .takeIf { !it.isError() }
                    ?.also {
                        dataStore.updateUser(user.copy(points = points))
                    }
            }
    }

    override suspend fun addPoints(points: Long) {
        user
            .firstOrNull()
            ?.takeIf { user -> user.available }
            ?.let { user ->
                val updatedPoints = user.points + points
                userRemoteSource
                    .updatePoints(user.account, updatedPoints, user.token)
                    .takeIf { !it.isError() }
                    ?.also {
                        dataStore.updateUser(user.copy(points = updatedPoints))
                    }
            }
    }
}
