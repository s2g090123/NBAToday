package com.jiachian.nbatoday.datasource.remote.user

import com.jiachian.nbatoday.models.remote.user.RemoteUser
import retrofit2.Response

/**
 * The remote data source for user-related operations.
 */
abstract class UserRemoteSource {
    /**
     * Attempts to log in a user using the provided account and password.
     *
     * @param account The user's account.
     * @param password The user's password.
     * @return A [Response] containing a [RemoteUser] object.
     */
    abstract suspend fun login(account: String, password: String): Response<RemoteUser>

    /**
     * Registers a new user with the provided account and password.
     *
     * @param account The user's account.
     * @param password The user's password.
     * @return A [Response] containing a [RemoteUser] object.
     */
    abstract suspend fun register(account: String, password: String): Response<RemoteUser>

    /**
     * Updates the password for a user account.
     *
     * @param account The user's account.
     * @param password The new password.
     * @param token The authentication token.
     * @return A [Response] containing a success message.
     */
    abstract suspend fun updatePassword(account: String, password: String, token: String): Response<String>

    /**
     * Updates the points for a user account.
     *
     * @param account The user's account.
     * @param points The new points value.
     * @param token The authentication token.
     * @return A [Response] containing a success message.
     */
    abstract suspend fun updatePoints(account: String, points: Long, token: String): Response<String>
}
