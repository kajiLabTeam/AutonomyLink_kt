package net.kajilab.elpissender.Presenter.ui.view.User

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import net.kajilab.elpissender.API.SearedPreferenceApi
import net.kajilab.elpissender.Repository.UserRepository
import net.kajilab.elpissender.Service.SensingService
import net.kajilab.elpissender.entity.User

class UserViewModel: ViewModel() {

    var userName by mutableStateOf("")
    var password by mutableStateOf("")
    var serverUrl by mutableStateOf("")
    var isSensing by mutableStateOf(false)

    val userRepository = UserRepository()
    val searedPreferenceApi = SearedPreferenceApi()

    fun saveUserSetting(context: Context){
        userRepository.saveUserSetting(
            userName,
            password,
            serverUrl,
            context
        )
    }

    fun checkHealthyService(){

    }

    fun getSensingStatus(context: Context): Boolean {
        return searedPreferenceApi.getBooleanValueByKey("isSensing",context)
    }

    fun getUserSetting(context: Context): User {
        return userRepository.getUserSetting(context)
    }

    fun startForegroundSensing(
        isSensing: Boolean,
        context: Context
    ){
        if(isSensing){
            // TODO: ここで、通知と位置情報などのパーミッションチェックをしておくといい
            val serviceIntent = Intent(context, SensingService::class.java)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        }else{
            val serviceIntent = Intent(context, SensingService::class.java)
            context.stopService(serviceIntent)
        }
    }
}