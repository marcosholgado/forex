package com.marcosholgado.forex.comparison

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.marcosholgado.forex.R
import com.marcosholgado.forex.common.State
import com.marcosholgado.forex.comparison.model.CompareCurrenciesViewModel
import com.marcosholgado.forex.comparison.model.CurrencyDateHeaderView
import com.marcosholgado.forex.di.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_comparison.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_error.*
import java.text.DecimalFormat
import javax.inject.Inject
import javax.inject.Named

class ComparisonActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var adapter: ComparisonAdapter
    @field:[Inject Named("base")]
    lateinit var base: String

    private lateinit var comparisonCurrenciesViewModel: ComparisonCurrenciesViewModel
    private lateinit var symbols: String
    private var baseRate: Float = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comparison)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        comparisonCurrenciesViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ComparisonCurrenciesViewModel::class.java)

        symbols = intent.getStringExtra(INTENT_EXTRA_SYMBOLS)
        baseRate = intent.getFloatExtra(INTENT_EXTRA_BASE_RATE, 1f)

        setupModelView()
        setupView()
        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        comparisonCurrenciesViewModel.getCurrencies()
            .observe(this, Observer {
                if (it != null) this.handleDataState(it)
            })

        setupAdapter()
    }

    private fun setupView() {
        val format = DecimalFormat("0.#")
        value.text = getString(R.string.last_5_days, format.format(baseRate), base)
    }

    private fun setupModelView() {
        comparisonCurrenciesViewModel.baseRate = baseRate
        comparisonCurrenciesViewModel.symbols = symbols
        comparisonCurrenciesViewModel.fetchCurrencies()
    }

    private fun setupAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        val symbolsList = symbols.split(",")
        adapter.addHeader(CurrencyDateHeaderView("Date", symbolsList.first(), symbolsList.last()))
    }

    private fun setupListeners() {
        retry_empty_button.setOnClickListener { comparisonCurrenciesViewModel.fetchCurrencies() }
        retry_error_button.setOnClickListener { comparisonCurrenciesViewModel.fetchCurrencies() }
    }

    private fun handleDataState(data: CompareCurrenciesViewModel) {
        when (data.state) {
            State.SUCCESS -> updateListView(data)
            State.LOADING -> showLoading()
            State.ERROR -> showError(data)
        }
    }

    private fun showLoading() {
        view_error.visibility = View.GONE
        view_empty.visibility = View.GONE
        recyclerView.visibility = View.GONE
        progress.visibility = View.VISIBLE
    }

    private fun showError(data: CompareCurrenciesViewModel) {
        progress.visibility = View.GONE
        view_error.visibility = View.VISIBLE
        view_empty.visibility = View.GONE
        text_error_message.text = data.message
    }

    private fun updateListView(data: CompareCurrenciesViewModel) {
        progress.visibility = View.GONE
        view_error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        if (data.currencies == null) {
            view_empty.visibility = View.VISIBLE
        } else {
            view_empty.visibility = View.GONE
            adapter.addRow(data.currencies)
        }
    }

    companion object {
        private const val INTENT_EXTRA_BASE_RATE = "base_rate"
        private const val INTENT_EXTRA_SYMBOLS = "symbols"

        fun launch(context: Context, baseCurrency: Float, symbols: String) {
            val intent = Intent(context, ComparisonActivity::class.java)
            intent.putExtra(INTENT_EXTRA_BASE_RATE, baseCurrency)
            intent.putExtra(INTENT_EXTRA_SYMBOLS, symbols)
            context.startActivity(intent)
        }
    }
}
