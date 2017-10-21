package com.foo.umbrella.ui.home

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.foo.umbrella.R
import com.foo.umbrella.data.model.ForecastCondition
import kotlinx.android.synthetic.main.hourly_forecast_card.view.*

class ForecastAdapter(private val forecast: List<ForecastCondition>) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.hourly_forecast_card, parent, false)

        return ViewHolder(context, view)
    }

    override fun getItemCount(): Int {
        return forecast.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(forecast[position])
    }

    class ViewHolder(val context: Context, view: View) : RecyclerView.ViewHolder(view) {

        val currentDayLabel: TextView = view.currentDayLabel

        fun bind(item: ForecastCondition) {
            currentDayLabel.text = item.dateTime.toString()
        }
    }
}