package com.foo.umbrella.ui.home

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.foo.umbrella.R
import com.foo.umbrella.data.model.DailyForecast
import com.foo.umbrella.data.model.ForecastCondition
import com.foo.umbrella.data.model.WeatherData
import com.foo.umbrella.ui.home.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.TextStyle
import java.util.*

class HomeActivity : AppCompatActivity(), HomeContracts.View {

    lateinit var homePresenter: HomeContracts.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
        supportActionBar?.title = currentObservation.displayLocation.fullName
        weatherDescription.text = currentObservation.weatherDescription

        val groupBy = weatherData.forecast.groupBy { it.dateTime.dayOfYear }
        val a = groupBy.mapKeys {getCurrentDay(it.value.first())}

        val dailyForecastList =  a.entries.map {
            val (k, v) = it
                DailyForecast(k, v)
        }

        dailyForecastRecyclerView.layoutManager = LinearLayoutManager(this)
        dailyForecastRecyclerView.adapter = ForecastAdapter(dailyForecastList)
    }

    fun getCurrentDay(forecastCondition: ForecastCondition) : String {
        val dayOfYear = forecastCondition.dateTime.dayOfYear
        if (dayOfYear == LocalDateTime.now().dayOfYear){
            return getString(R.string.today)
        }else if (dayOfYear == LocalDateTime.now().dayOfYear + 1) {
            return getString(R.string.tomorrow)
        }
        return forecastCondition.dateTime.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
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