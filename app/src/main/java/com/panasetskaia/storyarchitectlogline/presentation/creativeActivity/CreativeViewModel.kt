package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.storyarchitectlogline.data.LoglineRepositoryImpl
import com.panasetskaia.storyarchitectlogline.domain.useCases.AddLoglineUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreativeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = LoglineRepositoryImpl(application)
    private val addLoglineUseCase = AddLoglineUseCase(repo)

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

    private var currentPronoun: String = initialState
    private var currentCharacterInfo: String = initialState
    private var currentMajorEvent: String = initialState
    private var currentStoryGoal: String = dummy
    private var currentMajorEventIncludesMainCharacter: Boolean = false
    private var currentIsThemeNeeded: Boolean = false
    private var currentTheme: String? = null
    private var currentMprEvent: String? = null
    private var currentAfterMprEvent: String? = null
    private var currentStakes: String? = null
    private var currentWorldText: String? = null

    fun saveLogline() {
        viewModelScope.launch {
            if (allRequiredFieldsNotEmpty()) {
                Log.e("MY_TAG", "$currentPronoun, $currentMajorEvent, $currentCharacterInfo")
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
                    currentWorldText
                )
            }

        }
    }

    private fun allRequiredFieldsNotEmpty(): Boolean {
        return currentPronoun != initialState &&
                currentStoryGoal != initialState &&
                currentCharacterInfo != initialState
        //дописать свитчеры
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

    companion object {
        private const val initialState = ""
        private const val dummy = "dududuudud"
        private const val genderMalePosition = 1
        private const val genderFemalePosition = 2
        private const val genderOtherPosition = 3
        private const val genderMale = "he"
        private const val genderFemale = "she"
        private const val genderOther = "they"
    }

}