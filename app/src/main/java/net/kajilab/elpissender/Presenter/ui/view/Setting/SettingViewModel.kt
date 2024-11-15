package net.kajilab.elpissender.Presenter.ui.view.Setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SettingViewModel: ViewModel() {
    var negativeModelUrl by mutableStateOf("https://example.com")
    var positiveModelUrl by mutableStateOf("https://example.com")

    var sensingTime by mutableIntStateOf(10)  // センシング中の時間
    var waitTime by mutableIntStateOf(5)    // センシング待機時間
}