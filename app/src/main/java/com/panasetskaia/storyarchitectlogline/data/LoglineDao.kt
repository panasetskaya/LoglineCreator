package com.panasetskaia.storyarchitectlogline.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.panasetskaia.storyarchitectlogline.domain.Logline
import kotlinx.coroutines.flow.Flow

@Dao
interface LoglineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLogline(logline: Logline)

    @Query("DELETE FROM logline WHERE id=:idToDelete")
    suspend fun deleteLogline(idToDelete: Int)

    @Query("SELECT * FROM logline ORDER BY number")
    fun getAllLoglines(): Flow<List<Logline>>

    @Query("SELECT * FROM logline WHERE text LIKE '%' || :query || '%'")
    fun searchLogline(query: String): Flow<List<Logline>>

    @Query("SELECT * FROM logline WHERE id=:idToSelect LIMIT 1")
    suspend fun selectById(idToSelect: Int): Logline
}
