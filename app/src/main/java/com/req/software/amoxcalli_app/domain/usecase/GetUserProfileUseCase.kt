package com.req.software.amoxcalli_app.domain.usecase

import com.req.software.amoxcalli_app.data.repository.UserRepository
import com.req.software.amoxcalli_app.domain.model.UserProfile

/**
 * Use case for fetching user profile data
 */
class GetUserProfileUseCase(
    private val userRepository: UserRepository = UserRepository()
) {
    suspend operator fun invoke(userId: String, forceRefresh: Boolean = false): Result<UserProfile> {
        return userRepository.getUserProfile(userId, forceRefresh)
    }
}
