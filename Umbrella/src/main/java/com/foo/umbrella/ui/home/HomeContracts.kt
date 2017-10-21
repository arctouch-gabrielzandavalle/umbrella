package com.foo.umbrella.ui.home

import android.app.Application
import com.foo.umbrella.data.model.WeatherData

class HomeContracts {

    interface View {
        fun showForecastForZip(weatherData: WeatherData)
        fun getContext() : Application
    }

    interface Presenter {
        fun loadForecastForZip()
    }
}