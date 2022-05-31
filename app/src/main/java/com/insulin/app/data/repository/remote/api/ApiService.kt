package com.insulin.app.data.repository.remote.api

import com.insulin.app.data.model.DetectionResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST(".")
    fun diagnoseDiabetes(
        @Field("age") age: Int,
        @Field("gender") gender: Boolean,
        @Field("polyuria") polyuria: Boolean,
        @Field("polydipsia") polydipsia: Boolean,
        @Field("weightLoss") weightLoss: Boolean,
        @Field("weakness") weakness: Boolean,
        @Field("polyphagia") polyphagia: Boolean,
        @Field("genital_thrus") genital_thrus: Boolean,
        @Field("itching") itching: Boolean,
        @Field("irritability") irritability: Boolean,
        @Field("delayed_healing") delayed_healing: Boolean,
        @Field("partial_paresis") partial_paresis: Boolean,
        @Field("muscle_stiffness") muscle_stiffness: Boolean,
        @Field("alopecia") alopecia: Boolean,
        @Field("obesity") obesity: Boolean,
        @Field("visual_blurring") visual_blurring: Boolean,
    ): Call<DetectionResponse>

}