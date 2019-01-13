package com.marcosholgado.forex.navigation

import android.content.Context
import com.marcosholgado.forex.comparison.ComparisonActivity
import javax.inject.Inject

class Navigator @Inject constructor() {

    fun openComparisonActivity(context: Context, baseCurrency: Float, symbols: String) {
        ComparisonActivity.launch(context, baseCurrency, symbols)
    }
}