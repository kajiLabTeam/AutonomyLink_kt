package net.kajilab.elpissender.API

import android.app.Activity
import android.content.Context
import android.provider.Settings.Global.getString

class SearedPreferenceApi {
    private val preference_file_key = "SearedPreference"

    fun getStringValueByKey(
        key: String,
        context: Context
    ): String {

        val sharedPref = context.getSharedPreferences(
            preference_file_key,
            Context.MODE_PRIVATE
        )

        return sharedPref.getString(key, "") ?: ""
    }

    fun setStringValueByKey(
        key: String,
        value: String,
        context: Context
    ) {
        val sharedPref = context.getSharedPreferences(
            preference_file_key,
            Context.MODE_PRIVATE
        )

        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getIntegerValueByKey(
        key: String,
        defaultValue: Int = 1,
        context: Context
    ): Int {
        val sharedPref = context.getSharedPreferences(
            preference_file_key,
            Context.MODE_PRIVATE
        )

        return sharedPref.getInt(key, defaultValue)
    }

    fun setIntegerValueByKey(
        key: String,
        value: Int,
        context: Context
    ) {
        val sharedPref = context.getSharedPreferences(
            preference_file_key,
            Context.MODE_PRIVATE
        )

        with(sharedPref.edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun getBooleanValueByKey(
        key: String,
        context: Context
    ):Boolean {
        val sharedPref = context.getSharedPreferences(
            preference_file_key,
            Context.MODE_PRIVATE
        )

        return sharedPref.getBoolean(key, false)
    }

    fun setBooleanValueByKey(
        key: String,
        value: Boolean,
        context: Context
    ){
        val sharedPref = context.getSharedPreferences(
            preference_file_key,
            Context.MODE_PRIVATE
        )

        with(sharedPref.edit()) {
            putBoolean(key, value)
            apply()
        }
    }
}