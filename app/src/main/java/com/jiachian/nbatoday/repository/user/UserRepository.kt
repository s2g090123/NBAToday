package com.jiachian.nbatoday.repository.user

import com.jiachian.nbatoday.common.Response
import com.jiachian.nbatoday.models.local.user.User
import com.jiachian.nbatoday.repository.BaseRepository
import kotlinx.coroutines.flow.Flow

/**
 * Manage user-related data.
 */
abstract class UserRepository : BaseRepository() {
    /**
     * A Flow representing the current user information.
     */
    abstract val user: Flow<User?>

    /**
     * Performs a user login operation.
     *
     * @param account The user account for login.
     * @param password The password associated with the account.
     */
    abstract suspend fun login(account: String, password: String): Response<User>

    /**
     * Performs a user logout operation.
     */
    abstract suspend fun logout()

    /**
     * Performs a user registration operation.
     *
     * @param account The user account for registration.
     * @param password The password associated with the account.
     */
    abstract suspend fun register(account: String, password: String): Response<User>

    /**
     * Updates the points for the currently logged-in user.
     *
     * @param points The new points value.
     */
    abstract suspend fun updatePoints(points: Long)

    /**
     * Adds points to the currently logged-in user.
     *
     * @param points The points to be added.
     */
    abstract suspend fun addPoints(points: Long)
}
