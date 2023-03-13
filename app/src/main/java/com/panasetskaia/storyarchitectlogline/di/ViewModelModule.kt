package com.panasetskaia.storyarchitectlogline.di

import androidx.lifecycle.ViewModel
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeViewModel
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.EditorViewModel
import com.panasetskaia.storyarchitectlogline.presentation.mainActivity.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(impl: MainViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(CreativeViewModel::class)
    fun bindCreativeViewModel(impl: CreativeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditorViewModel::class)
    fun bindEditorViewModel(impl: EditorViewModel): ViewModel
}

