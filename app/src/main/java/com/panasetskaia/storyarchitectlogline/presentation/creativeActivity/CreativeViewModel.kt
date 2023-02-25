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
    private var currentMajorEvent: String = dummy
    private var currentStoryGoal: String = dummy
    private var currentMajorEventIncludesMainCharacter: Boolean = true
    private var currentCharacterInfo: String = dummy
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

    companion object {
        val initialState = ""
        val dummy = "dududuudud"
    }

}