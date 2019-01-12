package com.marcosholgado.forex.mapper

import com.marcosholgado.domain.model.Currency
import com.marcosholgado.forex.home.model.CurrencyView
import javax.inject.Inject

open class CurrencyViewMapper @Inject constructor() {
    fun mapToView(currencyEntity: Currency): CurrencyView {
        return CurrencyView(currencyEntity.currency, currencyEntity.rate)
    }
}