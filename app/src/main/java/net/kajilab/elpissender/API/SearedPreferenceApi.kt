package net.kajilab.elpissender.API

import android.app.Activity
import android.content.Context
import android.provider.Settings.Global.getString

class SearedPreferenceApi {
    private val preference_file_key = "SearedPreference"

    fun getStringValueByKey(
        key: String,
        activity: Activity
    ): String {

        val sharedPref = activity.getSharedPreferences(
            preference_file_key,
            Context.MODE_PRIVATE
        )

        return sharedPref.getString(key, "") ?: ""
    }

    fun setStringValueByKey(
        key: String,
        value: String,
        activity: Activity
    ) {
        val sharedPref = activity.getSharedPreferences(
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
        activity: Activity
    ): Int {
        val sharedPref = activity.getSharedPreferences(
            preference_file_key,
            Context.MODE_PRIVATE
        )

        return sharedPref.getInt(key, 0)
    }

    fun setIntegerValueByKey(
        key: String,
        value: Int,
        activity: Activity
    ) {
        val sharedPref = activity.getSharedPreferences(
            preference_file_key,
            Context.MODE_PRIVATE
        )

        with(sharedPref.edit()) {
            putInt(key, value)
            apply()
        }
    }
}