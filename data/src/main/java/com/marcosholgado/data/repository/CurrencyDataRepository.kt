package com.marcosholgado.data.repository

import com.marcosholgado.data.mapper.CurrencyDateMapper
import com.marcosholgado.data.mapper.CurrencyMapper
import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.model.CurrencyDate
import com.marcosholgado.domain.repository.CurrencyRepository
import io.reactivex.Flowable
import javax.inject.Inject

class CurrencyDataRepository @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val currencyMapper: CurrencyMapper,
    private val currencyDateMapper: CurrencyDateMapper
) : CurrencyRepository {

    override fun getCurrencies(): Flowable<List<Currency>> {
        return remoteRepository.getCurrencies().flatMap {
            Flowable.just(it.map { currencyMapper.mapFromEntity(it) })
        }
    }

    override fun compareCurrencies(date: String, symbols: String): Flowable<CurrencyDate> {
        return remoteRepository.compareCurrencies(date, symbols).flatMap {
            Flowable.just(currencyDateMapper.mapFromEntity(it))
        }
    }
}