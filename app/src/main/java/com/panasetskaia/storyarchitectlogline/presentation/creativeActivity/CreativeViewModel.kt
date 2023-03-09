package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.storyarchitectlogline.data.LoglineRepositoryImpl
import com.panasetskaia.storyarchitectlogline.domain.Logline
import com.panasetskaia.storyarchitectlogline.domain.useCases.AddLoglineUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.ChangeTextUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.GetLastSavedUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CreativeViewModel(application: Application) : AndroidViewModel(application) {


    private val repo = LoglineRepositoryImpl(application)
    private val addLoglineUseCase = AddLoglineUseCase(repo)
    private val getLastSavedUseCase = GetLastSavedUseCase(repo)
    private val changeTextUseCase = ChangeTextUseCase(repo)

    private val _lastLoglineFlow = MutableSharedFlow<Logline>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val lastLoglineFlow: SharedFlow<Logline>
        get() = _lastLoglineFlow

    private val _isSwipingFromPageOneAllowed = MutableStateFlow(false)
    val isSwipingFromPageOneAllowed: StateFlow<Boolean>
        get() = _isSwipingFromPageOneAllowed

    private val _isSwipingFromPageTwoAllowed = MutableStateFlow(true)
    val isSwipingFromPageTwoAllowed: StateFlow<Boolean>
        get() = _isSwipingFromPageTwoAllowed

    private val _isSwipingFromPageThreeAllowed = MutableStateFlow(true)
    val isSwipingFromPageThreeAllowed: StateFlow<Boolean>
        get() = _isSwipingFromPageThreeAllowed

    private val _isSwipingFromPageFourAllowed = MutableStateFlow(false)
    val isSwipingFromPageFourAllowed: StateFlow<Boolean>
        get() = _isSwipingFromPageFourAllowed

    private val _isSwipingFromPageFiveAllowed = MutableStateFlow(true)
    val isSwipingFromPageFiveAllowed: StateFlow<Boolean>
        get() = _isSwipingFromPageFiveAllowed

    private val _isSwipingFromPageSixAllowed = MutableStateFlow(true)
    val isSwipingFromPageSixAllowed: StateFlow<Boolean>
        get() = _isSwipingFromPageSixAllowed

    private val _isSwipingFromPageSevenAllowed = MutableStateFlow(true)
    val isSwipingFromPageSevenAllowed: StateFlow<Boolean>
        get() = _isSwipingFromPageSevenAllowed

    private var currentPronoun: String = initialState
    private var currentCharacterInfo: String = initialState
    private var currentMajorEvent: String = initialState
    private var currentStoryGoal: String = initialState
    private var currentMajorEventIncludesMainCharacter: Boolean = false
    private var currentIsThemeNeeded: Boolean = false
    private var currentTheme: String? = null
    private var currentIsMprNeeded: Boolean = false
    private var currentMprEvent: String? = null
    private var currentAfterMprEvent: String? = null
    private var currentIsWorldNeeded = false
    private var currentStoryWorld: String? = null
    private var currentAreStakesNeeded = false
    private var currentStakes: String? = null

    fun saveLogline() {
        viewModelScope.launch {
            if (allRequiredFieldsNotEmpty()) {
                Log.e("MY_TAG", "allRequiredFieldsNotEmpty, saving logline!")
                addLoglineUseCase(
                    currentPronoun,
                    currentMajorEvent,
                    currentStoryGoal,
                    currentMajorEventIncludesMainCharacter,
                    currentCharacterInfo,
                    currentTheme,
                    currentMprEvent,
                    currentAfterMprEvent,
                    currentStakes,
                    currentStoryWorld
                )
            }
        }
    }

    fun getLastLogline() {
        viewModelScope.launch {
            _lastLoglineFlow.emitAll(
                getLastSavedUseCase()
            )
        }
    }

    private fun allRequiredFieldsNotEmpty(): Boolean {
        return currentPronoun != initialState &&
                currentStoryGoal != initialState &&
                currentCharacterInfo != initialState
        //todo: дописать свитчеры
    }


    fun changeCharacterInfo(newCharacterInfo: String) {
        currentCharacterInfo = newCharacterInfo
        emitSwipingFromPage1Admission()
    }

    fun changePronoun(pronounPosition: Int) {
        currentPronoun = when (pronounPosition) {
            genderMalePosition -> genderMale
            genderFemalePosition -> genderFemale
            genderOtherPosition -> genderOther
            else -> initialState
        }
        emitSwipingFromPage1Admission()
    }

    private fun emitSwipingFromPage1Admission() {
        if (currentCharacterInfo != initialState && currentPronoun != initialState) {
            _isSwipingFromPageOneAllowed.tryEmit(true)
        } else {
            _isSwipingFromPageOneAllowed.tryEmit(false)
        }
    }

    fun changeMajorEvent(event: String) {
        currentMajorEvent = event
        emitSwipingFromPage2Admission()
    }

    fun changeIsMCIncludedSwitch(isIncluded: Boolean) {
        currentMajorEventIncludesMainCharacter = isIncluded
        emitSwipingFromPage2Admission()
    }

    private fun emitSwipingFromPage2Admission() {
        if (currentMajorEvent==initialState && currentMajorEventIncludesMainCharacter) {
            _isSwipingFromPageTwoAllowed.tryEmit(false)
        } else {
            _isSwipingFromPageTwoAllowed.tryEmit(true)
        }
    }

    fun changeIsThemeNeeded(needed: Boolean) {
        currentIsThemeNeeded = needed
        emitSwipingFromPage3Admission()
    }

    fun changeTheme(theme: String) {
        currentTheme = theme
        emitSwipingFromPage3Admission()
    }

    private fun emitSwipingFromPage3Admission() {
        if (currentIsThemeNeeded && currentTheme==null) {
            _isSwipingFromPageThreeAllowed.tryEmit(false)
        } else {
            _isSwipingFromPageThreeAllowed.tryEmit(true)
        }
    }

    fun changeStoryGoal(newGoal: String) {
        currentStoryGoal = newGoal
        emitSwipingFromPage4Admission()
    }

    private fun emitSwipingFromPage4Admission() {
        if (currentStoryGoal!= initialState) {
            _isSwipingFromPageFourAllowed.tryEmit(true)
        } else {
            _isSwipingFromPageFourAllowed.tryEmit(false)
        }
    }

    fun changeIsMprNeeded(needed: Boolean) {
        currentIsMprNeeded = needed
        emitSwipingFromPage5Admission()
    }

    fun changeMidPoint(newMidPoint: String) {
        currentMprEvent = newMidPoint
        emitSwipingFromPage5Admission()
    }

    fun changeAfterMpr(newAfterMprEvent: String) {
        currentAfterMprEvent = newAfterMprEvent
        emitSwipingFromPage5Admission()
    }

    private fun emitSwipingFromPage5Admission() {
        if (currentIsMprNeeded && (currentMprEvent==null || currentAfterMprEvent==null )) {
            _isSwipingFromPageFiveAllowed.tryEmit(false)
        } else {
            _isSwipingFromPageFiveAllowed.tryEmit(true)
        }
    }

    fun changeIsStoryWorldNeeded(needed: Boolean) {
        currentIsWorldNeeded = needed
        emitSwipingFromPage6Admission()
    }

    fun changeStoryWorld(newWorld: String) {
        currentStoryWorld = newWorld
        emitSwipingFromPage6Admission()
    }

    private fun emitSwipingFromPage6Admission() {
        if (currentIsWorldNeeded && currentStoryWorld==null) {
            _isSwipingFromPageSixAllowed.tryEmit(false)
        } else {
            _isSwipingFromPageSixAllowed.tryEmit(true)
        }
    }

    fun changeAreStakesNeeded(needed: Boolean) {
        currentAreStakesNeeded = needed
        emitSwipingFromPage7Admission()
    }

    fun changeStakes(newStakes: String) {
        currentStakes = newStakes
        emitSwipingFromPage7Admission()
    }

    private fun emitSwipingFromPage7Admission() {
        if (currentAreStakesNeeded && currentStakes==null) {
            _isSwipingFromPageSevenAllowed.tryEmit(false)
        } else {
            _isSwipingFromPageSevenAllowed.tryEmit(true)
        }
    }

    fun editLoglineText(id: Int, newText: String) {
        viewModelScope.launch {
            changeTextUseCase(id,newText)
        }
    }

    companion object {
        private const val initialState = ""
        private const val genderMalePosition = 1
        private const val genderFemalePosition = 2
        private const val genderOtherPosition = 3
        private const val genderMale = "he"
        private const val genderFemale = "she"
        private const val genderOther = "they"
    }

}