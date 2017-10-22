package com.foo.umbrella.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.foo.umbrella.R
import com.foo.umbrella.data.model.ForecastCondition
import kotlinx.android.synthetic.main.hourly_forecast_grid_item.view.*

class HourlyForecastAdapter(
        private val context: Context,
        private val hourlyForecast: List<ForecastCondition>,
        private val isCelsius: Boolean) : BaseAdapter() {

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        var itemView: View?

        if (view == null) {
            val layoutInflater = LayoutInflater.from(context)
            itemView = layoutInflater.inflate(R.layout.hourly_forecast_grid_item, null);

            val hourForecast = hourlyForecast[position]
            val temperature = if (isCelsius) hourForecast.tempCelsius else hourForecast.tempFahrenheit

            itemView.currentHour.text = hourForecast.displayTime
            itemView.currentHourCondition.text = context.getString(R.string.temperature, temperature)
            itemView.currentHourIcon.setImageResource(R.drawable.weather_cloudy)

        } else {
            itemView = view
        }

        return itemView!!
    }

    override fun getItem(position: Int): Any {
        return hourlyForecast[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return hourlyForecast.size
    }
}