package net.kajilab.elpissender.Repository

import android.app.Activity
import android.content.Context
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import net.kajilab.elpissender.API.WiFiApi
import net.kajilab.elpissender.Utils.DateUtils
import net.kajilab.elpissender.Utils.extension.SensorExtension
import java.io.File

class WiFiRepository(context: Context) : SensorBase(context) {

    override val sensorType: Int = SensorExtension.TYPE_WIFI
    override val sensorName: String = "WiFi"

    val TAG: String = "WiFiRepository"

    @Volatile
    private var isScanning = false

    val wifiApi = WiFiApi()

    fun getPermission(activity: Activity){
        wifiApi.getPermission(context, activity)
    }


    override suspend fun start(filename: String, samplingFrequency: Double) {
        super.start(filename, samplingFrequency)
        wifiApi.getScanResults(context){ scanResults ->
            val time = DateUtils.getTimeStamp()
            for (scanResult in scanResults){
                val data = "$time , ${scanResult.BSSID} , ${scanResult.level}"
                addQueue(
                    sensorName = sensorName,
                    timeStamp = time,
                    data = data
                )
            }
        }

        val scanInterval = 1000L
        isScanning = true

        // 以下の処理を止めるまで繰り返す
        while(isScanning){
            withContext(Dispatchers.Default){
                wifiApi.scanWiFi(context)
                delay(scanInterval)
            }
        }
    }

    override fun stop(): Single<File> {
        isScanning = false
        return super.stop()
    }
}