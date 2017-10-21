package com.foo.umbrella.ui.home

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.foo.umbrella.R
import com.foo.umbrella.data.model.WeatherData
import com.foo.umbrella.ui.home.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeContracts.View {

    lateinit var homePresenter: HomeContracts.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.title = "Floripa"
        supportActionBar?.setHomeButtonEnabled(true)

        homePresenter = HomePresenter(this)
        homePresenter.loadForecastForZip()
    }

    override fun getContext(): Application {
        return this.application
    }

    override fun showForecastForZip(weatherData: WeatherData) {
        val currentObservation = weatherData.currentObservation
        temperature.text = getString(R.string.temperature, Math.round(currentObservation.tempCelsius.toDouble()).toString())
        weatherDescription.text = currentObservation.weatherDescription
        dailyForecastRecyclerView.layoutManager = LinearLayoutManager(this)
        dailyForecastRecyclerView.adapter = ForecastAdapter(weatherData.forecast)
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