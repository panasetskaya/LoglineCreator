package com.panasetskaia.storyarchitectlogline.domain.useCases

import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository

class AddLoglineUseCase(private val repo: LoglineRepository) {

    suspend operator fun invoke(
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
        repo.addLogline(
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