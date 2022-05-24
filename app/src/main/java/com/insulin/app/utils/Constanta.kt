package com.insulin.app.utils

object Constanta {
    const val TIME_SCREEN_ACTIVITY = 1500L

    const val TEMP_UID = "UIDKharlos"

    /* PERMISSION REQUEST CODE */
    const val CAMERA_PERMISSION_CODE = 10
    const val STORAGE_PERMISSION_CODE = 20
    const val LOCATION_PERMISSION_CODE = 30
    const val LINK_WEB_ATTRIBUTION = "https://sites.google.com/view/insul-in/atribusi-data"
    const val LINK_WEB_TENTANG_APLIKASI = "https://sites.google.com/view/insul-in/tentang"



    enum class DiabetesSympthoms {
        Age,
        Gender,
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