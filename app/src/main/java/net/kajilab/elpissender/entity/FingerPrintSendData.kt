package net.kajilab.elpissender.entity

import com.google.gson.annotations.SerializedName

data class FingerPrintSendData(
    @SerializedName("sample_type")
    val sampleType: String,
    @SerializedName("room_id")
    val roomId: Int,
)
