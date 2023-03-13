package com.panasetskaia.storyarchitectlogline.presentation.mainActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.useCases.DeleteLoglineUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.GetAllUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.SearchUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val deleteLoglineUseCase: DeleteLoglineUseCase,
    private val getAllUseCase: GetAllUseCase,
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _loglinesFlow = MutableSharedFlow<List<Logline>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val loglinesFlow: SharedFlow<List<Logline>>
        get() = _loglinesFlow

    init {
        getLatestLoglines()
    }


    private fun getLatestLoglines() {
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


}