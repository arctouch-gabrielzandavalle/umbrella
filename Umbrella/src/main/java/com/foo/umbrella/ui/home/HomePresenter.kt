package com.foo.umbrella.ui.home

import android.util.Log
import com.foo.umbrella.data.ApiServicesProvider
import com.foo.umbrella.data.model.CurrentWeatherDisplay
import com.foo.umbrella.data.model.DailyForecast
import com.foo.umbrella.data.model.WeatherData
import retrofit2.adapter.rxjava.Result
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class HomePresenter(
        private val view: HomeContracts.View,
        val apiServicesProvider: ApiServicesProvider) : HomeContracts.Presenter {

    companion object {
        private const val TAG = "HomePresenter"
    }

    override fun loadForecastForZip(zipCode: String) {
        view.showDialog()
        apiServicesProvider.weatherService.forecastForZipObservable(zipCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                    val currentDisplayWeather = transformWeatherData(it)
                    view.showForecast(currentDisplayWeather)
                    view.closeDialog()
                }, {
                    Log.e(TAG, it.message)
                    view.closeDialog()
                    view.showErrorMessage()

                })
    }

    private fun transformWeatherData(result: Result<WeatherData>): CurrentWeatherDisplay {
        val weatherData = result.response().body()
        val groupBy = weatherData.forecast.groupBy { it.dateTime.dayOfYear }
        val dailyForecastList = groupBy
                .mapKeys { it.value.first() }
                .entries.map { (day, hourlyForecast) ->

            val minTemperature = hourlyForecast.minBy { it.tempFahrenheit }
            val maxTemperature = hourlyForecast.maxBy { it.tempFahrenheit }
            DailyForecast(day, hourlyForecast, minTemperature, maxTemperature)
        }

        val currentDisplayWeather = CurrentWeatherDisplay(
                weatherData.currentObservation,
                dailyForecastList
        )
        return currentDisplayWeather
    }

    override fun tryLoadingDefaultZipCode() {
        view.showDialog()
        apiServicesProvider.weatherService.forecastForZipObservable(HomeActivity.DEFAULT_ZIPCODE)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({

                    val currentDisplayWeather = transformWeatherData(it)
                    view.showForecast(currentDisplayWeather)
                    view.closeDialog()
                }, {
                    Log.e(TAG, it.message)
                    view.closeDialog()
                    view.showErrorOnRetryingMessage()
                })
    }

}