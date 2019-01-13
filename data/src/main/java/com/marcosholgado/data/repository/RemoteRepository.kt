package com.marcosholgado.data.repository

import com.marcosholgado.data.model.CurrencyDateEntity
import com.marcosholgado.data.model.CurrencyEntity
import io.reactivex.Flowable

interface RemoteRepository {
    fun getCurrencies(): Flowable<List<CurrencyEntity>>

    fun compareCurrencies(date: String, symbols: String): Flowable<CurrencyDateEntity>
}