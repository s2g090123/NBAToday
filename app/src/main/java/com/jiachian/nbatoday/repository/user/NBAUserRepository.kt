package com.jiachian.nbatoday.repository.user

import com.jiachian.nbatoday.datasource.remote.user.UserRemoteSource
import com.jiachian.nbatoday.datastore.BaseDataStore
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.models.remote.user.extensions.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NBAUserRepository(
    private val userRemoteSource: UserRemoteSource,
    private val dataStore: BaseDataStore,
) : UserRepository() {
    override val user: Flow<User?> = dataStore.user

    override suspend fun login(account: String, password: String) {
        loading {
            userRemoteSource
                .login(account, password)
                .takeIf { !it.isError() }
                ?.body()
                ?.toUser()
                ?.takeIf { user -> user.available }
                ?.let { user ->
                    dataStore.updateUser(user)
                }
                ?: onError()
        }
    }

    override suspend fun logout() {
        loading {
            dataStore.updateUser(null)
        }
    }

    override suspend fun register(account: String, password: String) {
        loading {
            userRemoteSource
                .register(account, password)
                .takeIf { !it.isError() }
                ?.body()
                ?.toUser()
                ?.let { user ->
                    dataStore.updateUser(user)
                }
                ?: onError()
        }
    }

    override suspend fun updatePoints(points: Long) {
        loading {
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
                ?: onError()
        }
    }

    override suspend fun addPoints(points: Long) {
        loading {
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
                ?: onError()
        }
    }
}
