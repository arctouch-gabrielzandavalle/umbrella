package com.foo.umbrella.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
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
        private val isCelsius: Boolean,
        private val minTemperature: ForecastCondition?,
        private val maxTemperature: ForecastCondition?) : BaseAdapter() {

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {

        var itemView: View?

        if (view == null) {
            val layoutInflater = LayoutInflater.from(context)
            itemView = layoutInflater.inflate(R.layout.hourly_forecast_grid_item, null);

            val hourForecast = hourlyForecast[position]
            val temperature = if (isCelsius) hourForecast.tempCelsius else hourForecast.tempFahrenheit

            itemView.currentHour.text = hourForecast.displayTime
            itemView.currentHourCondition.text = context.getString(R.string.temperature, temperature)
            itemView.currentHourIcon.setImageResource(getImageResource(hourForecast.icon))

            if (hourForecast.equals(minTemperature)) {
                itemView.currentHour.setTextColor(ContextCompat.getColor(context, R.color.weather_cool))
                itemView.currentHourCondition.setTextColor(ContextCompat.getColor(context, R.color.weather_cool))
                itemView.currentHourIcon.setColorFilter(ContextCompat.getColor(context, R.color.weather_cool))
            }

            if (hourForecast.equals(maxTemperature)) {
                itemView.currentHour.setTextColor(ContextCompat.getColor(context, R.color.weather_warm))
                itemView.currentHourCondition.setTextColor(ContextCompat.getColor(context, R.color.weather_warm))
                itemView.currentHourIcon.setColorFilter(ContextCompat.getColor(context, R.color.weather_warm))
            }

        } else {
            itemView = view
        }

        return itemView!!
    }

    private fun getImageResource(icon: String) = when (icon) {
        "cloudy" -> R.drawable.weather_cloudy
        "fog" -> R.drawable.weather_fog
        "sleet" -> R.drawable.weather_hail
        "chancetstorms" -> R.drawable.weather_lightning
        "tstorms" -> R.drawable.weather_lightning_rainy
        "partlycloudy" -> R.drawable.weather_partlycloudy
        "rain" -> R.drawable.weather_rainy
        "chancerain" -> R.drawable.weather_rainy
        "snow" -> R.drawable.weather_snowy
        "chancesnow" -> R.drawable.weather_snowy
        "hazy" -> R.drawable.weather_snowy_rainy
        "clear" -> R.drawable.weather_sunny
        "sunny" -> R.drawable.weather_sunny
        else -> R.drawable.weather_sunny
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