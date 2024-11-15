package net.kajilab.elpissender.Presenter.ui.view.Setting

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import net.kajilab.elpissender.API.SearedPreferenceApi

class SettingViewModel: ViewModel() {
    var sensingTime by mutableIntStateOf(0)  // センシング中の時間
    var waitTime by mutableIntStateOf(0)    // センシング待機時間

    val searedPreferenceApi = SearedPreferenceApi()

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
}