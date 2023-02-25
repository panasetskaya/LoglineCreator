package com.panasetskaia.storyarchitectlogline.presentation.creativeActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.panasetskaia.storyarchitectlogline.data.LoglineRepositoryImpl
import com.panasetskaia.storyarchitectlogline.domain.useCases.AddLoglineUseCase
import kotlinx.coroutines.launch

class CreativeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = LoglineRepositoryImpl(application)
    private val addLoglineUseCase = AddLoglineUseCase(repo)

    private var currentPronoun: String = dummyString
    private var currentMajorEvent: String = dummyString
    private var currentStoryGoal: String = dummyString
    private var currentMajorEventIncludesMainCharacter: Boolean = true
    private var currentCharacterInfo: String = dummyString
    private var currentTheme: String? = null
    private var currentMprEvent: String? = null
    private var currentAfterMprEvent: String? = null
    private var currentStakes: String? = null
    private var currentWorldText: String? = null

    fun addLogline(
    ) {
        viewModelScope.launch {
            if (allRequiredFieldsNotNull()) {
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

    private fun allRequiredFieldsNotNull(): Boolean {
        return currentPronoun != dummyString && currentMajorEvent != dummyString &&
                currentStoryGoal != dummyString && currentCharacterInfo != dummyString
    }

    companion object {
        val dummyString = ""
    }

}