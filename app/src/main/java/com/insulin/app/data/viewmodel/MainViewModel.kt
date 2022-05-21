package com.insulin.app.data.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.insulin.app.data.model.Detection
import com.insulin.app.data.model.DetectionResponse
import com.insulin.app.data.repository.remote.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetectionViewModel : ViewModel() {

    val data = MutableLiveData<Detection>()

    fun setDiagnosisData(detection: Detection){
        data.postValue(detection)
    }
}