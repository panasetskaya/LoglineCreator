package com.panasetskaia.storyarchitectlogline.domain

import kotlinx.coroutines.flow.Flow

interface LoglineRepository {

    fun getAllSavedLoglines(): Flow<List<Logline>>

    fun searchForWords(query: String): Flow<List<Logline>>

    suspend fun deleteLogline(id: Int)

    suspend fun addLogline(lgln: Logline)

    suspend fun changeOrder(id: Int, newPosition: Int)

}