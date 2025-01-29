package net.kajilab.elpissender.Repository

import net.kajilab.elpissender.API.graylog.GlaylogApiResponse
import net.kajilab.elpissender.entity.LogData

class LogSendRepository {

    val glaylogApiResponse = GlaylogApiResponse()

    fun sendLog(
        result: String,
        message: String,
        level: Int
    ) {
        // 端末名の取得
        val host = android.os.Build.MODEL
        val version = android.os.Build.VERSION.RELEASE

        val logData = LogData(
            result = result,
            shortMessage = message,
            level = level,
            version = version,
            host = host,
        )
        glaylogApiResponse.sendLog(logData)
    }
}