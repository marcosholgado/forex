package com.marcosholgado.forex.di

import com.marcosholgado.data.repository.CurrencyDataRepository
import com.marcosholgado.data.repository.RemoteRepository
import com.marcosholgado.domain.repository.CurrencyRepository
import com.marcosholgado.forex.network.RemoteRepositoryImp
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun bindCurrencyRepository(currencyRepository: RemoteRepositoryImp): RemoteRepository

    @Binds
    abstract fun bindCurrencyDataRepository(currencyDataRepository: CurrencyDataRepository): CurrencyRepository
}