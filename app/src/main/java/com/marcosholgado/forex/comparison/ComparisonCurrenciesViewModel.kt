package com.marcosholgado.forex.comparison

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marcosholgado.domain.model.CurrencyDate
import com.marcosholgado.domain.useCases.comparison.CompareCurrencies
import com.marcosholgado.forex.common.State
import com.marcosholgado.forex.comparison.mapper.CurrencyDateViewMapper
import com.marcosholgado.forex.comparison.model.CompareCurrenciesViewModel
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject
import javax.inject.Named

class ComparisonCurrenciesViewModel @Inject internal constructor(
    private val compareCurrencies: CompareCurrencies,
    private val mapper: CurrencyDateViewMapper,
    @Named("base") private val base: String
) : ViewModel() {

    private val currenciesLiveData: MutableLiveData<CompareCurrenciesViewModel> = MutableLiveData()
    lateinit var symbols: String
    var baseRate: Float = 1f

    override fun onCleared() {
        compareCurrencies.dispose()
        super.onCleared()
    }

    fun getCurrencies(): LiveData<CompareCurrenciesViewModel> {
        return currenciesLiveData
    }

    fun fetchCurrencies() {
        currenciesLiveData.postValue(CompareCurrenciesViewModel(base, null, State.LOADING))
        return compareCurrencies.execute(CurrenciesSubscriber(), baseRate, this.symbols)
    }

    inner class CurrenciesSubscriber : DisposableSubscriber<CurrencyDate>() {

        override fun onComplete() {

        }

        override fun onNext(t: CurrencyDate) {
            currenciesLiveData.postValue(
                CompareCurrenciesViewModel(
                    base,
                    mapper.mapToView(t)
                    , State.SUCCESS
                )
            )
        }

        override fun onError(exception: Throwable) {
            val currenciesViewModel = currenciesLiveData.value
            currenciesLiveData.postValue(
                CompareCurrenciesViewModel(
                    base,
                    currenciesViewModel!!.currencies,
                    State.ERROR,
                    exception.message
                )
            )
        }
    }
}