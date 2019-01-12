package com.marcosholgado.data.mapper

import com.google.common.truth.Truth.assertThat
import com.marcosholgado.data.model.CurrencyEntity
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyMapperTest {

    @Test
    fun mapFromEntity() {
        val mapperTest = CurrencyMapper()
        val currencyEntity = CurrencyEntity("EUR", 1.12345f)
        val currency = mapperTest.mapFromEntity(currencyEntity)

        assertThat(currency.currency).isEqualTo(currencyEntity.currency)
        assertThat(currency.rate).isEqualTo(currencyEntity.rate)
    }

}