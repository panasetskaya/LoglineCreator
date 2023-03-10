package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUseCase @Inject constructor(private val repo: LoglineRepository) {

    operator fun invoke(): Flow<List<Logline>> {
        return repo.getAllSavedLoglines()
    }

}