package com.panasetskaia.storyarchitectlogline.di

import android.app.Application
import com.panasetskaia.storyarchitectlogline.data.LoglineDao
import com.panasetskaia.storyarchitectlogline.data.LoglineDatabase
import com.panasetskaia.storyarchitectlogline.data.LoglineRepositoryImpl
import com.panasetskaia.storyarchitectlogline.domain.LoglineRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @LoglineAppScope
    @Binds
    fun bindRepo(repoImpl: LoglineRepositoryImpl): LoglineRepository

    companion object {

        @LoglineAppScope
        @Provides
        fun provideLoglineDao(application: Application): LoglineDao {
            return LoglineDatabase.getInstance(application).loglineDao()
        }

    }

}