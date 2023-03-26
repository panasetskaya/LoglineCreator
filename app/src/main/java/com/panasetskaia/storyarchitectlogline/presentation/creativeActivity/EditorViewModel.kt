package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.storyarchitectlogline.domain.useCases.ChangeTextUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.GetGeneratedLoglineTextUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.GetLoglineByIdUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.SaveNewLoglineUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditorViewModel @Inject constructor(
    private val getGeneratedLoglineTextUseCase: GetGeneratedLoglineTextUseCase,
    private val changeTextUseCase: ChangeTextUseCase,
    private val getLoglineByIdUseCase: GetLoglineByIdUseCase,
    private val saveNewLoglineUseCase: SaveNewLoglineUseCase
) : ViewModel() {

    private var editedLoglineText = initialState
    private var editedLoglineId = nullId

    private val _editedLoglineFlow = MutableSharedFlow<String?>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val editedLoglineFlow: SharedFlow<String?>
        get() = _editedLoglineFlow

    fun getGeneratedLoglineText() {
        viewModelScope.launch {
            val generated = getGeneratedLoglineTextUseCase()
            _editedLoglineFlow.emitAll(generated)
        }
    }

    fun getLoglineById(id: Int) {
        viewModelScope.launch {
            val foundLogline = getLoglineByIdUseCase(id)
            _editedLoglineFlow.emit(foundLogline.text)
        }
    }

    fun editLoglineText(id: Int?, newText: String?) {
        newText?.let {
            editedLoglineText = it
        }
        id?.let {
            editedLoglineId = it
        }
    }

    fun saveChangedLogline() {
        if (editedLoglineText != initialState && editedLoglineId != nullId) {
            viewModelScope.launch {
                changeTextUseCase(editedLoglineId, editedLoglineText)
            }
        }
    }

    fun saveNewLogline() {
        viewModelScope.launch {
            saveNewLoglineUseCase(editedLoglineText)
        }
    }
    //todo: навесить эту функцию на все кнопки!

    companion object {
        private const val initialState = ""
        private const val nullId = -1
    }
}