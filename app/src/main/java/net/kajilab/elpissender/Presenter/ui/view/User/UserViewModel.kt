package net.kajilab.elpissender.Presenter.ui.view.User

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import net.kajilab.elpissender.API.SearedPreferenceApi

class UserViewModel: ViewModel() {

    var userName by mutableStateOf("")
    var password by mutableStateOf("")
    var serverUrl by mutableStateOf("")
    var isSensing by mutableStateOf(false)

    val searedPreferenceApi = SearedPreferenceApi()

    fun updateUserName(
        name: String,
        activity: Activity
    ) {
        searedPreferenceApi.setStringValueByKey("userName",name,activity)
    }

    fun updatePassword(
        password: String,
        activity: Activity
    ) {
        searedPreferenceApi.setStringValueByKey("password",password,activity)
    }

    fun updateServerUrl(
        url: String,
        activity: Activity
    ) {
        searedPreferenceApi.setStringValueByKey("serverUrl",url,activity)
    }


    fun getUserName(
        activity: Activity
    ): String {
        return searedPreferenceApi.getStringValueByKey("userName",activity)
    }

    fun getPassword(
        activity: Activity
    ): String {
        return searedPreferenceApi.getStringValueByKey("password",activity)
    }

    fun getServerUrl(
        activity: Activity
    ): String {
        return searedPreferenceApi.getStringValueByKey("serverUrl",activity)
    }

    fun saveUserSetting(activity: Activity){
        updateUserName(userName,activity)
        updatePassword(password,activity)
        updateServerUrl(serverUrl,activity)
    }

}