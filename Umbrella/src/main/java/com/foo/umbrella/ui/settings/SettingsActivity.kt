package com.foo.umbrella.ui.home.settings

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import com.foo.umbrella.R
import com.foo.umbrella.ui.home.HomeActivity
import com.foo.umbrella.ui.util.SharedPreferencesUtil
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val CEULSIUS_OPTION = "Celsius"
        private const val FAHRENHEIT_OPTION = "Fahrenheit"
    }

    private val sharedPreferencesUtil = SharedPreferencesUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.settings_action_bar_color))

        zipPreference.setOnClickListener { promptForZipCode() }
        unitPreference.setOnClickListener { promptForUnit() }

        setUpPrefs()
    }

    private fun setUpPrefs() {
        val sharedPref = sharedPreferencesUtil.getSharedPreferences()
        zipCode.text = sharedPref.getString(HomeActivity.ZIPCODE_SHARED_PREFERENCES_KEY, HomeActivity.DEFAULT_ZIPCODE)
        unit.text = sharedPref.getString(HomeActivity.UNIT_SHARED_PREFERENCES_KEY, HomeActivity.CELSIUS)
    }

    private fun promptForUnit() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Choose a Unit")

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
        arrayAdapter.add(CEULSIUS_OPTION)
        arrayAdapter.add(FAHRENHEIT_OPTION)

        dialog.setNegativeButton("cancel") { dialog, which -> dialog.dismiss() }

        dialog.setAdapter(arrayAdapter) { dialog, which ->
            val selectedUnit = arrayAdapter.getItem(which)
            sharedPreferencesUtil.savePreference(HomeActivity.UNIT_SHARED_PREFERENCES_KEY, selectedUnit)
            unit.text = selectedUnit
            dialog.dismiss()
        }
        dialog.show()
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
                        sharedPreferencesUtil.savePreference(HomeActivity.ZIPCODE_SHARED_PREFERENCES_KEY, editText.text.toString())
                        zipCode.text = editText.text
                    }
                })
                .setNegativeButton("Cancel", { dialog, id -> dialog.cancel() })
                .show()
    }
}