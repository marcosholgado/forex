package com.marcosholgado.forex.comparison

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class ComparisonViewholder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: T)
}