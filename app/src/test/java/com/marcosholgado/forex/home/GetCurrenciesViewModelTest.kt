package com.marcosholgado.forex.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.marcosholgado.domain.useCases.currencies.GetCurrencies
import com.marcosholgado.forex.mapper.CurrencyViewMapper
import com.marcosholgado.domain.model.Currency
import com.marcosholgado.domain.useCases.currencies.UpdateExchangeRate
import com.marcosholgado.forex.home.model.CurrenciesViewModel
import com.marcosholgado.forex.home.model.CurrencyView
import com.marcosholgado.forex.home.model.State
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
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

@RunWith(JUnit4::class)
class GetCurrenciesViewModelTest {

    private lateinit var getCurrencies: GetCurrencies
    private lateinit var updateExchangeRate: UpdateExchangeRate
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
        updateExchangeRate = mock()
        currencyViewMapper = mock()
        getCurrenciesViewModel =
                GetCurrenciesViewModel(getCurrencies, updateExchangeRate, currencyViewMapper)
    }

    @Test
    fun initViewModelCallsUseCase() {
        verify(getCurrencies, times(1)).execute(any(), anyOrNull(), anyOrNull())
    }

    @Test
    fun initReturnsDataNoErrorNoMessage() {
        val currencyList = createCurrencyList()
        val currencyViewList = createCurrencyViewList()
        val currenciesViewModel = createCurrenciesViewModel(currencyViewList)

        currencyList.forEachIndexed { index, it ->
            whenever(currencyViewMapper.mapToView(it)).thenReturn(currencyViewList[index])
        }

        verify(getCurrencies).execute(captor.capture(), eq(1f), anyOrNull())
        captor.firstValue.onNext(currencyList)

        assertThat(getCurrenciesViewModel.getCurrencies().value?.currencies).isEqualTo(
            currenciesViewModel.currencies
        )

        assertThat(getCurrenciesViewModel.getCurrencies().value?.state).isEqualTo(State.SUCCESS)
        assertThat(getCurrenciesViewModel.getCurrencies().value?.message).isNull()
    }

    @Test
    fun initEmptyDataWhenError() {
        verify(getCurrencies).execute(captor.capture(), eq(1f), anyOrNull())
        captor.firstValue.onError(RuntimeException())

        assertThat(getCurrenciesViewModel.getCurrencies().value?.currencies).isEmpty()
        assertThat(getCurrenciesViewModel.getCurrencies().value?.state).isEqualTo(State.ERROR)
    }

    @Test
    fun initLoadingState() {
        assertThat(getCurrenciesViewModel.getCurrencies().value?.state).isEqualTo(State.LOADING)
        assertThat(getCurrenciesViewModel.getCurrencies().value?.message).isNull()
        assertThat(getCurrenciesViewModel.getCurrencies().value?.currencies).isEmpty()
    }

    @Test
    fun returnsMessageWhenError() {
        verify(getCurrencies).execute(captor.capture(), eq(1f), anyOrNull())
        captor.firstValue.onError(RuntimeException("this is a test"))

        assertThat(getCurrenciesViewModel.getCurrencies().value?.state).isEqualTo(State.ERROR)
        assertThat(getCurrenciesViewModel.getCurrencies().value?.message).isEqualTo("this is a test")
    }

    @Test
    fun updateCurrenciesRateReturnsCorrectData() {
        val currencyList = createCurrencyList()
        val currencyViewList = createCurrencyViewList()
        val currenciesViewModel = createCurrenciesViewModel(currencyViewList)
        val currencyWithBaseRate = createCurrencyListWithBaseRate()

        currencyList.forEachIndexed { index, it ->
            whenever(currencyViewMapper.mapToView(it)).thenReturn(currencyViewList[index])
        }

        currenciesViewModel.currencies.forEachIndexed { index, it ->
            whenever(currencyViewMapper.mapFromView(it, 1f)).thenReturn(currencyWithBaseRate[index])
        }

        verify(getCurrencies).execute(captor.capture(), eq(1f), anyOrNull())
        captor.firstValue.onNext(currencyList)
        assertThat(getCurrenciesViewModel.getCurrencies().value?.currencies).isEqualTo(
            currenciesViewModel.currencies
        )

        getCurrenciesViewModel.updateCurrenciesRate(2.3456f)
        verify(updateExchangeRate).execute(captor.capture(), eq(2.3456f), eq(currencyWithBaseRate))
    }

    @Test
    fun updateCurrenciesRateEmptyList() {
        getCurrenciesViewModel.updateCurrenciesRate(1f)
        verify(updateExchangeRate, never()).execute(captor.capture(), eq(1f), any())
    }

    private fun createCurrencyViewList(): List<CurrencyView> {
        val currencyViewList = mutableListOf<CurrencyView>()
        currencyViewList.add(CurrencyView("EUR", 1.12345f))
        currencyViewList.add(CurrencyView("USD", 2.12345f))
        return currencyViewList
    }

    private fun createCurrencyListWithBaseRate(): List<Currency> {
        val currencyEntityList = mutableListOf<Currency>()
        currencyEntityList.add(Currency("EUR", 1.12345f * 2.3456f))
        currencyEntityList.add(Currency("USD", 2.12345f * 2.3456f))
        return currencyEntityList
    }

    private fun createCurrencyList(): List<Currency> {
        val currencyEntityList = mutableListOf<Currency>()
        currencyEntityList.add(Currency("EUR", 1.12345f))
        currencyEntityList.add(Currency("USD", 2.12345f))
        return currencyEntityList
    }

    private fun createCurrenciesViewModel(currencyViewList: List<CurrencyView>): CurrenciesViewModel {
        return CurrenciesViewModel(null, currencyViewList, State.SUCCESS)
    }
}