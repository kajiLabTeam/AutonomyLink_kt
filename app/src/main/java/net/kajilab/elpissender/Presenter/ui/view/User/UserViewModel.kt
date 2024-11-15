package net.kajilab.elpissender.Presenter.ui.view.User

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
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

    fun saveUserSetting(
        context: Context,
        showSnackbar : (String) -> Unit
    ){
        // バリデーションチェック
        if(userName == "" || password == "" || serverUrl == ""){
            showSnackbar("ユーザー名、パスワード、サーバーURLを入力してください")
        }

        // serverURLはurlの形式になっているかチェックする
        if(!serverUrl.startsWith("http://") && !serverUrl.startsWith("https://")){
            showSnackbar("サーバーURLが正しくありません")
        }

        // serverURLは最後にスラッシュを入れてください
        if(!serverUrl.endsWith("/")){
            showSnackbar("サーバーURLの最後にスラッシュを入れてください")
        }

        userRepository.saveUserSetting(
            userName,
            password,
            serverUrl,
            context
        )

        showSnackbar("ユーザー情報を保存しました")
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
            val user = getUserSetting(context)
            if(user.userName == "" || user.password == "" || user.serverUrl == ""){
                // toastを表示して、画面を閉じる
                Log.d("SensingService","ユーザー情報が取得できませんでした")
                Toast.makeText(context, "ユーザー情報が取得できませんでした", Toast.LENGTH_SHORT).show()
                this.isSensing = false
                return
            }

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