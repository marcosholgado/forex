package com.marcosholgado.forex.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyEntityMapperTest {

    @Test
    fun mapFromRemote() {
        val mapperTest = CurrencyEntityMapper()
        val currency = Pair("EUR", "1.12345")
        val currencyEntity = mapperTest.mapFromRemote(currency)

        assertThat(currencyEntity.currency).isEqualTo(currency.first)
        assertThat(currencyEntity.rate).isEqualTo(currency.second.toFloat())
    }
}