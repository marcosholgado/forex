package com.marcosholgado.forex.mapper

import com.google.common.truth.Truth.assertThat
import com.marcosholgado.domain.model.Currency
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyViewMapperTest {

    @Test
    fun mapFromCurrency() {
        val mapperTest = CurrencyViewMapper()
        val currency = Currency("EUR", 1.12345f)
        val currencyView = mapperTest.mapToView(currency)

        assertThat(currencyView.name).isEqualTo(currency.currency)
        assertThat(currencyView.rate).isEqualTo(currency.rate)
    }

}