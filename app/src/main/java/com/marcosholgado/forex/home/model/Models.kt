package com.marcosholgado.forex.home.model

class CurrenciesViewModel(
    val baseCurrency: CurrencyView?,
    val currencies: List<CurrencyView> = emptyList(),
    val state: State,
    val message: String? = null
)

class CurrencyView(val name: String, val rate: Float)

enum class State {
    LOADING, SUCCESS, ERROR
}