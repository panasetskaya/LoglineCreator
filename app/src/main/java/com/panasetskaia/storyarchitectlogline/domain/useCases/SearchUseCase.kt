package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import kotlinx.coroutines.flow.Flow

class SearchUseCase (private val repo: LoglineRepository) {

    operator fun invoke(query: String): Flow<List<Logline>> {
        return repo.searchForWords(query)
    }

}