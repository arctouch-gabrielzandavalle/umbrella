package com.foo.umbrella.ui.util

import android.content.Context
import android.content.SharedPreferences
import com.foo.umbrella.R

class SharedPreferencesUtil(private val context: Context) {

    fun savePreference(key: String, value: String) {
        val sharedPref = getSharedPreferences()
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getSharedPreferences() : SharedPreferences {
        return context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    }
}