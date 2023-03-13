package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLastSavedUseCase @Inject constructor(private val repo: LoglineRepository) {

    suspend operator fun invoke(): Logline {
        return repo.getLastSavedLogline()
    }

}
