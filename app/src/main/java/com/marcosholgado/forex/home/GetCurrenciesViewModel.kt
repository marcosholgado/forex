package com.marcosholgado.forex.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.useCases.currencies.GetCurrencies
import com.marcosholgado.domain.useCases.currencies.UpdateExchangeRate
import com.marcosholgado.forex.home.model.CurrenciesViewModel
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
        fetchCurrencies()
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
        this.baseRate = baseRate
        updateExchangeRate.execute(CurrenciesSubscriber(), this.baseRate, currencyList)
    }

    fun getCurrencies(baseRate: Float): LiveData<CurrenciesViewModel> {
        this.baseRate = baseRate
        return currenciesLiveData
    }

    private fun fetchCurrencies() {
        currenciesLiveData.postValue(CurrenciesViewModel(null, emptyList()))
        return getCurrencies.execute(CurrenciesSubscriber(), baseRate, null)
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