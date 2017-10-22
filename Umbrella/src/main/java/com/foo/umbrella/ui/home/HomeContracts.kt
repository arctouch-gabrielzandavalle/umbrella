package com.foo.umbrella.ui.home

import android.app.Application
import com.foo.umbrella.data.model.CurrentWeatherDisplay

class HomeContracts {

    interface View {
        fun showForecastForZip(currentWeatherDisplay: CurrentWeatherDisplay)
        fun getContext(): Application
    }

    interface Presenter {
        fun loadForecastForZip(zipCode: String)
    }
}