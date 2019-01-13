package com.marcosholgado.forex.comparison.mapper

import com.marcosholgado.domain.model.CurrencyDate
import com.marcosholgado.forex.comparison.model.CurrencyDateView
import javax.inject.Inject

open class CurrencyDateViewMapper @Inject constructor() {
    open fun mapToView(currency: CurrencyDate): CurrencyDateView {
        val list = mutableListOf<String>()
        currency.rates.entries.forEach {
            list.add(it.value.toString())
        }
        return CurrencyDateView(currency.date, list.first(), list.last())
    }
}