package com.marcosholgado.data.repository

import com.marcosholgado.data.model.CurrencyEntity
import io.reactivex.Flowable

interface RemoteRepository {
    fun getCurrencies(): Flowable<List<CurrencyEntity>>
}