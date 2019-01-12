package com.marcosholgado.forex.di

import android.app.Application
import com.marcosholgado.forex.ForexApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        UIModule::class,
        DataModule::class,
        PresentationModule::class,
        NetworkModule::class,
        ThreadModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<ForexApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): ApplicationComponent.Builder

        fun build(): ApplicationComponent
    }
}