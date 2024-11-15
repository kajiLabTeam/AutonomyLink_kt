package net.kajilab.elpissender.usecase

import android.app.Activity
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kajilab.elpissender.API.SearedPreferenceApi
import net.kajilab.elpissender.API.http.ApiResponse
import net.kajilab.elpissender.R
import net.kajilab.elpissender.Repository.BLERepository
import net.kajilab.elpissender.Repository.SensingRepository
import net.kajilab.elpissender.Repository.SensorBase
import net.kajilab.elpissender.Repository.UserRepository
import net.kajilab.elpissender.Repository.WiFiRepository
import net.kajilab.elpissender.entity.User

class SensingUsecase(
    private val context: Context
) {
    private val apiResponse = ApiResponse(context)
    private val sensorRepository = SensingRepository(context)

    private var scanFlag = false
    private var targetSensors: MutableList<SensorBase> = mutableListOf()
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    private var user = User()

    fun firstStart(user: User){
        this.user = user
        scanFlag = true
        timerStart(
            fileName = "background",
            onStopped = {
                Log.d("SensingService","BackGroundで一回実行されたよ")
            }
        )
    }

    fun finalStop() {
        scanFlag = false
        if (targetSensors.isNotEmpty()) {
            stop {
                Log.d("SensingService", "バックグラウンドで実行を止めたよ")
            }
        }
    }

    fun start(fileName:String){
        addSensor(context = context)

        val samplingFrequency = -1.0
        serviceScope.launch {
            sensorRepository.sensorStart(
                fileName = fileName,
                sensors = targetSensors,
                samplingFrequency = samplingFrequency
            )
        }
    }

    fun stop(onStopped:() -> Unit){
        sensorRepository.sensorStop(
            sensors = targetSensors,
            onStopped = { sensorFileList ->
                val bleFile = sensorFileList[0]
                val wifiFile = sensorFileList[1]

                if(
                    bleFile != null &&
                    wifiFile != null &&
                    user.userName != "" &&
                    user.password != "" &&
                    user.serverUrl != ""
                ){
                    apiResponse.postCsvData(
                        wifiFile,
                        bleFile,
                        user.userName,
                        user.password,
                        user.serverUrl
                    )
                }
                onStopped()
            }
        )
        targetSensors = mutableListOf() // センサーをリセット
    }

    suspend fun timerStart10s(fileName:String,onStopped:() -> Unit){
        start(fileName)
        Log.d("Timer", "タイマー開始")
        delay(10000)
        Log.d("Timer", "タイマー終了")
        stop(onStopped)
        onStopped()
    }

    private fun timerStart(
        fileName:String,
        onStopped:() -> Unit
    ){
        serviceScope.launch {
            while(scanFlag){
                start(fileName)
                Log.d("Timer", "タイマー開始")
                delay(2 * 60 * 1000)
                Log.d("Timer", "タイマー終了")
                if(targetSensors.isNotEmpty()){
                    stop(onStopped)
                    onStopped()
                }
                delay(    1 * 60 * 1000)
            }
        }
    }

    private fun addSensor(context: Context){
        targetSensors.add(BLERepository(context))
        targetSensors.add(WiFiRepository(context))
    }
}