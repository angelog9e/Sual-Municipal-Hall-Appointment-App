package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://server-address/" // Replace server address

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val appointmentService: AppointmentService by lazy {
        retrofit.create(AppointmentService::class.java)
    }
}
