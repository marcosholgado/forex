package com.marcosholgado.forex.network

import com.marcosholgado.data.model.CurrencyEntity
import com.marcosholgado.data.repository.RemoteRepository
import com.marcosholgado.forex.mapper.CurrencyEntityMapper
import io.reactivex.Flowable
import javax.inject.Inject

class RemoteRepositoryImp @Inject constructor(
    private val mapper: CurrencyEntityMapper,
    private val forexService: ForexService
) : RemoteRepository {

    override fun getCurrencies(): Flowable<List<CurrencyEntity>> {
        return forexService.getCurrencies("EUR", "USD,JPY,GBP,AUD,CAD,CHF,CNY,SEK,NZD")
            .map {
                val currencies = mutableListOf<CurrencyEntity>()
                it.rates.forEach { (key, value) ->
                    currencies.add(mapper.mapFromRemote(Pair(key, value)))
                }
                currencies
            }
    }

}