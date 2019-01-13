package com.marcosholgado.forex.comparison.model

import com.marcosholgado.forex.common.State

class CompareCurrenciesViewModel(
    val baseCurrency: String,
    val currencies: CurrencyDateView?,
    val state: State,
    val message: String? = null
)

class CurrencyDateView(val date: String, val rate1: String, val rate2: String) :
    CurrencyDateViewItem()

class CurrencyDateHeaderView(val date: String, val currency1: String, val currency2: String) :
    CurrencyDateViewItem()

open class CurrencyDateViewItem