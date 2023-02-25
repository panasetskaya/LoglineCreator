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

    private var currentPronoun: String? = null
    private var currentMajorEvent: String? = null
    private var currentStoryGoal: String? = null
    private var currentMajorEventIncludesMainCharacter: String? = null
    private var currentCharacterInfo: String? = null
    private var currentTheme: String? = null
    private var currentMprEvent: String? = null
    private var currentAfterMprEvent: String? = null
    private var currentStakes: String? = null
    private var currentWorldText: String? = null

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

}