package com.marcosholgado.forex.home

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrenciesViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(GetCurrenciesViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        getCurrenciesViewModel.getCurrencies().observe(this, Observer {
            if (it != null) this.handleDataState(it)
        })
        setupAdapter()
    }

    private fun setupAdapter() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = currenciesAdapter
    }


    private fun handleDataState(data: CurrenciesViewModel) {
        updateListView(data.currencies)
    }

    private fun updateListView(currencies: List<CurrencyView>) {
        currenciesAdapter.currencies = currencies.map { it }
        currenciesAdapter.notifyDataSetChanged()
    }
}
