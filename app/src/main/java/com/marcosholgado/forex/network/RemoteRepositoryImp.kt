package com.marcosholgado.forex.network

import com.marcosholgado.data.model.CurrencyDateEntity
import com.marcosholgado.data.model.CurrencyEntity
import com.marcosholgado.data.repository.RemoteRepository
import com.marcosholgado.forex.mapper.CurrencyDateEntityMapper
import com.marcosholgado.forex.mapper.CurrencyEntityMapper
import io.reactivex.Flowable
import javax.inject.Inject

class RemoteRepositoryImp @Inject constructor(
    private val currencyEntityMapper: CurrencyEntityMapper,
    private val currencyDateEntityMapper: CurrencyDateEntityMapper,
    private val forexService: ForexService
) : RemoteRepository {

    override fun getCurrencies(): Flowable<List<CurrencyEntity>> {
        return forexService.getCurrencies("EUR", "USD,JPY,GBP,AUD,CAD,CHF,CNY,SEK,NZD")
            .map {
                val currencies = mutableListOf<CurrencyEntity>()

                it.error?.let {
                    error(it.info)
                }

                it.rates?.forEach { (key, value) ->
                    currencies.add(currencyEntityMapper.mapFromRemote(Pair(key, value)))
                }
                currencies
            }
    }

    override fun compareCurrencies(date: String, symbols: String): Flowable<CurrencyDateEntity> {
        return forexService.compareCurrencies(date, "EUR", symbols)
            .map {
                it.error?.let {
                    error(it.info)
                }
                currencyDateEntityMapper.mapFromRemote(it)
            }

    }
}