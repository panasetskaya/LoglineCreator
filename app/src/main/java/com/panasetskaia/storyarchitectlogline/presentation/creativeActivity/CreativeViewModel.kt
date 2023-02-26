package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.storyarchitectlogline.data.LoglineRepositoryImpl
import com.panasetskaia.storyarchitectlogline.domain.useCases.AddLoglineUseCase
import kotlinx.coroutines.launch

class CreativeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = LoglineRepositoryImpl(application)
    private val addLoglineUseCase = AddLoglineUseCase(repo)

    private var currentPronoun: String = dummy
    private var currentCharacterInfo: String = dummy
    private var currentMajorEvent: String = dummy
    private var currentStoryGoal: String = dummy
    private var currentMajorEventIncludesMainCharacter: Boolean = true
    private var currentTheme: String? = null
    private var currentMprEvent: String? = null
    private var currentAfterMprEvent: String? = null
    private var currentStakes: String? = null
    private var currentWorldText: String? = null

    fun saveLogline() {
        viewModelScope.launch {
            if (allRequiredFieldsNotEmpty()) {
                Log.e("MY_TAG", "allRequiredFieldsNotEmpty")
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
        return currentPronoun != initialState && currentMajorEvent != initialState &&
                currentStoryGoal != initialState && currentCharacterInfo != initialState
    }

    fun characterInfoIsFilled(): Boolean {
        return currentCharacterInfo!= initialState
    }

    fun changeCharacterInfo(newCharacterInfo: String) {
        currentCharacterInfo = newCharacterInfo
    }

    fun pronounIsSet(): Boolean {
        return currentPronoun!= initialState
    }

    fun changePronoun(pronounPosition: Int) {
        currentPronoun = when (pronounPosition) {
            genderMalePosition -> genderMale
            genderFemalePosition -> genderFemale
            genderOtherPosition -> genderOther
            else -> initialState
        }
    }

    companion object {
        private const val initialState = ""
        private const val dummy = "dududuudud"
        private const val genderMalePosition = 1
        private const val genderFemalePosition = 2
        private  const val genderOtherPosition = 3
        private const val genderMale = "he"
        private const val genderFemale = "she"
        private const val genderOther = "they"
    }

}