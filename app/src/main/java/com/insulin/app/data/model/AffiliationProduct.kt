package com.insulin.app.data.model

import com.google.firebase.database.PropertyName

data class AffiliationProduct (
    @PropertyName("product_category")
    var product_category: Int = 0,

    @PropertyName("product_url")
    var product_url: String = "",

    @PropertyName("product_name")
    var product_name: String = "",

    @PropertyName("product_image_url")
    var product_image_url: String = "",

    @PropertyName("product_store_image_url")
    var product_store_image_url: String = "",

    @PropertyName("product_price")
    var product_price: Int = 0,
)
