package com.foo.umbrella.ui.home

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.foo.umbrella.R
import com.foo.umbrella.data.model.CurrentWeatherDisplay
import com.foo.umbrella.ui.home.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeContracts.View {

    companion object {
        private const val FAHRENHEIT_REFERENCE = 60
        private const val CELSIUS_REFERENCE = 15
    }

    lateinit var homePresenter: HomeContracts.Presenter

    private val isCelsius = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.setHomeButtonEnabled(true)

        homePresenter = HomePresenter(this)
        homePresenter.loadForecastForZip("94016")
    }

    override fun getContext(): Application {
        return this.application
    }

    override fun showForecastForZip(currentWeatherDisplay: CurrentWeatherDisplay) {
        val currentObservation = currentWeatherDisplay.currentObservation

        val currentTemperature = if (isCelsius) currentObservation.tempCelsius.toDouble() else currentObservation.tempFahrenheit.toDouble()
        val roundedTemperature = Math.round(currentTemperature)

        currentWeatherLayout.setBackgroundColor(
                if (roundedTemperature < getReferenceMetric()) ContextCompat.getColor(this, R.color.weather_cool)
                else ContextCompat.getColor(this, R.color.weather_warm))

        temperature.text = getString(R.string.temperature, roundedTemperature.toString())
        supportActionBar?.title = currentObservation.displayLocation.fullName
        weatherDescription.text = currentObservation.weatherDescription

        dailyForecastRecyclerView.layoutManager = LinearLayoutManager(this)
        dailyForecastRecyclerView.adapter = DayForecastAdapter(currentWeatherDisplay.dailyForecastList, isCelsius)
    }

    private fun getReferenceMetric(): Int {
        if (isCelsius) {
            return CELSIUS_REFERENCE
        }
        return FAHRENHEIT_REFERENCE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}