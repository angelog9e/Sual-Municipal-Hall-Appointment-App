package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AppointmentService {
    @Multipart
    @POST("submit_appointment.php")
    fun submitAppointment(
        @Part("appointment") appointment: RequestBody,
        @Part frontPhoto: MultipartBody.Part,
        @Part backPhoto: MultipartBody.Part,
        @Part selfiePhoto: MultipartBody.Part
    ): Call<ApiResponse>
}