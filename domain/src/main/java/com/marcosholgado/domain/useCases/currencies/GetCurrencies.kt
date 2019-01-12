package com.marcosholgado.domain.useCases.currencies

import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.repository.CurrencyRepository
import com.marcosholgado.domain.useCases.UseCase
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

open class GetCurrencies @Inject constructor(
    private val repository: CurrencyRepository,
    private val observeScheduler: Scheduler
) : UseCase<List<Currency>>() {

    override fun execute(observer: DisposableSubscriber<List<Currency>>) {
        val observable = repository.getCurrencies()
            .subscribeOn(Schedulers.io())
            .observeOn(observeScheduler)
        addDisposable(observable.subscribeWith(observer))
    }

}