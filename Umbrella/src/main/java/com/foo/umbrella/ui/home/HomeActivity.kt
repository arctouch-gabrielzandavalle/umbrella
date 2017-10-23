package com.foo.umbrella.ui.home

import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.foo.umbrella.R
import com.foo.umbrella.data.ApiServicesProvider
import com.foo.umbrella.data.model.CurrentWeatherDisplay
import com.foo.umbrella.ui.adapter.DayForecastAdapter
import com.foo.umbrella.ui.home.settings.SettingsActivity
import com.foo.umbrella.ui.util.SharedPreferencesUtil
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeContracts.View {

    companion object {
        private const val FAHRENHEIT_REFERENCE = 60
        const val DEFAULT_ZIPCODE = "99551"
        const val CELSIUS = "Celsius"
        const val ZIPCODE_SHARED_PREFERENCES_KEY = "zipCode"
        const val UNIT_SHARED_PREFERENCES_KEY = "unit"
    }

    lateinit var homePresenter: HomeContracts.Presenter

    private var isCelsius = false
    private val sharedPreferencesUtil = SharedPreferencesUtil(this)
    private lateinit var dialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = ""
        dialog = ProgressDialog(this)

        homePresenter = HomePresenter(this, ApiServicesProvider(this.application))

        val sharedPref = sharedPreferencesUtil.getSharedPreferences()
        val zipCode = sharedPref.getString(ZIPCODE_SHARED_PREFERENCES_KEY, "")

        if (TextUtils.isEmpty(zipCode)) {
            promptForZipCode()
        } else {
            homePresenter.loadForecastForZip(zipCode)
        }

        val unit = sharedPref.getString(UNIT_SHARED_PREFERENCES_KEY, CELSIUS)
        isCelsius = unit == CELSIUS
    }

    private fun promptForZipCode() {
        val layoutInflater = LayoutInflater.from(this)
        val promptView = layoutInflater.inflate(R.layout.zipcode_dialog, null)
        val editText: EditText = promptView.findViewById(R.id.zipCodeEntry) as EditText

        AlertDialog.Builder(this)
                .setTitle("Choose a ZipCode")
                .setView(promptView)
                .setPositiveButton("OK", { dialog, id ->
                    if (!TextUtils.isEmpty(editText.text)) {
                        onDialogOptionSelected(editText.text.toString())
                    }
                })
                .setNegativeButton("Cancel", {
                    dialog, id ->
                    dialog.cancel()
                    onDialogOptionSelected(DEFAULT_ZIPCODE)
                })
                .show()
    }

    fun onDialogOptionSelected(zipCode: String) {
        sharedPreferencesUtil.savePreference(
                HomeActivity.ZIPCODE_SHARED_PREFERENCES_KEY,
                zipCode)
        homePresenter.loadForecastForZip(zipCode)
    }

    override fun getContext(): Application {
        return this.application
    }

    override fun showForecast(currentWeatherDisplay: CurrentWeatherDisplay) {
        val currentObservation = currentWeatherDisplay.currentObservation
        val currentTemperature = if (isCelsius) currentObservation.tempCelsius.toDouble() else currentObservation.tempFahrenheit.toDouble()
        val roundedTemperature = Math.round(currentTemperature)

        currentWeatherLayout.setBackgroundColor(
                if (isCoolWeather(currentObservation.tempFahrenheit.toDouble())) ContextCompat.getColor(this, R.color.weather_cool)
                else ContextCompat.getColor(this, R.color.weather_warm))

        temperature.text = getString(R.string.temperature, roundedTemperature.toString())
        supportActionBar?.title = currentObservation.displayLocation.fullName
        weatherDescription.text = currentObservation.weatherDescription

        dailyForecastRecyclerView.layoutManager = LinearLayoutManager(this)
        dailyForecastRecyclerView.adapter = DayForecastAdapter(currentWeatherDisplay.dailyForecastList, isCelsius)
    }

    private fun isCoolWeather(fahrenheitTemperature: Double): Boolean {
        return fahrenheitTemperature < FAHRENHEIT_REFERENCE
    }

    override fun showErrorMessage() {
        Toast.makeText(this, getString(R.string.invalid_zip_code), Toast.LENGTH_LONG).show()
        homePresenter.tryLoadingDefaultZipCode()
        sharedPreferencesUtil.savePreference(
                HomeActivity.ZIPCODE_SHARED_PREFERENCES_KEY,
                DEFAULT_ZIPCODE)
    }

    override fun showErrorOnRetryingMessage() {
        Toast.makeText(this, getString(R.string.could_load_defaul_zip_code), Toast.LENGTH_LONG).show()
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

    override fun showDialog() {
        dialog.setMessage(getString(R.string.loading_weather))
        dialog.show()
    }

    override fun closeDialog() {
        dialog.dismiss()
    }
}