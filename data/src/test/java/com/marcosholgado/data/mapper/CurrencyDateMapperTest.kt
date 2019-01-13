package com.marcosholgado.data.mapper

import com.google.common.truth.Truth.assertThat
import com.marcosholgado.data.model.CurrencyDateEntity
import org.junit.Test

import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyDateMapperTest {

    @Test
    fun mapFromEntity() {
        val mapperTest = CurrencyDateMapper()
        val map = mutableMapOf<String, Float>()
        map["EUR"] = 1.1234f
        map["USD"] = 2.1234f
        val currencyDateEntity = CurrencyDateEntity("10/11/2018", map)
        val currencyDate = mapperTest.mapFromEntity(currencyDateEntity)

        assertThat(currencyDate.date).isEqualTo(currencyDateEntity.date)
        assertThat(currencyDate.rates).isEqualTo(currencyDateEntity.rates)
    }

}