package com.marcosholgado.forex.di

import com.marcosholgado.forex.home.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}