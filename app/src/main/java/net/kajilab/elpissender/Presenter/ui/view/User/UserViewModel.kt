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
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import net.kajilab.elpissender.API.SearedPreferenceApi
import net.kajilab.elpissender.Repository.UserRepository
import net.kajilab.elpissender.Service.SensingService
import net.kajilab.elpissender.Service.SensingWorker
import net.kajilab.elpissender.entity.User
import java.util.concurrent.TimeUnit

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
        val wokManager = WorkManager.getInstance(context)
        return wokManager.getWorkInfosByTag(SensingWorker.WorkerTag).get().isNotEmpty()
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
            val wokManager = WorkManager.getInstance(context)
            val sensingWorkerRequest = PeriodicWorkRequest.Builder(
                SensingWorker::class.java,
                15, TimeUnit.MINUTES, // インターバルの時間
                5, TimeUnit.MINUTES  // フレックスの時間
            )
                .apply {
                    addTag(SensingWorker.WorkerTag)
                }
                .build()
            wokManager.enqueue(sensingWorkerRequest)
        }else{
            val wokManager = WorkManager.getInstance(context)
            wokManager.cancelAllWorkByTag(SensingWorker.WorkerTag)
        }
    }
}