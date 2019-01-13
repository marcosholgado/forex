package com.marcosholgado.data.mapper

import com.marcosholgado.data.model.CurrencyDateEntity
import com.marcosholgado.domain.model.CurrencyDate
import javax.inject.Inject

open class CurrencyDateMapper @Inject constructor() {

    open fun mapFromEntity(currency: CurrencyDateEntity): CurrencyDate {
        return CurrencyDate(currency.date, currency.rates)
    }
}