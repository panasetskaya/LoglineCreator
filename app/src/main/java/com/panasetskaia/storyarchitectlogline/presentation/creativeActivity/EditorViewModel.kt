package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.useCases.ChangeTextUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.GetLastSavedUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.GetLoglineByIdUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditorViewModel @Inject constructor(
    private val getLastSavedUseCase: GetLastSavedUseCase,
    private val changeTextUseCase: ChangeTextUseCase,
    private val getLoglineByIdUseCase: GetLoglineByIdUseCase

) : ViewModel() {

    private var editedLoglineText = initialState
    private var editedLoglineId = nullId

    private val _editedLoglineFlow = MutableSharedFlow<Logline>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val editedLoglineFlow: SharedFlow<Logline>
        get() = _editedLoglineFlow

    fun getLastLogline() {
        viewModelScope.launch {
            val lastLogline = getLastSavedUseCase()
            _editedLoglineFlow.emit(lastLogline)
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
        if (editedLoglineText != initialState && editedLoglineId != nullId) {
            viewModelScope.launch {
                changeTextUseCase(editedLoglineId, editedLoglineText)
            }
        }
    }

    companion object {
        private const val initialState = ""
        private const val nullId = -1
    }
}