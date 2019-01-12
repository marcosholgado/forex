package com.marcosholgado.forex.home

import com.marcosholgado.forex.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.marcosholgado.forex.home.model.CurrencyView
import javax.inject.Inject

class CurrenciesAdapter @Inject constructor() :
    RecyclerView.Adapter<CurrenciesAdapter.ViewHolder>() {

    var currencies: List<CurrencyView> = arrayListOf()
    private var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency = currencies[position]
        holder.bind(currency, tracker!!.isSelected(position.toLong()))
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

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setTracker(tracker: SelectionTracker<Long>?) {
        this.tracker = tracker
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var currencyText: TextView = view.findViewById(R.id.currency)
        private var rateText: TextView = view.findViewById(R.id.rate)

        fun bind(currencyView: CurrencyView, isActivated: Boolean = false) {
            currencyText.text = currencyView.name
            rateText.text = currencyView.rate.toString()
            itemView.isActivated = isActivated
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
    }
}