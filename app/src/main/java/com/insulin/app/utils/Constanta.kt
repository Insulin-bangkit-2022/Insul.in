package com.insulin.app.utils

object Constanta {
    const val TIME_SCREEN_ACTIVITY = 1500L

    /* PERMISSION REQUEST CODE */
    const val CAMERA_PERMISSION_CODE = 10
    const val STORAGE_PERMISSION_CODE = 20
    const val LOCATION_PERMISSION_CODE = 30

    enum class DiabetesSympthoms {
        Polyuria,
        Polydipsia,
        WeightLoss,
        Weakness,
        Polyphagia,
        GenitalThrus,
        Itching,
        Irritability,
        DelayedHealing,
        PartialParesis,
        MuscleStiffness,
        Alopecia,
        Obesity
    }

    enum class AnsweredSympthoms {
        NotSelected,
        SelectedYes,
        SelectedNo
    }
}