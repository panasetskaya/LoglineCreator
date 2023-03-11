package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository

class GetLoglineByIdUseCase(private val repo: LoglineRepository) {

    suspend operator fun invoke(id: Int): Logline{
        return repo.getLoglineById(id)
    }

}