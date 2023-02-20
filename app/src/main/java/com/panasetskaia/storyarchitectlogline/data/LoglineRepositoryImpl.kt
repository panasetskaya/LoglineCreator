package com.panasetskaia.storyarchitectlogline.data

import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import kotlinx.coroutines.flow.Flow

class LoglineRepositoryImpl: LoglineRepository {
    override fun getAllSavedLoglines(): Flow<List<Logline>> {
        TODO("Not yet implemented")
    }

    override fun searchForWords(query: String): Flow<List<Logline>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteLogline(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun addLogline(lgln: Logline) {
        TODO("Not yet implemented")
    }

    override suspend fun changeOrder(id: Int, newPosition: Int) {
        TODO("Not yet implemented")
    }
}