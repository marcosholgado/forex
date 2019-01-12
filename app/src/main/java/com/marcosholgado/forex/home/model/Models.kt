package com.marcosholgado.forex.home.model

class CurrenciesViewModel(val baseCurrency: CurrencyView?, val currencies: List<CurrencyView>)

class CurrencyView(val name: String, val rate: Float)

enum class State {
    LOADING, SUCCESS, ERROR
}