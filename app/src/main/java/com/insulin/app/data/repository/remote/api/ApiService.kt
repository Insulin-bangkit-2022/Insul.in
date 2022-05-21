package com.insulin.app.data.repository.remote.api

import com.insulin.app.data.model.DetectionResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("")
    fun diagnoseDiabetes(): Call<DetectionResponse>

}