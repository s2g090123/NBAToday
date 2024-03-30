package com.jiachian.nbatoday.repository.user

import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.models.local.user.User
import kotlinx.coroutines.flow.Flow

/**
 * Manage user-related data.
 */
interface UserRepository {
    /**
     * A Flow representing the current user information.
     */
    val user: Flow<User?>

    /**
     * Performs a user login operation.
     *
     * @param account The user account for login.
     * @param password The password associated with the account.
     */
    suspend fun login(account: String, password: String): Response<User>

    /**
     * Performs a user logout operation.
     */
    suspend fun logout()

    /**
     * Performs a user registration operation.
     *
     * @param account The user account for registration.
     * @param password The password associated with the account.
     */
    suspend fun register(account: String, password: String): Response<User>

    /**
     * Adds points to the currently logged-in user.
     *
     * @param points The points to be added.
     */
    suspend fun addPoints(points: Long): Response<Unit>
}
