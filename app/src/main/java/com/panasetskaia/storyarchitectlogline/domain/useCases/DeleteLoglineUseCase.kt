package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository

class DeleteLoglineUseCase (private val repo: LoglineRepository) {

    suspend operator fun invoke(id: Int) {
        repo.deleteLogline(id)
    }
}