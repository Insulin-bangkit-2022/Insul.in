package com.insulin.app.data.model

data class Detection(
    var isDiabetes: Boolean = false,
    var detectionTime: String = "",
    var isPolyuria: Boolean = false,
    var isPolydipsia: Boolean = false,
    var isWeightLoss: Boolean = false,
    var isWeakness: Boolean = false,
    var isPolyphagia: Boolean = false,
    var isGenitalThrus: Boolean = false,
    var isItching: Boolean = false,
    var isIrritability: Boolean = false,
    var isDelayedHealing: Boolean = false,
    var isPartialParesis: Boolean = false,
    var isMuscleStiffness: Boolean = false,
    var isAlopecia: Boolean = false,
    var isObesity: Boolean = false
)