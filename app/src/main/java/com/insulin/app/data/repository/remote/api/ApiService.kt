package com.insulin.app.data.repository.remote.api

import com.insulin.app.data.model.DetectionResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET(".")
    fun diagnoseDiabetes(
        @Field("age") age: Int,
        @Field("gender") gender: Int,
        @Field("polyuria") polyuria: Int,
        @Field("polydipsia") polydipsia: Int,
        @Field("weightLoss") weightLoss: Int,
        @Field("weakness") weakness: Int,
        @Field("polyphagia") polyphagia: Int,
        @Field("genital_thrus") genital_thrus: Int,
        @Field("itching") itching: Int,
        @Field("irritability") irritability: Int,
        @Field("delayed_healing") delayed_healing: Int,
        @Field("partial_paresis") partial_paresis: Int,
        @Field("muscle_stiffness") muscle_stiffness: Int,
        @Field("alopecia") alopecia: Int,
        @Field("obesity") obesity: Int,
        @Field("visual_blurring") visual_blurring: Int,
    ): Call<DetectionResponse>

}