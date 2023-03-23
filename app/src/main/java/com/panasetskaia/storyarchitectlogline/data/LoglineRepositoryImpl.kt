package com.panasetskaia.storyarchitectlogline.data

import android.util.Log
import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class LoglineRepositoryImpl @Inject constructor(
//    private val application: Application,
    private val dbDao: LoglineDao
) : LoglineRepository {

    private var newLoglineText = emptyString

    override fun getAllSavedLoglines(): Flow<List<Logline>> {
        return dbDao.getAllLoglines()
    }

    override suspend fun getLastSavedLogline(): Logline {
        return withContext(Dispatchers.IO) {
            val newLogline = Logline(
                idForNewLogline,
                newLoglineText,
                getCurrentDate(),
                countWords(newLoglineText)
            )
            dbDao.saveLogline(newLogline)
            val lastLogline = dbDao.getLastSavedLogline()
            lastLogline
        }
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

    override fun addLogline(
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
        newLoglineText = LoglineBuilder(
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

    private fun getCurrentDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return current.format(formatter)
    }

    private fun countWords(s: String): Int {
        return s.count{
            it == ' '
        } + 1
    }

    companion object {
        private val emptyString = ""
        private val idForNewLogline = 0
    }
}