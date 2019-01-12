package com.marcosholgado.domain.useCases.currencies

import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.useCases.UseCase
import io.reactivex.Flowable
import io.reactivex.Scheduler
import javax.inject.Inject

open class UpdateExchangeRate @Inject constructor(
    observeScheduler: Scheduler
) : UseCase<List<Currency>, Float, List<Currency>>(observeScheduler) {

    override fun createObservable(
        param1: Float?,
        param2: List<Currency>?
    ): Flowable<List<Currency>> {
        if (param1 == 0f) {
            return Flowable.error(Throwable("Base rate cannot be 0"))
        }
        return Flowable.fromArray(param2)
            .map {
                val currencies = mutableListOf<Currency>()
                it.forEach { cur ->
                    currencies.add(Currency(cur.currency, cur.rate * (param1 ?: 1f)))
                }
                currencies
            }
    }

}