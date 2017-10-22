package com.foo.umbrella.ui.home

import android.util.Log
import android.widget.Toast
import com.foo.umbrella.R
import com.foo.umbrella.data.ApiServicesProvider
import com.foo.umbrella.data.model.CurrentWeatherDisplay
import com.foo.umbrella.data.model.DailyForecast
import com.foo.umbrella.data.model.ForecastCondition
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.TextStyle
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class HomePresenter(private val view: HomeContracts.View) : HomeContracts.Presenter {

    companion object {
        private const val TAG = "HomePresenter"
    }

    val apiServicesProvider = ApiServicesProvider(view.getContext());

    override fun loadForecastForZip(zipCode: String) {
        apiServicesProvider.weatherService.forecastForZipObservable(zipCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                    val weatherData = it.response().body()
                    val groupBy = weatherData.forecast.groupBy { it.dateTime.dayOfYear }
                    val dailyForecastList = groupBy
                            .mapKeys { getCurrentDay(it.value.first()) }
                            .entries.map {
                        val (day, hourlyForecast) = it

                        val minTemperature = hourlyForecast.minBy { it.tempFahrenheit }
                        val maxTemperature = hourlyForecast.maxBy { it.tempFahrenheit }
                        DailyForecast(day, hourlyForecast, minTemperature, maxTemperature)
                    }

                    val currentDisplayWeather = CurrentWeatherDisplay(
                            weatherData.currentObservation,
                            dailyForecastList
                    )

                    view.showForecastForZip(currentDisplayWeather)
                }, {
                    Log.e(TAG, it.message)
                    view.showErrorMessage()
                })
    }

    fun getCurrentDay(forecastCondition: ForecastCondition): String {
        val dayOfYear = forecastCondition.dateTime.dayOfYear
        if (dayOfYear == LocalDateTime.now().dayOfYear) {
            return view.getContext().getString(R.string.today)
        } else if (dayOfYear == LocalDateTime.now().dayOfYear + 1) {
            return view.getContext().getString(R.string.tomorrow)
        }
        return forecastCondition.dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

}