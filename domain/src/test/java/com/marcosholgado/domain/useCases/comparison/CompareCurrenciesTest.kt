package com.marcosholgado.domain.useCases.comparison

import com.marcosholgado.domain.model.CurrencyDate
import com.marcosholgado.domain.repository.CurrencyRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString

@RunWith(JUnit4::class)
class CompareCurrenciesTest {

    private lateinit var compareCurrencies: CompareCurrencies
    private lateinit var scheduler: Scheduler
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        scheduler = Schedulers.trampoline()
        currencyRepository = mock()
        compareCurrencies = CompareCurrencies(currencyRepository, scheduler)
    }

    @Test
    fun createObservableCallsRepositoryFiveTimes() {
        stubCompareCurrencies()
        compareCurrencies.createObservable(1f, CURRENCIES).test()
        verify(currencyRepository, times(5)).compareCurrencies(anyString(), anyString())
    }


    @Test
    fun createObservableCompletes() {
        stubCompareCurrencies()
        val testObserver = compareCurrencies.createObservable(1f, CURRENCIES).test()
        testObserver.assertComplete()
    }

    @Test
    fun createObservableReturnsCorrectDataFiveTimes() {
        stubCompareCurrencies()
        val currencyDate = createCurrencyDate()
        val testObserver = compareCurrencies.createObservable(1f, CURRENCIES).test()
        testObserver.assertValues(
            currencyDate,
            currencyDate,
            currencyDate,
            currencyDate,
            currencyDate
        )
    }

    private fun stubCompareCurrencies() {
        whenever(currencyRepository.compareCurrencies(anyString(), anyString())).thenReturn(
            Flowable.just(createCurrencyDate())
        )
    }

    private fun createCurrencyDate(): CurrencyDate {
        val rates = mutableMapOf<String, Float>()
        rates["USD"] = 1.12345f
        rates["CAD"] = 2.12345f
        return CurrencyDate("10-11-2018", rates)
    }

    companion object {
        const val CURRENCIES = "USD,CAD"
    }
}