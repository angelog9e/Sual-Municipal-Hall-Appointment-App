package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.3.86/sual-web/controller/" // change URL if new pull

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer your-token") // Replace with your token if required
                .addHeader("User-Agent", "SualMunicipalHallAppointmentApp")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val appointmentService: AppointmentService by lazy {
        retrofit.create(AppointmentService::class.java)
    }
}
