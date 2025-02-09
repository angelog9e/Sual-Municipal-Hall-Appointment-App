package com.aquino_angelo_m.sualmunicipalhallappointmentapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AppointmentService {
    @POST("submit_appointment.php") // The endpoint on your server
    fun submitAppointment(@Body appointment: Appointment): Call<ApiResponse>
}