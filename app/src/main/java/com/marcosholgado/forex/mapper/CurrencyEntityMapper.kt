package com.marcosholgado.forex.mapper

import com.marcosholgado.data.model.CurrencyEntity
import javax.inject.Inject


open class CurrencyEntityMapper @Inject constructor() {

    open fun mapFromRemote(pair: Pair<String, String>): CurrencyEntity {
        return CurrencyEntity(pair.first, pair.second.toFloat())
    }

}