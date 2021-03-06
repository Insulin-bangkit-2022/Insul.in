package com.insulin.app.data.model

import com.google.firebase.database.PropertyName

data class Detection(

    @PropertyName("id")
    var detection_id: String = "",

    @PropertyName("diabetes")
    var isDiabetes: Boolean = false,

    @PropertyName("age")
    var age: Int = 0,

    @PropertyName("gender")
    var gender: Boolean = true,

    @PropertyName("detectionTime")
    var detectionTime: String = "",

    @PropertyName("polyuria")
    var isPolyuria: Boolean = false,

    @PropertyName("polydipsia")
    var isPolydipsia: Boolean = false,

    @PropertyName("weightLoss")
    var isWeightLoss: Boolean = false,

    @PropertyName("weakness")
    var isWeakness: Boolean = false,

    @PropertyName("polyphagia")
    var isPolyphagia: Boolean = false,

    @PropertyName("genitalThrus")
    var isGenitalThrus: Boolean = false,

    @PropertyName("itching")
    var isItching: Boolean = false,

    @PropertyName("irritability")
    var isIrritability: Boolean = false,

    @PropertyName("delayedHealing")
    var isDelayedHealing: Boolean = false,

    @PropertyName("partialParesis")
    var isPartialParesis: Boolean = false,

    @PropertyName("muscleStiffness")
    var isMuscleStiffness: Boolean = false,

    @PropertyName("alopecia")
    var isAlopecia: Boolean = false,

    @PropertyName("obesity")
    var isObesity: Boolean = false,

    @PropertyName("visualBlurring")
    var isVisualBlurring: Boolean = false
)