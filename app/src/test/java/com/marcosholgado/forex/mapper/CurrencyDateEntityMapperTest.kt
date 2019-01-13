package com.marcosholgado.forex.mapper

import com.google.common.truth.Truth.assertThat
import com.google.gson.internal.LinkedTreeMap
import com.marcosholgado.forex.network.ForexService
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyDateEntityMapperTest {

    @Test
    fun mapFromRemote() {
        val mapperTest = CurrencyDateEntityMapper()
        val currency = createDateResponse()
        val currencyDateEntity = mapperTest.mapFromRemote(currency)

        assertThat(currencyDateEntity.date).isEqualTo(currency.date)
        assertThat(currencyDateEntity.rates["USD"]).isEqualTo(currency.rates!!["USD"]!!.toFloat())
        assertThat(currencyDateEntity.rates["CAD"]).isEqualTo(currency.rates!!["CAD"]!!.toFloat())
    }

    private fun createDateResponse(): ForexService.CurrenciesDateResponse {
        val linkedTreeMap = LinkedTreeMap<String, String>()
        linkedTreeMap["USD"] = "1.12345"
        linkedTreeMap["CAD"] = "2.12345"
        return ForexService.CurrenciesDateResponse(true, "EUR", "10-11-2018", linkedTreeMap, null)
    }
}