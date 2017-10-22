package com.foo.umbrella.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import com.foo.umbrella.R
import com.foo.umbrella.data.model.DailyForecast
import kotlinx.android.synthetic.main.hourly_forecast_card.view.*

class DayForecastAdapter(
        private val dailyForecastList: List<DailyForecast>,
        private val isCelsius: Boolean) : RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.hourly_forecast_card, parent, false)

        return ViewHolder(context, view, isCelsius)
    }

    override fun getItemCount(): Int {
        return dailyForecastList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dailyForecastList[position])
    }

    class ViewHolder(val context: Context, view: View, val isCelsius: Boolean) : RecyclerView.ViewHolder(view) {

        val currentDayLabel: TextView = view.currentDayLabel
        val gridView: GridView = view.hourlyForecastGrid

        fun bind(item: DailyForecast) {
            currentDayLabel.text = item.dayOfWeek

            gridView.adapter = HourlyForecastAdapter(
                    context,
                    item.hourlyForecast,
                    isCelsius,
                    item.minTemperature,
                    item.maxTemperature)
        }
    }
}