package com.panasetskaia.storyarchitectlogline.presentation.mainActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.panasetskaia.storyarchitectlogline.data.LoglineRepositoryImpl
import com.panasetskaia.storyarchitectlogline.domain.useCases.AddLoglineUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.DeleteLoglineUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.GetAllUseCase
import com.panasetskaia.storyarchitectlogline.domain.useCases.SearchUseCase

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val repo = LoglineRepositoryImpl(application)
    private val addLoglineUseCase = AddLoglineUseCase(repo)
    private val deleteLoglineUseCase = DeleteLoglineUseCase(repo)
    private val getAllUseCase = GetAllUseCase(repo)
    private val searchUseCase = SearchUseCase(repo)
}