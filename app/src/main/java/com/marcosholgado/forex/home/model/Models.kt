package com.marcosholgado.forex.home.model

import com.marcosholgado.forex.common.State

class CurrenciesViewModel(
    val baseCurrency: CurrencyView?,
    val currencies: List<CurrencyView> = emptyList(),
    val state: State,
    val message: String? = null
)

class CurrencyView(val name: String, val rate: Float)