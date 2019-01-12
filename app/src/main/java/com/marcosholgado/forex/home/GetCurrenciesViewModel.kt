package com.marcosholgado.forex.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.useCases.currencies.GetCurrencies
import com.marcosholgado.forex.home.model.CurrenciesViewModel
import com.marcosholgado.forex.mapper.CurrencyViewMapper
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

open class GetCurrenciesViewModel @Inject internal constructor(
    private val getCurrencies: GetCurrencies,
    private val mapper: CurrencyViewMapper
) : ViewModel() {

    private val currenciesLiveData: MutableLiveData<CurrenciesViewModel> = MutableLiveData()

    init {
        fetchCurrencies()
    }

    override fun onCleared() {
        getCurrencies.dispose()
        super.onCleared()
    }

    fun getCurrencies(): LiveData<CurrenciesViewModel> {
        return currenciesLiveData
    }

    private fun fetchCurrencies() {
        currenciesLiveData.postValue(CurrenciesViewModel(null, emptyList()))
        return getCurrencies.execute(CurrenciesSubscriber())
    }

    inner class CurrenciesSubscriber : DisposableSubscriber<List<Currency>>() {

        override fun onComplete() {}

        override fun onNext(t: List<Currency>) {
            currenciesLiveData.postValue(
                CurrenciesViewModel(null,
                    t.map {
                        mapper.mapToView(it)
                    })
            )
        }

        override fun onError(exception: Throwable) {
            currenciesLiveData.postValue(CurrenciesViewModel(null, emptyList()))
        }

    }

}