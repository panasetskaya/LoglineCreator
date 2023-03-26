package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import javax.inject.Inject

class SaveNewLoglineUseCase @Inject constructor(private val repo: LoglineRepository) {

    suspend operator fun invoke(newText: String) {
        repo.saveNewLogline(newText)
    }
}
