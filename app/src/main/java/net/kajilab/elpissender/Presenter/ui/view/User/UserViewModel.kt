package net.kajilab.elpissender.Presenter.ui.view.User

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserViewModel: ViewModel() {

    var userName by mutableStateOf("")
    var password by mutableStateOf("")
    var isSensing by mutableStateOf(false)

}