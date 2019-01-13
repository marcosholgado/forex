package com.marcosholgado.forex.utils

import android.app.Activity
import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.marcosholgado.forex.di.DaggerTestApplicationComponent
import com.marcosholgado.forex.di.TestApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class TestApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Activity>

    private lateinit var appComponent: TestApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerTestApplicationComponent.builder().application(this).build()
        appComponent.inject(this)
    }

    companion object {

        fun appComponent(): TestApplicationComponent {
            return (ApplicationProvider.getApplicationContext<TestApplication>().applicationContext as TestApplication).appComponent
        }

    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return injector
    }

}