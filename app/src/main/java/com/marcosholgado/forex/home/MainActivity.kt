package com.marcosholgado.forex.home

import com.marcosholgado.forex.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.marcosholgado.forex.common.State
import com.marcosholgado.forex.di.ViewModelFactory
import com.marcosholgado.forex.home.model.CurrenciesViewModel
import com.marcosholgado.forex.home.model.CurrencyView
import com.marcosholgado.forex.navigation.Navigator
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_empty.*
import kotlinx.android.synthetic.main.view_error.*
import javax.inject.Inject
import javax.inject.Named

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var currenciesAdapter: CurrenciesAdapter
    @Inject
    lateinit var navigator: Navigator
    @field:[Inject Named("base")]
    lateinit var base: String

    private lateinit var getCurrenciesViewModel: GetCurrenciesViewModel
    private var tracker: SelectionTracker<Long>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savedInstanceState?.let {
            tracker?.onRestoreInstanceState(savedInstanceState)
        }

        getCurrenciesViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(GetCurrenciesViewModel::class.java)

        getCurrenciesViewModel.getCurrencies()
            .observe(this, Observer {
                if (it != null) this.handleDataState(it)
            })

        setupUI()
        setupListeners()
        setupAdapter()
        setupTracker()
    }

    override fun onResume() {
        super.onResume()
        tracker?.clearSelection()
    }

    override fun onSaveInstanceState(onSaveInstanceState: Bundle?) {
        super.onSaveInstanceState(onSaveInstanceState)

        onSaveInstanceState?.let {
            tracker?.onSaveInstanceState(it)
        }
    }

    private fun setupUI() {
        currency.text = base
    }

    private fun setupAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = currenciesAdapter
    }

    private fun setupTracker() {
        tracker = SelectionTracker.Builder<Long>(
            "selection-currency",
            recyclerView,
            CurrencyItemKeyProvider(recyclerView),
            CurrencyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        tracker?.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    val nItems: Int? = tracker?.selection!!.size()
                    if (nItems == 2) {
                        launchComparison(tracker?.selection!!)
                    }
                }
            })
        currenciesAdapter.setTracker(tracker)
    }

    private fun launchComparison(selection: Selection<Long>) {
        val symbols = selection.map {
            currenciesAdapter.currencies[it.toInt()].name
        }.joinToString(",")
        navigator.openComparisonActivity(this, value.text.toString().toFloat(), symbols)
    }

    private fun setupListeners() {
        value.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().isEmpty()) {
                    getCurrenciesViewModel.updateCurrenciesRate(p0.toString().toFloat())
                }
            }
        })
        retry_empty_button.setOnClickListener { getCurrenciesViewModel.fetchCurrencies(value.text.toString().toFloat()) }
        retry_error_button.setOnClickListener { getCurrenciesViewModel.fetchCurrencies(value.text.toString().toFloat()) }
    }

    private fun handleDataState(data: CurrenciesViewModel) {
        when (data.state) {
            State.SUCCESS -> updateListView(data.currencies)
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

    private fun showError(data: CurrenciesViewModel) {
        progress.visibility = View.GONE
        if (data.currencies.isEmpty()) {
            view_error.visibility = View.VISIBLE
            view_empty.visibility = View.GONE
            text_error_message.text = data.message
        } else {
            updateListView(data.currencies)
            data.message?.let { Snackbar.make(container, it, Snackbar.LENGTH_SHORT).show() }
        }
    }

    private fun updateListView(currencies: List<CurrencyView>) {
        progress.visibility = View.GONE
        view_error.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        if (currencies.isEmpty()) {
            view_empty.visibility = View.VISIBLE
        } else {
            view_empty.visibility = View.GONE
            currenciesAdapter.currencies = currencies
            currenciesAdapter.notifyDataSetChanged()
        }
    }
}
