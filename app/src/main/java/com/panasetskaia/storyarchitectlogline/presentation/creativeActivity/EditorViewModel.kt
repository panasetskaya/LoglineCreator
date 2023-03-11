package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.storyarchitectlogline.data.LoglineRepositoryImpl
import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.useCases.ChangeTextUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.GetLastSavedUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.GetLoglineByIdUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.launch

class EditorViewModel (application: Application) : AndroidViewModel(application) {

    private var editedLoglineText = initialState
    private var editedLoglineId = nullId


    private val repo = LoglineRepositoryImpl(application)
    private val getLastSavedUseCase = GetLastSavedUseCase(repo)
    private val changeTextUseCase = ChangeTextUseCase(repo)
    private val getLoglineByIdUseCase = GetLoglineByIdUseCase(repo)

    private val _editedLoglineFlow = MutableSharedFlow<Logline>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val editedLoglineFlow: SharedFlow<Logline>
        get() = _editedLoglineFlow

    fun getLastLogline() {
        viewModelScope.launch {
            delay(100)
            _editedLoglineFlow.emitAll(
                getLastSavedUseCase()
            )
        }
    }

    fun getLoglineById(id: Int) {
        viewModelScope.launch {
            val foundLogline = getLoglineByIdUseCase(id)
            _editedLoglineFlow.emit(foundLogline)
        }
    }

    fun editLoglineText(id: Int, newText: String) {
        editedLoglineText = newText
        editedLoglineId = id
    }

    fun saveChangedLogline() {
        if (editedLoglineText!= initialState && editedLoglineId!= nullId) {
            viewModelScope.launch {
                changeTextUseCase(editedLoglineId,editedLoglineText)
            }
        }
    }

    companion object {
        private const val initialState = ""
        private const val nullId = -1
    }
}