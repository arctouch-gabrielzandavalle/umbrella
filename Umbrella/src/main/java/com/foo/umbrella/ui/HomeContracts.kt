package com.foo.umbrella.ui

import android.app.Application
import com.foo.umbrella.data.model.WeatherData

/**
 * Created by gabrielzandavalle on 10/21/17.
 */
class HomeContracts {

    interface View {
        fun showForecastForZip(WeatherData: WeatherData)
        fun getContext() : Application
    }

    interface Presenter {
        fun loadForecastForZip()
    }
}