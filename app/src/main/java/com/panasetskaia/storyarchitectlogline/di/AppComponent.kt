package com.panasetskaia.storyarchitectlogline.di

import android.app.Application
import com.panasetskaia.storyarchitectlogline.presentation.creativeActivity.CreativeActivity
import com.panasetskaia.storyarchitectlogline.presentation.mainActivity.MainActivity
import dagger.BindsInstance
import dagger.Component

@LoglineAppScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
    fun inject(activity: CreativeActivity)

    @Component.Factory
    interface AppComponentFactory {
        fun create(@BindsInstance application: Application): AppComponent
    }

}