package net.kajilab.elpissender.Presenter.ui.view.Setting

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.kajilab.elpissender.API.SearedPreferenceApi
import net.kajilab.elpissender.API.http.ApiResponse
import net.kajilab.elpissender.Repository.UserRepository
import net.kajilab.elpissender.usecase.SensingUsecase

class SettingViewModel: ViewModel() {
    var sensingTime by mutableIntStateOf(0)  // センシング中の時間
    var waitTime by mutableIntStateOf(0)    // センシング待機時間

    val searedPreferenceApi = SearedPreferenceApi()
    var sensingUsecase:SensingUsecase? = null
    var apiResponse: ApiResponse? = null
    val userRepository = UserRepository()

    fun init(context: Context){
        sensingUsecase = SensingUsecase(context = context)
        apiResponse = ApiResponse(context)
    }

    fun getSensingTime(context: Context){
        sensingTime = searedPreferenceApi.getIntegerValueByKey(
            key = "sensingTime",
            context = context,
            defaultValue = 5
        )
        waitTime = searedPreferenceApi.getIntegerValueByKey(
            key = "waitTime",
            context = context,
            defaultValue = 10
        )
    }

    fun setSensingTime(context: Context){
        searedPreferenceApi.setIntegerValueByKey("sensingTime",sensingTime,context)
        searedPreferenceApi.setIntegerValueByKey("waitTime",waitTime,context)
    }

    fun getSetting(context: Context){
        getSensingTime(context)
    }

    fun startSensing10second(){
        viewModelScope.launch {
            sensingUsecase?.timerStart(
                fileName = "",
                onStopped = {
                    Log.d("SettingViewModel", "センシングが停止しました")
                },
                sensingSecond = 10,
            )
        }
    }

    fun sendNegativeModel(
        roomId:Int,
        sampleType:String,
        sensingTime:Int,
        context: Context,
        onStopped: () -> Unit,
    ){
        viewModelScope.launch {
            sensingUsecase?.timerStart(
                fileName = "",
                onStopped = {
                    Log.d("SettingViewModel", "センシングが停止しました")
                    onStopped()
                },
                sensingSecond = sensingTime,
                onSend = { sensorFileList ->
                    val bleFile = sensorFileList[0]
                    val wifiFile = sensorFileList[1]
                    val user = userRepository.getUserSetting(context)

                    if(
                        bleFile != null &&
                        wifiFile != null &&
                        user.serverUrl != ""
                    ){
                        apiResponse?.postModelData(
                            wifiFile = wifiFile,
                            bleFile = bleFile,
                            roomId = roomId,
                            sampleType = sampleType,
                            url = user.serverUrl,
                        )
                    }
                }
            )
        }
    }

    fun startSensing(){
        viewModelScope.launch {
            sensingUsecase?.start(
                fileName = ""
            )
        }
    }

    fun stopSensing(){
        sensingUsecase?.stop(
            onStopped = {
                Log.d("SettingViewModel", "センシングが停止しました")
            }
        )
    }
}