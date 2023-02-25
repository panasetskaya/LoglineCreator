package com.panasetskaia.storyarchitectlogline.data

import android.app.Application
import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import kotlinx.coroutines.flow.Flow

class LoglineRepositoryImpl(application: Application) : LoglineRepository {

    private val dbDao = LoglineDatabase.getInstance(application).loglineDao()

    override fun getAllSavedLoglines(): Flow<List<Logline>> {
        return dbDao.getAllLoglines()
    }

    override fun searchForWords(query: String): Flow<List<Logline>> {
        return dbDao.searchLogline(query)
    }

    override suspend fun deleteLogline(id: Int) {
        dbDao.deleteLogline(id)
    }

    override suspend fun addLogline(
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
    ) {
        val newLogline = LoglineBuilder(
            pronoun,
            majorEvent,
            storyGoal,
            majorEventIncludesMainCharacter,
            characterInfo,
            theme,
            mprEvent,
            afterMprEvent,
            stakes,
            worldText,
            getCurrentDate(),
            getPositionForLast()
        ).buildLogline()
        dbDao.saveLogline(newLogline)
    }


    override suspend fun changeOrder(id: Int, newPosition: Int) {
        val oldLogline = dbDao.selectById(id)
        val newLogline = oldLogline.copy(number = newPosition)
        dbDao.saveLogline(newLogline)
    }

    override suspend fun changeText(id: Int, newText: String) {
        val wordCount = newText.count {
            it == ' '
        } + 1
        val oldLogline = dbDao.selectById(id)
        val newLogline =
            oldLogline.copy(
                text = newText,
                date = getCurrentDate(),
                number = getPositionForLast(),
                count = wordCount
            )
        dbDao.saveLogline(newLogline)
    }

    private fun getCurrentDate(): String {
        return "00.00.0000"
        //todo: Not yet implemented
    }

    private fun getPositionForLast(): Int {
        return 0
        //todo: Not yet implemented
    }
}