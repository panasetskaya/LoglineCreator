package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository

class AddLoglineUseCase(private val repo: LoglineRepository) {

    suspend operator fun invoke(logline: Logline) {
        repo.addLogline(logline)
    }
}