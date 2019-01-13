package com.marcosholgado.domain.useCases.currencies

import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.repository.CurrencyRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Test

import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GetCurrenciesTest {

    private lateinit var getCurrencies: GetCurrencies
    private lateinit var scheduler: Scheduler
    private lateinit var currencyRepository: CurrencyRepository

    @Before
    fun setup() {
        scheduler = Schedulers.trampoline()
        currencyRepository = mock()
        getCurrencies = GetCurrencies(currencyRepository, scheduler)
    }

    @Test
    fun createObservableCallsRepository() {
        stubGetCurrencies()
        getCurrencies.createObservable()
        verify(currencyRepository).getCurrencies()
    }

    @Test
    fun createObservableCompletes() {
        stubGetCurrencies()
        val testObserver = getCurrencies.createObservable(1f).test()
        testObserver.assertComplete()
    }

    @Test
    fun createObservableReturnsCorrectData() {
        stubGetCurrencies()
        val currencyList = createCurrencyList()
        val testObserver = getCurrencies.createObservable(1f).test()
        testObserver.assertValues(currencyList)
    }

    private fun stubGetCurrencies() {
        whenever(currencyRepository.getCurrencies()).thenReturn(
            Flowable.just(createCurrencyList())
        )
    }

    private fun createCurrencyList(): List<Currency> {
        val currencyEntityList = mutableListOf<Currency>()
        currencyEntityList.add(Currency("EUR", 1.12345f))
        currencyEntityList.add(Currency("USD", 2.12345f))
        return currencyEntityList
    }
}