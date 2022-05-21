package com.insulin.app.data.model

import com.google.gson.annotations.SerializedName

data class DetectionResponse(
    @field:SerializedName("result_diagnose")
    val isDiabetes: Boolean,

    @field:SerializedName("error")
    val isError: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("article")
    val article: Article?,

    @field:SerializedName("affiliation_product")
    val affiliationProduct: AffiliationProduct?
)
