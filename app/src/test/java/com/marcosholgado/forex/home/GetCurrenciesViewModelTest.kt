package com.marcosholgado.forex.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.marcosholgado.domain.useCases.currencies.GetCurrencies
import com.marcosholgado.forex.mapper.CurrencyViewMapper
import com.marcosholgado.domain.model.Currency
import com.marcosholgado.forex.home.model.CurrenciesViewModel
import com.marcosholgado.forex.home.model.CurrencyView
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.subscribers.DisposableSubscriber
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Captor
import org.mockito.Mock

@RunWith(JUnit4::class)
class GetCurrenciesViewModelTest {

    private lateinit var getCurrencies: GetCurrencies
    private lateinit var currencyViewMapper: CurrencyViewMapper
    private lateinit var getCurrenciesViewModel: GetCurrenciesViewModel

    @Captor
    private lateinit var captor: KArgumentCaptor<DisposableSubscriber<List<Currency>>>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        captor = argumentCaptor<DisposableSubscriber<List<Currency>>>()
        getCurrencies = mock()
        currencyViewMapper = mock()
        getCurrenciesViewModel = GetCurrenciesViewModel(getCurrencies, currencyViewMapper)
    }

    @Test
    fun initViewModelCallsUseCase() {
        verify(getCurrencies, times(1)).execute(any())
    }

    @Test
    fun initReturnsDataNoError() {
        val currencyList = createCurrencyList()
        val currencyViewList = createCurrencyViewList()
        val currenciesViewModel = createCurrenciesViewModel(currencyViewList)

        currencyList.forEachIndexed { index, it ->
            whenever(currencyViewMapper.mapToView(it)).thenReturn(currencyViewList[index])
        }

        verify(getCurrencies).execute(captor.capture())
        captor.firstValue.onNext(currencyList)

        assertThat(getCurrenciesViewModel.getCurrencies().value?.currencies).isEqualTo(
            currenciesViewModel.currencies
        )
    }

    @Test
    fun initEmptyDataWhenError() {
        verify(getCurrencies).execute(captor.capture())
        captor.firstValue.onError(RuntimeException())

        assertThat(getCurrenciesViewModel.getCurrencies().value?.currencies).isEmpty()
    }

    private fun createCurrencyViewList(): List<CurrencyView> {
        val currencyViewList = mutableListOf<CurrencyView>()
        currencyViewList.add(CurrencyView("EUR", 1.12345f))
        currencyViewList.add(CurrencyView("USD", 2.12345f))
        return currencyViewList
    }

    private fun createCurrencyList(): List<Currency> {
        val currencyEntityList = mutableListOf<Currency>()
        currencyEntityList.add(com.marcosholgado.domain.model.Currency("EUR", 1.12345f))
        currencyEntityList.add(com.marcosholgado.domain.model.Currency("USD", 2.12345f))
        return currencyEntityList
    }

    private fun createCurrenciesViewModel(currencyViewList: List<CurrencyView>): CurrenciesViewModel {
        return CurrenciesViewModel(null, currencyViewList)
    }
}