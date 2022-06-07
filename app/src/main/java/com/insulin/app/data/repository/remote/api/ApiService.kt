package com.insulin.app.data.repository.remote.api

import com.insulin.app.data.model.DetectionResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET(".")
    fun diagnoseDiabetes(
        @Query("age") age: Int,
        @Query("gender") gender: Int,
        @Query("polyuria") polyuria: Int,
        @Query("polydipsia") polydipsia: Int,
        @Query("weightLoss") weightLoss: Int,
        @Query("weakness") weakness: Int,
        @Query("polyphagia") polyphagia: Int,
        @Query("genital_thrus") genital_thrus: Int,
        @Query("itching") itching: Int,
        @Query("irritability") irritability: Int,
        @Query("delayed_healing") delayed_healing: Int,
        @Query("partial_paresis") partial_paresis: Int,
        @Query("muscle_stiffness") muscle_stiffness: Int,
        @Query("alopecia") alopecia: Int,
        @Query("obesity") obesity: Int,
        @Query("visual_blurring") visual_blurring: Int,
    ): Call<DetectionResponse>

}