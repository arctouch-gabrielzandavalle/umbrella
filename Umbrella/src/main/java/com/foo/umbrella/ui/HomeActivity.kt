package com.foo.umbrella.ui

import android.app.Application
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.foo.umbrella.R
import com.foo.umbrella.data.model.WeatherData
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeContracts.View {

    lateinit var homePresenter: HomeContracts.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        homePresenter = HomePresenter(this)
        homePresenter.loadForecastForZip()
    }

    override fun getContext(): Application {
        return this.application
    }

    override fun showForecastForZip(weatherData: WeatherData) {
        val currentObservation = weatherData.currentObservation
        temperature.text = Math.round(currentObservation.tempCelsius.toDouble()).toString()
        weatherDescription.text = currentObservation.weatherDescription
    }
}