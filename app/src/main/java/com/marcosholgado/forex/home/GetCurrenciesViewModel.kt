package com.marcosholgado.forex.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.useCases.currencies.GetCurrencies
import com.marcosholgado.domain.useCases.currencies.UpdateExchangeRate
import com.marcosholgado.forex.home.model.CurrenciesViewModel
import com.marcosholgado.forex.home.model.State
import com.marcosholgado.forex.mapper.CurrencyViewMapper
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

open class GetCurrenciesViewModel @Inject internal constructor(
    private val getCurrencies: GetCurrencies,
    private val updateExchangeRate: UpdateExchangeRate,
    private val mapper: CurrencyViewMapper
) : ViewModel() {

    private val currenciesLiveData: MutableLiveData<CurrenciesViewModel> = MutableLiveData()
    private var baseRate = 1f

    init {
        fetchCurrencies(baseRate)
    }

    override fun onCleared() {
        getCurrencies.dispose()
        super.onCleared()
    }

    fun updateCurrenciesRate(baseRate: Float) {
        val currenciesViewModel = currenciesLiveData.value
        val currencyList = mutableListOf<Currency>()
        currenciesViewModel?.let {
            it.currencies.forEach {
                currencyList.add(mapper.mapFromView(it, this.baseRate))
            }
        }
        if (baseRate > 0) {
            this.baseRate = baseRate
        }
        if (currencyList.isNotEmpty()) {
            updateExchangeRate.execute(CurrenciesSubscriber(), baseRate, currencyList)
        }
    }

    fun getCurrencies(): LiveData<CurrenciesViewModel> {
        return currenciesLiveData
    }

    fun fetchCurrencies(baseRate: Float) {
        currenciesLiveData.postValue(CurrenciesViewModel(null, emptyList(), State.LOADING))
        return getCurrencies.execute(CurrenciesSubscriber(), baseRate, null)
    }

    inner class CurrenciesSubscriber : DisposableSubscriber<List<Currency>>() {

        override fun onComplete() {}

        override fun onNext(t: List<Currency>) {
            currenciesLiveData.postValue(
                CurrenciesViewModel(null,
                    t.map {
                        mapper.mapToView(it)
                    }, State.SUCCESS
                )
            )
        }
        override fun onError(exception: Throwable) {
            val currenciesViewModel = currenciesLiveData.value
            currenciesLiveData.postValue(
                CurrenciesViewModel(
                    null,
                    currenciesViewModel!!.currencies,
                    State.ERROR,
                    exception.message
                )
            )
        }
    }

}