package com.marcosholgado.domain.repository

import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.model.CurrencyDate
import io.reactivex.Flowable

interface CurrencyRepository {
    fun getCurrencies(): Flowable<List<Currency>>

    fun compareCurrencies(date: String, symbols: String): Flowable<CurrencyDate>
}