package com.marcosholgado.forex.comparison

import com.marcosholgado.forex.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marcosholgado.forex.comparison.model.CurrencyDateHeaderView
import com.marcosholgado.forex.comparison.model.CurrencyDateView
import com.marcosholgado.forex.comparison.model.CurrencyDateViewItem
import javax.inject.Inject

class ComparisonAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var currencies: MutableList<CurrencyDateViewItem> = mutableListOf()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currency = currencies[position]
        if (holder is ViewHolderRate) {
            holder.bind(currency as CurrencyDateView)
        }
        if (holder is ViewHolderHeader) {
            holder.bind(currency as CurrencyDateHeaderView)
        }
    }

    // Implement factory if there are more view types
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comparison_header, parent, false)
            return ViewHolderHeader(itemView)

        } else {
            val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.item_comparison, parent, false)
            return ViewHolderRate(itemView)
        }
    }

    override fun getItemViewType(position: Int): Int = if (position == 0) HEADER else NORMAL

    override fun getItemCount(): Int {
        return currencies.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun addHeader(view: CurrencyDateHeaderView) {
        currencies.add(view)
        notifyItemChanged(currencies.size - 1)
    }

    fun addRow(view: CurrencyDateView) {
        currencies.add(view)
        notifyItemChanged(currencies.size - 1)
    }

    inner class ViewHolderRate(view: View) : ComparisonViewholder<CurrencyDateView>(view) {
        private var rate1Text: TextView = view.findViewById(R.id.rate1)
        private var rate2Text: TextView = view.findViewById(R.id.rate2)
        private var dateText: TextView = view.findViewById(R.id.date)

        override fun bind(currencyView: CurrencyDateView) {
            rate1Text.text = currencyView.rate1
            rate2Text.text = currencyView.rate2
            dateText.text = currencyView.date
        }
    }

    inner class ViewHolderHeader(view: View) : ComparisonViewholder<CurrencyDateHeaderView>(view) {
        private var currency1Text: TextView = view.findViewById(R.id.currency1)
        private var currency2Text: TextView = view.findViewById(R.id.currency2)
        private var dateText: TextView = view.findViewById(R.id.date)

        override fun bind(currencyView: CurrencyDateHeaderView) {
            currency1Text.text = currencyView.currency1
            currency2Text.text = currencyView.currency2
            dateText.text = currencyView.date
        }
    }


    companion object {
        const val HEADER = 0
        const val NORMAL = 1
    }
}