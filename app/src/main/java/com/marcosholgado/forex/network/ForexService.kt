package com.marcosholgado.forex.network

import com.google.gson.internal.LinkedTreeMap
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface ForexService {

    @GET("latest?access_key=55b3d7ea41ece863950d0174813e0195")
    fun getCurrencies(@Query("base") base: String, @Query("symbols") symbols: String): Flowable<CurrenciesResponse>

    data class CurrenciesResponse(
        val success: Boolean,
        val base: String?,
        val rates: LinkedTreeMap<String, String>?,
        val error: CurrencyError?
    )

    data class CurrencyError(val info: String)

}