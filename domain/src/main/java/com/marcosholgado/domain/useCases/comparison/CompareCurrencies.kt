package com.marcosholgado.domain.useCases.comparison

import com.marcosholgado.domain.model.CurrencyDate
import com.marcosholgado.domain.repository.CurrencyRepository
import com.marcosholgado.domain.useCases.UseCase
import io.reactivex.Flowable
import io.reactivex.Scheduler
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

open class CompareCurrencies @Inject constructor(
    private val repository: CurrencyRepository,
    observeScheduler: Scheduler
) : UseCase<CurrencyDate, Float?, String?>(observeScheduler) {

    override fun createObservable(param1: Float?, param2: String?): Flowable<CurrencyDate> {
        return Flowable.fromIterable(
            getDays()
        ).concatMap {
            repository.compareCurrencies(it, param2!!)
                .map {
                    val ratesMap = mutableMapOf<String, Float>()
                    it.rates.forEach { cur ->
                        ratesMap[cur.key] = cur.value * (param1 ?: 1f)
                    }
                    val currencyDate = CurrencyDate(it.date, ratesMap)
                    currencyDate
                }
        }
    }

    private fun getDays(): List<String> {
        val daysList = mutableListOf<String>()
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        daysList.add(sdf.format(calendar.time))
        for (i in 0..3) {
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            daysList.add(sdf.format(calendar.time))
        }
        return daysList
    }

    companion object {
        const val DATE_FORMAT = "yyyy-MM-DD"
    }
}