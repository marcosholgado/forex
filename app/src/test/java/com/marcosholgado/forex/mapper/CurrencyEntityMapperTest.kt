package com.marcosholgado.forex.mapper

import com.google.common.truth.Truth
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

        Truth.assertThat(currencyEntity.currency).isEqualTo(currency.first)
        Truth.assertThat(currencyEntity.rate).isEqualTo(currency.second.toFloat())
    }
}