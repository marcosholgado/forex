package com.marcosholgado.forex.comparison

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.marcosholgado.domain.model.CurrencyDate
import com.marcosholgado.domain.useCases.comparison.CompareCurrencies
import com.marcosholgado.forex.common.State
import com.marcosholgado.forex.comparison.mapper.CurrencyDateViewMapper
import com.marcosholgado.forex.comparison.model.CurrencyDateView
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.eq
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

@RunWith(JUnit4::class)
class ComparisonCurrenciesViewModelTest {

    private lateinit var compareCurrencies: CompareCurrencies
    private lateinit var currencyDateViewMapper: CurrencyDateViewMapper
    private lateinit var comparisonCurrenciesViewModel: ComparisonCurrenciesViewModel

    @Captor
    private lateinit var captor: KArgumentCaptor<DisposableSubscriber<CurrencyDate>>

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        captor = argumentCaptor<DisposableSubscriber<CurrencyDate>>()
        compareCurrencies = mock()
        currencyDateViewMapper = mock()
        comparisonCurrenciesViewModel =
                ComparisonCurrenciesViewModel(compareCurrencies, currencyDateViewMapper, "EUR")
        comparisonCurrenciesViewModel.symbols = "USD,CAD"
    }

    @Test
    fun fetchCurrenciesCallsUseCase() {
        comparisonCurrenciesViewModel.fetchCurrencies()
        verify(compareCurrencies, times(1)).execute(any(), anyOrNull(), anyOrNull())
    }

    @Test
    fun fetchCurrenciesLoadingState() {
        comparisonCurrenciesViewModel.fetchCurrencies()
        assertThat(comparisonCurrenciesViewModel.getCurrencies().value?.state).isEqualTo(State.LOADING)
        assertThat(comparisonCurrenciesViewModel.getCurrencies().value?.message).isNull()
        assertThat(comparisonCurrenciesViewModel.getCurrencies().value?.currencies).isNull()
    }

    @Test
    fun fetchCurrenciesReturnsMessageWhenError() {
        comparisonCurrenciesViewModel.fetchCurrencies()

        verify(compareCurrencies).execute(captor.capture(), eq(1f), anyOrNull())
        captor.firstValue.onError(RuntimeException("this is a test"))

        assertThat(comparisonCurrenciesViewModel.getCurrencies().value?.state).isEqualTo(State.ERROR)
        assertThat(comparisonCurrenciesViewModel.getCurrencies().value?.message).isEqualTo("this is a test")
    }

    @Test
    fun fetchCurrenciesReturnsDataNoErrorNoMessage() {
        val currencyDate = createCurrencyDate()
        val currencyDateView = createCurrencyDateView()

        whenever(currencyDateViewMapper.mapToView(currencyDate)).thenReturn(currencyDateView)

        comparisonCurrenciesViewModel.fetchCurrencies()

        verify(compareCurrencies).execute(captor.capture(), eq(1f), anyOrNull())
        captor.firstValue.onNext(currencyDate)

        assertThat(comparisonCurrenciesViewModel.getCurrencies().value?.currencies).isEqualTo(
            currencyDateView
        )

        assertThat(comparisonCurrenciesViewModel.getCurrencies().value?.state).isEqualTo(State.SUCCESS)
        assertThat(comparisonCurrenciesViewModel.getCurrencies().value?.message).isNull()
    }

    private fun createCurrencyDate(): CurrencyDate {
        val rates = mutableMapOf<String, Float>()
        rates["USD"] = 1.12345f
        rates["CAD"] = 2.12345f
        return CurrencyDate("10-11-2018", rates)
    }

    private fun createCurrencyDateView(): CurrencyDateView =
        CurrencyDateView("10-11-2018", "1.12345", "2.12345")
}