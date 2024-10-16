package net.kajilab.elpissender.Service

import android.Manifest
import android.app.Application
import android.app.ForegroundServiceStartNotAllowedException
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.kajilab.elpissender.API.FileExplorerApi
import net.kajilab.elpissender.API.http.ApiResponse
import net.kajilab.elpissender.R
import net.kajilab.elpissender.Repository.SensingRepository
import net.kajilab.elpissender.Repository.SensorBase
import java.io.File

class SensingService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        sensing()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun sensing(){
        timerStart(
            fileName = "background",
            onStopped = {
                Log.d("SensingService","BackGroundで一回実行されたよ")
            }
        )
    }

    private fun startForeground() {

        // TODO: パーミッションチェックをしておくと良い
//        val locationPermission =
//            PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//        if (locationPermission != PermissionChecker.PERMISSION_GRANTED) {
//            stopSelf()
//            return
//        }

        try {
            val notification = createNotification()
            startForeground(1, notification)
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && e is ForegroundServiceStartNotAllowedException
            ) {
                Log.e("SensingService", "ForegroundServiceStartNotAllowedException occurred.")
            }
        }
    }

    private fun createNotification(): Notification {
        val notificationChannelId = "SensingServiceChannel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Sensing Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // 通知の内容を設定
        val notificationBuilder = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("Sensing Service")
            .setContentText("Service is running...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return notificationBuilder.build()
    }

    var targetSensors: MutableList<SensorBase> = mutableListOf()
    val apiResponse = ApiResponse(this)
    val sensorRepository = SensingRepository(this)
    var sensorStartFlag = false
    private val serviceScope = CoroutineScope(Dispatchers.IO)

    suspend fun start(fileName:String){

        val samplingFrequency = -1.0

        sensorRepository.sensorStart(
            fileName = fileName,
            sensors = targetSensors,
            samplingFrequency = samplingFrequency
        )
        sensorStartFlag = true
    }

    fun stop(onStopped:() -> Unit){
        sensorRepository.sensorStop(
            sensors = targetSensors,
            onStopped = { sensorFileList ->
                val bleFile = sensorFileList[0]
                val wifiFile = sensorFileList[1]

                if(bleFile != null && wifiFile != null){
                    apiResponse.postCsvData(wifiFile, bleFile)
                }
                onStopped()
            }
        )
        sensorStartFlag = false
        targetSensors = mutableListOf() // センサーをリセット
    }

    fun timerStart(fileName:String,onStopped:() -> Unit){
        serviceScope.launch {
            while(true){
                start(fileName)
                Log.d("Timer", "タイマー開始")
                delay(10000)
                Log.d("Timer", "タイマー終了")
                stop(onStopped)
                onStopped()
                delay(10 * 60 * 1000)
            }
        }
    }
}