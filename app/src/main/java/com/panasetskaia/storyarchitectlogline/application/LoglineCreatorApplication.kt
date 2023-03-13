package com.panasetskaia.storyarchitectlogline.application

import android.app.Application
import com.panasetskaia.storyarchitectlogline.di.DaggerAppComponent

class LoglineCreatorApplication: Application() {

    val component by lazy {
        DaggerAppComponent.factory().create(this)
    }


}