package com.marcosholgado.domain.useCases

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


abstract class UseCase<T, in P, in O> constructor(private val observeScheduler: Scheduler) {
    private val disposables = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }

    open fun execute(observer: DisposableSubscriber<T>, param1: P? = null, param2: O?) {
        val observable = this.createObservable(param1, param2)
            .subscribeOn(Schedulers.io())
            .observeOn(observeScheduler)
        addDisposable(observable.subscribeWith(observer))
    }

    abstract fun createObservable(param1: P? = null, param2: O? = null): Flowable<T>
}