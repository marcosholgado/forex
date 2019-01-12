package com.marcosholgado.forex.mapper

import com.google.common.truth.Truth.assertThat
import com.marcosholgado.domain.model.Currency
import com.marcosholgado.forex.home.model.CurrencyView
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyViewMapperTest {

    @Test
    fun mapToViewCurrency() {
        val mapperTest = CurrencyViewMapper()
        val currency = Currency("EUR", 1.12345f)
        val currencyView = mapperTest.mapToView(currency)

        assertThat(currencyView.name).isEqualTo(currency.currency)
        assertThat(currencyView.rate).isEqualTo(currency.rate)
    }

    @Test
    fun mapFromViewCurrency() {
        val mapperTest = CurrencyViewMapper()
        val baseRate = 2f
        val currencyView = CurrencyView("EUR", 4.4666f)
        val currency = mapperTest.mapFromView(currencyView, baseRate)

        assertThat(currency.currency).isEqualTo(currencyView.name)
        assertThat(currency.rate).isEqualTo(currencyView.rate / baseRate)
    }
}