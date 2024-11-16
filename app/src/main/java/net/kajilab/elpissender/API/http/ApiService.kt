package net.kajilab.elpissender.API.http

import net.kajilab.elpissender.entity.FingerPrintSendData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
interface ApiService {
    @Multipart
    @POST("/api/signals/submit")
    fun submitSignalData(
        @Part wifiData: MultipartBody.Part?,
        @Part bleData: MultipartBody.Part?
    ): Call<ResponseBody>

    @Multipart
    @POST("/api/fingerprint/collect")
    fun sendFingerPrintModel(
        @Part wifiData: MultipartBody.Part?,
        @Part bleData: MultipartBody.Part?,
        @Part sampleType:MultipartBody.Part?,
        @Part roomId:MultipartBody.Part?
    ): Call<ResponseBody>
}
