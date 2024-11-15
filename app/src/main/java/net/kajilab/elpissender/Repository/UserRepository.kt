package net.kajilab.elpissender.Repository

import android.app.Activity
import android.content.Context
import net.kajilab.elpissender.API.SearedPreferenceApi
import net.kajilab.elpissender.entity.User

class UserRepository {
    val searedPreferenceApi = SearedPreferenceApi()

    fun getUserName(
        context: Context
    ): String {
        return searedPreferenceApi.getStringValueByKey("userName",context)
    }

    fun getPassword(
        context: Context
    ): String {
        return searedPreferenceApi.getStringValueByKey("password",context)
    }

    fun getServerUrl(
        context: Context
    ): String {
        return searedPreferenceApi.getStringValueByKey("serverUrl",context)
    }

    fun updateUserName(
        name: String,
        context: Context
    ) {
        searedPreferenceApi.setStringValueByKey("userName",name,context)
    }

    fun updatePassword(
        password: String,
        context: Context
    ) {
        searedPreferenceApi.setStringValueByKey("password",password,context)
    }

    fun updateServerUrl(
        url: String,
        context: Context
    ) {
        searedPreferenceApi.setStringValueByKey("serverUrl",url,context)
    }

    fun saveUserSetting(
        userName: String,
        password: String,
        serverUrl: String,
        context: Context
    ){
        updateUserName(userName,context)
        updatePassword(password,context)
        updateServerUrl(serverUrl,context)
    }

    fun getUserSetting(
        context: Context
    ): User {
        val userName = getUserName(context)
        val password = getPassword(context)
        val serverUrl = getServerUrl(context)
        return User(
            userName,
            password,
            serverUrl
        )
    }
}
