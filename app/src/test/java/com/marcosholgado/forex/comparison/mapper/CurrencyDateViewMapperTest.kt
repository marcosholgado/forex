package com.marcosholgado.forex.comparison.mapper

import com.google.common.truth.Truth
import com.marcosholgado.domain.model.CurrencyDate
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyDateViewMapperTest {
    @Test
    fun mapToDateViewCurrency() {
        val mapperTest = CurrencyDateViewMapper()
        val map = mutableMapOf<String, Float>()
        map["USD"] = 1.12345f
        map["CAD"] = 2.12345f
        val currency = CurrencyDate("10-11-2018", map)
        val currencyView = mapperTest.mapToView(currency)

        Truth.assertThat(currencyView.date).isEqualTo(currency.date)
        Truth.assertThat(currencyView.rate1).isEqualTo(currency.rates["USD"].toString())
        Truth.assertThat(currencyView.rate2).isEqualTo(currency.rates["CAD"].toString())
    }
}