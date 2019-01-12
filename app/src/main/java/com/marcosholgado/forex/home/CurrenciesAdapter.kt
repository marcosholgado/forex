package com.marcosholgado.forex.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marcosholgado.forex.home.model.CurrencyView
import javax.inject.Inject

class CurrenciesAdapter @Inject constructor() :
    RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>() {

    var currencies: List<CurrencyView> = arrayListOf()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = currencies[position]
        holder.currencyText.text = currency.name
        holder.rateText.text = currency.rate.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_currency, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var currencyText: TextView = view.findViewById(R.id.currency)
        var rateText: TextView = view.findViewById(R.id.rate)
    }
}