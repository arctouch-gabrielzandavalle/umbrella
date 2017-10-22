package com.foo.umbrella.ui.home.settings

import android.content.Context
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import com.foo.umbrella.R
import com.foo.umbrella.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_settings.*
import android.widget.ListView



class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = getString(R.string.app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.settings_action_bar_color))

        zipPreference.setOnClickListener { promptForZipCode() }
        unitPreference.setOnClickListener { promptForUnit() }

        val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val currentZipCode = sharedPref.getString("zipCode", HomeActivity.DEFAULT_ZIPCODE)
        zipCode.text = currentZipCode
    }

    private fun promptForUnit() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Choose a Unit")

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
        arrayAdapter.add("Celsius")
        arrayAdapter.add("Fahrenheit")

        dialog.setNegativeButton("cancel") { dialog, which -> dialog.dismiss() }

        dialog.setAdapter(arrayAdapter) { dialog, which ->
            val selectedUnit = arrayAdapter.getItem(which)
            saveOnSharedPreferences("unit", selectedUnit)
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
                        saveOnSharedPreferences("zipCode", editText.text.toString())
                        zipCode.text = editText.text
                    }
                })
                .setNegativeButton("Cancel", { dialog, id -> dialog.cancel() })
                .show()
    }

    private fun saveOnSharedPreferences(key: String, value: String) {
        val sharedPref = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}