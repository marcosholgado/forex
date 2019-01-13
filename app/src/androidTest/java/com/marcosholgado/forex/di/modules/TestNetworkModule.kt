package com.marcosholgado.forex.di.modules

import com.marcosholgado.forex.network.ForexService
import com.nhaarman.mockitokotlin2.mock
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
object TestNetworkModule {

    @Provides
    @JvmStatic
    @Singleton
    fun provideForexService(): ForexService {
        return mock()
    }

    @Provides
    @JvmStatic
    @Named("base")
    fun provideBaseCurrency(): String = "EUR"

    @Provides
    @JvmStatic
    @Named("currencies")
    fun provideCurrencies(): String = "USD,JPY,GBP,AUD,CAD,CHF,CNY,SEK,NZD"
}