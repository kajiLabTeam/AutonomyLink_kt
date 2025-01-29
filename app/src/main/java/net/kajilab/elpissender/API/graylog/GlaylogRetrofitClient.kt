package net.kajilab.elpissender.API.graylog

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GlaylogRetrofitClient {
    private const val BASE_URL = "https://graylog-gelf.harutiro.net/"

    val instance: GraylogService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(GraylogService::class.java)
    }
}