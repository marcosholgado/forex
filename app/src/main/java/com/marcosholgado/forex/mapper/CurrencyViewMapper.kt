package com.marcosholgado.forex.mapper

import com.marcosholgado.domain.model.Currency
import com.marcosholgado.forex.home.model.CurrencyView
import javax.inject.Inject

open class CurrencyViewMapper @Inject constructor() {
    open fun mapToView(currency: Currency): CurrencyView {
        return CurrencyView(currency.currency, currency.rate)
    }

    open fun mapFromView(currencyView: CurrencyView, rate: Float): Currency {
        return Currency(currencyView.name, currencyView.rate / rate)
    }
}