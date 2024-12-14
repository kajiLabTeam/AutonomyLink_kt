package net.kajilab.elpissender.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.kajilab.elpissender.API.SearedPreferenceApi
import net.kajilab.elpissender.R
import net.kajilab.elpissender.Utils.FirebaseLogger
import net.kajilab.elpissender.usecase.SensingUsecase

class SensingWorker (
    val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val TAG = "SensingWorker"
    private val NOTIFICATION_ID = 1

    val sensingUsecase = SensingUsecase(context)
    val searedPreferenceApi = SearedPreferenceApi()
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    val firebaseLogger = FirebaseLogger(context)

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification()
        )
    }

    override suspend fun doWork(): Result {
        searedPreferenceApi.setBooleanValueByKey("isSensing",true,context)
        serviceScope.launch {

            firebaseLogger.logSensing("start","センシングを開始しました")
            sensingUsecase.timerStart(
                fileName = "",
                onStopped = {
                    firebaseLogger.logSensing("end","センシングを終了しました。")
                    Log.d("SettingViewModel", "センシングが停止しました")
                },
                sensingSecond = 10 * 60,
            )
        }
        return Result.success()
    }

    private fun createNotification() : Notification {
        val notificationChannelId = "SensingServiceChannel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Sensing Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // 通知の内容を設定
        val notificationBuilder = NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle("Sensing Service")
            .setContentText("Service is running...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return notificationBuilder.build()
    }
}