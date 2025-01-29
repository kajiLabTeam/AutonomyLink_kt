package net.kajilab.elpissender.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File

class SensingRepository(context: Context) {
    val tag: String = "SensingRepository"

    private val wifiRepository = WiFiRepository(context)
    private val bleRepository = BLERepository(context)

    fun getPermission(activity: Activity) {
        wifiRepository.getPermission(activity)
        bleRepository.getPermission(activity)
    }

    suspend fun sensorStart(
        fileName: String,
        sensors: MutableList<SensorBase>,
        samplingFrequency: Double,
    ) {
        for (sensor in sensors) {
            sensor.init()
            sensor.start(
                filename = fileName,
                samplingFrequency = samplingFrequency,
            )
            Log.d(tag, "fileName = $fileName")
        }
    }

    fun sensorStop(
        sensors: MutableList<SensorBase>,
        onStopped: (List<File?>) -> Unit,
    ) {
        val singles =
            sensors.map { sensor ->
                sensor.stop() // This should return Single<File?>
            }

        val disposable: Disposable =
            Single.zip(singles) { results ->
                results.map { it as File? }
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { files ->
                        Log.d(tag, "センサー停止 成功")
                        // センサーが終了した時にMainActivityに伝える。
                        onStopped(files)
                    },
                    { e ->
                        Log.e(tag, "センサー停止 失敗", e)
                    },
                )
    }
}
