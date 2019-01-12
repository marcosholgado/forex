package com.marcosholgado.data.mapper

import com.marcosholgado.data.model.CurrencyEntity
import com.marcosholgado.domain.model.Currency
import javax.inject.Inject

open class CurrencyMapper @Inject constructor() {

    fun mapFromEntity(currency: CurrencyEntity): Currency {
        return Currency(currency.currency, currency.rate)
    }

    fun mapToEntity(currency: Currency): CurrencyEntity {
        return CurrencyEntity(currency.currency, currency.rate)
    }


}