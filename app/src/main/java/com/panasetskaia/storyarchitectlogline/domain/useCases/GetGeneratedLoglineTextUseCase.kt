package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGeneratedLoglineTextUseCase @Inject constructor(private val repo: LoglineRepository) {

    suspend operator fun invoke(): Flow<String?> {
        return repo.getGeneratedLoglineText()
    }

}
