package com.insulin.app.utils

object Constanta {
    const val TIME_SCREEN_ACTIVITY = 1500L

    const val TEMP_UID = "TEMP_UID_INSULIN"

    /* PERMISSION REQUEST CODE */
    const val CAMERA_PERMISSION_CODE = 10
    const val STORAGE_PERMISSION_CODE = 20
    const val LOCATION_PERMISSION_CODE = 30
    const val LINK_WEB_ATTRIBUTION = "https://sites.google.com/view/insul-in/atribusi-data"
    const val LINK_WEB_TENTANG_APLIKASI = "https://sites.google.com/view/insul-in/tentang"
    const val TYPE_ONE_TIME = "OneTimeAlarm"
    const val TYPE_REPEATING = "RepeatingAlarm"
    const val EXTRA_MESSAGE = "message"
    const val EXTRA_TYPE = "type"
    const val ID_ONETIME = 100
    const val ID_REPEATING = 101
    const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    const val LINK_WEB_KONSULTASI_DOKTER = "https://sites.google.com/view/insul-in/konsultasi"
    const val LINK_WEB_KOMUNITAS_DIABETES = "https://sites.google.com/view/insul-in/komunitas"
    const val LINK_WEB_MONITORING_GULA_DARAH = "https://sites.google.com/view/insul-in/monitoring"
    const val LINK_WEB_CEK_LAB = "https://sites.google.com/view/insul-in/lab"
    const val LINK_WEB_HELP = "https://sites.google.com/view/insul-in/bantuan"


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
        Obesity,
        VirtualBlurring
    }

    enum class AnsweredSympthoms {
        NotSelected,
        SelectedYes,
        SelectedNo
    }
}