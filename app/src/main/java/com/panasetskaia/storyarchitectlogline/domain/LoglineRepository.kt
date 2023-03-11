package com.panasetskaia.storyarchitectlogline.domain

import kotlinx.coroutines.flow.Flow

interface LoglineRepository {

    fun getAllSavedLoglines(): Flow<List<Logline>>

    fun getLastSavedLogline(): Flow<Logline>

    suspend fun getLoglineById(id: Int): Logline

    fun searchForWords(query: String): Flow<List<Logline>>

    suspend fun deleteLogline(id: Int)

    suspend fun addLogline(
        pronoun: String,
        majorEvent: String,
        storyGoal: String,
        majorEventIncludesMainCharacter: Boolean,
        characterInfo: String,
        theme: String?,
        mprEvent: String?,
        afterMprEvent: String?,
        stakes: String?,
        worldText: String?
    )

    suspend fun changeText(id: Int, newText: String)

}