package com.panasetskaia.storyarchitectlogline.presentation.mainActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.storyarchitectlogline.data.LoglineRepositoryImpl
import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.useCases.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = LoglineRepositoryImpl(application)
    private val addLoglineUseCase = AddLoglineUseCase(repo)
    private val deleteLoglineUseCase = DeleteLoglineUseCase(repo)
    private val getAllUseCase = GetAllUseCase(repo)
    private val searchUseCase = SearchUseCase(repo)
    private val changeOrderUseCase = ChangeOrderUseCase(repo)

    private val _loglinesFlow = MutableSharedFlow<List<Logline>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val loglinesFlow: SharedFlow<List<Logline>>
        get() = _loglinesFlow


    fun getLatestLoglines() {
        viewModelScope.launch {
            _loglinesFlow.emitAll(
                getAllUseCase()
            )
        }
    }

    fun searchInLoglines(query: String) {
        viewModelScope.launch {
            _loglinesFlow.emitAll(
                searchUseCase(query)
            )
        }
    }

    fun deleteLogline(id: Int) {
        viewModelScope.launch {
            deleteLoglineUseCase(id)
        }
    }

    fun addLogline(
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
        viewModelScope.launch {
            addLoglineUseCase(
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
            )
        }
    }

    fun changeOrder(id: Int, newPosition: Int) {
        viewModelScope.launch {
            changeOrderUseCase(id, newPosition)
        }
    }
}