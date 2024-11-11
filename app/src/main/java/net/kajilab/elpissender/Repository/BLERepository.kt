package net.kajilab.elpissender.Repository

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.core.Single
import net.kajilab.elpissender.API.BLEApi
import net.kajilab.elpissender.Utils.DateUtils
import net.kajilab.elpissender.Utils.extension.SensorExtension
import org.altbeacon.beacon.Beacon
import java.io.File


class BLERepository(context: Context): SensorBase(context) {

    override val sensorType: Int = SensorExtension.TYPE_BLEBEACON
    override val sensorName: String = "BLEBeacon"

    val TAG: String = "BLERepository"

    val bleApi = BLEApi()

    fun getPermission(activity: Activity){
        bleApi.getPermission(context, activity)
    }

    override suspend fun start(filename: String, samplingFrequency: Double) {
        super.start(filename, samplingFrequency)

        bleApi.startBLEBeaconScan(context){ beacons ->
            //ここにビーコンの情報を受け取る処理を書く
            val time = DateUtils.getTimeStamp()
            val uuids = beacons?.scanRecord?.serviceUuids
            val rssi = beacons?.rssi
            val mac = beacons?.device?.address // 一旦macアドレスは送らない設定

            if(uuids != null) {
                for(uuid in uuids){
                    val uuid = uuid.uuid.toString()

                    val data = "$time , $uuid , $rssi"
                    addQueue(
                        sensorName = sensorName,
                        timeStamp = time,
                        data = data
                    )
                }
            }

        }
    }

    override fun stop(): Single<File> {
        bleApi.stopBLEBeaconScan()

        return super.stop()
    }
}