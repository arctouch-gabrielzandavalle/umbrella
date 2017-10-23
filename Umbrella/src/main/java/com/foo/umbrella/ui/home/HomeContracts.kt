package com.foo.umbrella.ui.home

import android.app.Application
import com.foo.umbrella.data.model.CurrentWeatherDisplay

class HomeContracts {

    interface View {
        fun showForecast(currentWeatherDisplay: CurrentWeatherDisplay)
        fun getContext(): Application
        fun showDialog()
        fun closeDialog()
        fun showErrorMessage()
        fun showErrorOnRetryingMessage()
    }

    interface Presenter {
        fun loadForecastForZip(zipCode: String)
        fun tryLoadingDefaultZipCode()
    }
}