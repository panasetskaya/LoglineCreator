package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import javax.inject.Inject

class ChangeTextUseCase @Inject constructor(private val repo: LoglineRepository) {

    suspend operator fun invoke(id: Int, newText: String) {
        repo.changeText(id,newText)
    }

}