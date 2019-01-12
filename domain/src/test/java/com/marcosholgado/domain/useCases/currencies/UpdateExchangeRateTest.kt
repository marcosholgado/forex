package com.marcosholgado.domain.useCases.currencies

import com.google.common.truth.Truth.assertThat
import com.marcosholgado.domain.model.Currency
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class UpdateExchangeRateTest {

    private lateinit var updateExchangeRate: UpdateExchangeRate
    private lateinit var scheduler: Scheduler

    @Before
    fun setUp() {
        scheduler = Schedulers.trampoline()
        updateExchangeRate = UpdateExchangeRate(scheduler)
    }

    @Test
    fun createObservableCompletes() {
        val testObserver = updateExchangeRate.createObservable(1f, createCurrencyList()).test()
        testObserver.assertComplete()
    }

    @Test
    fun createObservableReturnsCorrectData() {
        val currencyList = createCurrencyList()
        val testObserver = updateExchangeRate.createObservable(1f, currencyList).test()
        testObserver.assertValues(currencyList)
    }

    @Test
    fun createObservableReturnsCorrectDataDiffBaseRate() {
        val currencyList = createCurrencyList()
        val testObserver = updateExchangeRate.createObservable(2.5123f, currencyList).test()
        testObserver.assertValues(createDiffBaseRateCurrencyList())
    }

    @Test
    fun createObservableReturnsError() {
        val currencyList = createCurrencyList()
        val testObserver = updateExchangeRate.createObservable(0f, currencyList).test()
        assertThat(testObserver.errorCount()).isEqualTo(1)
    }

    private fun createCurrencyList(): List<Currency> {
        val currencyEntityList = mutableListOf<Currency>()
        currencyEntityList.add(Currency("EUR", 1.12345f))
        currencyEntityList.add(Currency("USD", 2.12345f))
        return currencyEntityList
    }

    private fun createDiffBaseRateCurrencyList(): List<Currency> {
        val currencyEntityList = mutableListOf<Currency>()
        currencyEntityList.add(Currency("EUR", 1.12345f * 2.5123f))
        currencyEntityList.add(Currency("USD", 2.12345f * 2.5123f))
        return currencyEntityList
    }
}