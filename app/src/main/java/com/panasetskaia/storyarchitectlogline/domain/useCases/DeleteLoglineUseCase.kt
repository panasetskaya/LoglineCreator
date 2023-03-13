package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import javax.inject.Inject

class DeleteLoglineUseCase @Inject constructor(private val repo: LoglineRepository) {

    suspend operator fun invoke(id: Int) {
        repo.deleteLogline(id)
    }
}