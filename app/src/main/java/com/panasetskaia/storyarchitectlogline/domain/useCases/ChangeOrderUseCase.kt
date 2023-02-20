package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository

class ChangeOrderUseCase (private val repo: LoglineRepository) {

    suspend operator fun invoke(id: Int, newPosition: Int) {
        repo.changeOrder(id,newPosition)
    }

}