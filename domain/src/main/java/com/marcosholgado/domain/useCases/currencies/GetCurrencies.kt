package com.marcosholgado.domain.useCases.currencies

import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.repository.CurrencyRepository
import com.marcosholgado.domain.useCases.UseCase
import io.reactivex.Flowable
import io.reactivex.Scheduler
import javax.inject.Inject

open class GetCurrencies @Inject constructor(
    private val repository: CurrencyRepository,
    observeScheduler: Scheduler
) : UseCase<List<Currency>>(observeScheduler) {

    override fun createObservable(): Flowable<List<Currency>> {
        return repository.getCurrencies()
    }

}