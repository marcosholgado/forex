package com.marcosholgado.domain.repository

import com.marcosholgado.domain.model.Currency
import io.reactivex.Flowable

interface CurrencyRepository {
    fun getCurrencies(): Flowable<List<Currency>>
}