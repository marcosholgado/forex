package com.marcosholgado.forex.mapper

import com.marcosholgado.data.model.CurrencyDateEntity
import com.marcosholgado.forex.network.ForexService
import javax.inject.Inject


open class CurrencyDateEntityMapper @Inject constructor() {

    open fun mapFromRemote(currencyDateResponse: ForexService.CurrenciesDateResponse): CurrencyDateEntity {
        val ratesMap = mutableMapOf<String, Float>()
        currencyDateResponse.rates?.forEach { (key, value) ->
            ratesMap[key] = value.toFloat()
        }
        return CurrencyDateEntity(currencyDateResponse.date!!, ratesMap)
    }

}