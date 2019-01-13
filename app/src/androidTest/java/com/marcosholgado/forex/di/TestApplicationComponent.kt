package com.marcosholgado.forex.di

import android.app.Application
import com.marcosholgado.domain.repository.CurrencyRepository
import com.marcosholgado.forex.di.modules.TestApplicationModule
import com.marcosholgado.forex.di.modules.TestDataModule
import com.marcosholgado.forex.di.modules.TestNetworkModule
import com.marcosholgado.forex.di.modules.TestThreadModule
import com.marcosholgado.forex.utils.TestApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        UIModule::class,
        TestDataModule::class,
        PresentationModule::class,
        TestNetworkModule::class,
        TestThreadModule::class,
        TestApplicationModule::class]
)
interface TestApplicationComponent : ApplicationComponent {

    fun currencyRepository(): CurrencyRepository

    fun inject(application: TestApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): TestApplicationComponent.Builder

        fun build(): TestApplicationComponent
    }

}