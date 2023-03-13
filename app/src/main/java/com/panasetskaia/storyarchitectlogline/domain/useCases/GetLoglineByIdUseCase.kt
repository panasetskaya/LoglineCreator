package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import javax.inject.Inject

class GetLoglineByIdUseCase @Inject constructor(private val repo: LoglineRepository) {

    suspend operator fun invoke(id: Int): Logline{
        return repo.getLoglineById(id)
    }

}