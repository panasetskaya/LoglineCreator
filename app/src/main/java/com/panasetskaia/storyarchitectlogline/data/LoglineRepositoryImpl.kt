package com.panasetskaia.storyarchitectlogline.data

import android.util.Log
import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class LoglineRepositoryImpl @Inject constructor(
    private val dbDao: LoglineDao
) : LoglineRepository {

    private val newLoglineText = MutableStateFlow<String?>(null)

    override fun getAllSavedLoglines(): Flow<List<Logline>> {
        return dbDao.getAllLoglines()
    }

    override fun getGeneratedLoglineText(): Flow<String?> {
        return newLoglineText
    }

    override suspend fun getLoglineById(id: Int): Logline {
        return withContext(Dispatchers.IO) {
            dbDao.selectById(id)
        }
    }

    override fun searchForWords(query: String): Flow<List<Logline>> {
        return dbDao.searchLogline(query)
    }

    override suspend fun deleteLogline(id: Int) {
        withContext(Dispatchers.IO) {
            dbDao.deleteLogline(id)
        }
    }

    override fun buildLogline(
        pronoun: String,
        majorEvent: String?,
        storyGoal: String,
        majorEventIncludesMainCharacter: Boolean,
        characterInfo: String,
        theme: String?,
        mprEvent: String?,
        afterMprEvent: String?,
        stakes: String?,
        worldText: String?
    ) {
        val newText = LoglineBuilder(
            pronoun,
            majorEvent,
            storyGoal,
            majorEventIncludesMainCharacter,
            characterInfo,
            theme,
            mprEvent,
            afterMprEvent,
            stakes,
            worldText
        ).buildLogline()
        newLoglineText.tryEmit(newText)
    }

    override suspend fun changeText(id: Int, newText: String) {
        withContext(Dispatchers.IO) {
            val wordCount = newText.count {
                it == ' '
            } + 1
            val oldLogline = dbDao.selectById(id)
            Log.e("MY_TAG", "oldLogline id is: ${oldLogline}")
            val newLogline =
                oldLogline.copy(
                    text = newText,
                    date = getCurrentDate(),
                    count = wordCount
                )
            dbDao.saveLogline(newLogline)
        }
    }

    override suspend fun saveNewLogline(s: String) {
        withContext(Dispatchers.IO) {
            val newLogline = Logline(
                idForNewLogline,
                s,
                getCurrentDate(),
                countWords(s)
            )
            dbDao.saveLogline(newLogline)
        }
    }

    private fun getCurrentDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(datePattern)
        return current.format(formatter)
    }

    private fun countWords(s: String): Int {
        return s.count {
            it == ' '
        } + 1
    }

    companion object {
        private const val idForNewLogline = 0
        private const val datePattern = "yyyy-MM-dd"
    }
}