package com.marcosholgado.domain.useCases

import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber


abstract class UseCase<T> constructor(private val observeScheduler: Scheduler) {
    private val disposables = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun dispose() {
        if (!disposables.isDisposed) disposables.dispose()
    }

    open fun execute(observer: DisposableSubscriber<T>) {
        val observable = this.createObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(observeScheduler)
        addDisposable(observable.subscribeWith(observer))
    }

    abstract fun createObservable(): Flowable<T>
}