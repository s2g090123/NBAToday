package com.jiachian.nbatoday.home.user.data

import com.jiachian.nbatoday.common.data.Response
import com.jiachian.nbatoday.common.data.datastore.BaseDataStore
import com.jiachian.nbatoday.home.user.data.model.local.User
import com.jiachian.nbatoday.home.user.data.model.remote.LoginBody
import com.jiachian.nbatoday.home.user.data.model.remote.UpdatePointsBody
import com.jiachian.nbatoday.home.user.data.model.toUser
import com.jiachian.nbatoday.utils.isError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class NBAUserRepository(
    private val service: UserService,
    private val dataStore: BaseDataStore,
) : UserRepository {
    override val user: Flow<User?> = dataStore.user

    override suspend fun login(account: String, password: String): Response<User> {
        return service
            .login(LoginBody(account, password))
            .takeIf { !it.isError() }
            ?.body()
            ?.toUser()
            ?.takeIf { user -> user.available }
            ?.also { user ->
                dataStore.updateUser(user)
            }
            ?.let {
                Response.Success(it)
            }
            ?: Response.Error()
    }

    override suspend fun logout() {
        dataStore.updateUser(null)
    }

    override suspend fun register(account: String, password: String): Response<User> {
        return service
            .register(LoginBody(account, password))
            .takeIf { !it.isError() }
            ?.body()
            ?.toUser()
            ?.takeIf { user -> user.available }
            ?.also { user ->
                dataStore.updateUser(user)
            }
            ?.let {
                Response.Success(it)
            }
            ?: Response.Error()
    }

    override suspend fun addPoints(points: Long): Response<Unit> {
        return user
            .firstOrNull()
            ?.takeIf { user -> user.available }
            ?.let { user ->
                val updatedPoints = user.points + points
                service
                    .updatePoints(UpdatePointsBody(user.account, user.token, updatedPoints))
                    .takeIf { !it.isError() }
                    ?.also {
                        dataStore.updateUser(user.copy(points = updatedPoints))
                    }
            }
            ?.let {
                Response.Success(Unit)
            }
            ?: Response.Error()
    }
}
