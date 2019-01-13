package com.marcosholgado.forex.di.modules

import com.marcosholgado.data.repository.RemoteRepository
import com.marcosholgado.domain.repository.CurrencyRepository
import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object TestDataModule {
    @Provides
    @JvmStatic
    @Singleton
    fun provideRemoteRepository(): RemoteRepository {
        return mock()
    }

    @Provides
    @JvmStatic
    @Singleton
    fun provideCurrencyRepository(): CurrencyRepository {
        return mock()
    }
}
