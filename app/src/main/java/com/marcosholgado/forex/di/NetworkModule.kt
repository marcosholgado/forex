package com.marcosholgado.forex.di

import com.marcosholgado.forex.network.ForexService
import com.marcosholgado.forex.network.ForexServiceFactory
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
abstract class NetworkModule {

    /**
     * This companion object annotated as a module is necessary in order to provide dependencies
     * statically in case the wrapping module is an abstract class (to use binding)
     */
    @Module
    companion object {
        @Provides
        @JvmStatic
        fun provideForexService(): ForexService = ForexServiceFactory.createForexService()

        @Provides
        @JvmStatic
        @Named("base")
        fun provideBaseCurrency(): String = "EUR"

        @Provides
        @JvmStatic
        @Named("currencies")
        fun provideCurrencies(): String = "USD,JPY,GBP,AUD,CAD,CHF,CNY,SEK,NZD"
    }
}