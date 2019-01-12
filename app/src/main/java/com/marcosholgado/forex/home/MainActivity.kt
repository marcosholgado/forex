package com.marcosholgado.forex.home

import com.marcosholgado.forex.R
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.marcosholgado.forex.di.ViewModelFactory
import com.marcosholgado.forex.home.model.CurrenciesViewModel
import com.marcosholgado.forex.home.model.CurrencyView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    @Inject
    lateinit var currenciesAdapter: CurrenciesAdapter

    private lateinit var getCurrenciesViewModel: GetCurrenciesViewModel
    private lateinit var tracker: SelectionTracker<Long>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrenciesViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(GetCurrenciesViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        getCurrenciesViewModel.getCurrencies(value.text.toString().toFloat())
            .observe(this, Observer {
            if (it != null) this.handleDataState(it)
        })

        setupListeners()
        setupAdapter()
        setupTracker()
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

        tracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    val nItems: Int? = tracker.selection.size()
                    if (nItems == 2) {
                        Log.d("TEST", "2 items selected!")
                    }
                }
            })
        currenciesAdapter.setTracker(tracker)
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
    }

    private fun handleDataState(data: CurrenciesViewModel) {
        updateListView(data.currencies)
    }

    private fun updateListView(currencies: List<CurrencyView>) {
        currenciesAdapter.currencies = currencies.map { it }
        currenciesAdapter.notifyDataSetChanged()
    }
}
