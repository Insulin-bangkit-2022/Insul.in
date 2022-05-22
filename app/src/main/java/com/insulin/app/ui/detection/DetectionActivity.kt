package com.insulin.app.ui.detection

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.insulin.app.R
import com.insulin.app.data.model.Detection
import com.insulin.app.data.model.DetectionResponse
import com.insulin.app.data.repository.remote.api.ApiConfig
import com.insulin.app.data.viewmodel.DetectionViewModel
import com.insulin.app.databinding.ActivityDetectionBinding
import com.insulin.app.ui.detection.fragment.*
import com.insulin.app.utils.Constanta
import com.insulin.app.utils.Helper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectionBinding
    val viewModel: DetectionViewModel by viewModels()

    private val diagnoseSymptoms: MutableMap<String, Any> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loading.root.isVisible = false
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        initSymptom()
        switchFragment(fragmentPolyuria)

    }



    private fun initSymptom() {
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Polyuria.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Polydipsia.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.WeightLoss.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Weakness.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Polyphagia.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.GenitalThrus.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Itching.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Irritability.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.DelayedHealing.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.PartialParesis.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.MuscleStiffness.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Alopecia.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Obesity.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
    }

    fun postValue(key: String, value: Any) {
        diagnoseSymptoms[key] = value
    }

    fun getValue(): String {
        return "polyurea : ${diagnoseSymptoms[Constanta.DiabetesSympthoms.Polyuria.name].toString()} " +
                "\n polydipsia : ${diagnoseSymptoms[Constanta.DiabetesSympthoms.Polydipsia.name].toString()}"
    }

    private fun getAnsweredQuestion(key: String): Any? {
        return diagnoseSymptoms[key]
    }

    fun parseAnsweredQuestion(key: String): Boolean {
        return when (getAnsweredQuestion(key).toString()) {
            Constanta.AnsweredSympthoms.SelectedYes.name -> true
            else -> false
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setBackgroundState(key: String, option0: LinearLayout, option1: LinearLayout) {
        when (getAnsweredQuestion(key)) {
            Constanta.AnsweredSympthoms.SelectedYes.name -> {
                option0.background = getDrawable(R.drawable.custom_background_detection)
                option1.background = getDrawable(R.drawable.custom_background_detection_active)
            }
            Constanta.AnsweredSympthoms.SelectedNo.name -> {
                option0.background = getDrawable(R.drawable.custom_background_detection_active)
                option1.background = getDrawable(R.drawable.custom_background_detection)
            }
        }
    }


    fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }




    fun saveDataToFirebase(data: Detection) {
        val database = Firebase.database
        val uid = Constanta.TEMP_UID
        val myRef =
            database.getReference("detection_history").child(uid).child(data.detection_id)
        binding.loading.root.isVisible = true
        myRef.setValue(data).addOnSuccessListener {
            binding.loading.root.isVisible = false
            val dialog = Helper.dialogInfoBuilder(
                this,
                "Disimpan !",
                "Data diagnosis berhasil disimpan, klik ok untuk kembali"
            )
            val btnOk: Button = dialog.findViewById(R.id.btn_ok)
            btnOk.setOnClickListener {
                dialog.dismiss()
                finish()
            }
            dialog.show()
        }.addOnFailureListener {
            binding.loading.root.isVisible = false
            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        }
    }


    companion object {
        val fragmentPolyuria = SymptomPolyuria()
        val fragmentPolydipsia = SymptomPolydipsia()
        val fragmentWeightLoss = SymptomWeightLoss()
        val fragmentWeakness = SymptomWeakness()
        val fragmentPolyphagia = SymptomPolyphagia()
        val fragmentGenitalThrus = SymptomGenitalThrus()
        val fragmentItching = SymptomItching()
        val fragmentIrritability = SymptomIrritability()
        val fragmentDelayedHealing = SymptomDelayedHealing()
        val fragmentPartialParesis = SymptomPartialParesis()
        val fragmentMuscleStiffness = SymptomMuscleStiffness()
        val fragmentAlopecia = SymptomAlopecia()
        val fragmentObesity = SymptomObesity()
        val fragmentConfirm = ConfirmSympthoms()
        val fragmentResult0 = DetectionResult0()
        val fragmentResult1 = DetectionResult1()
        val fragmentLoading = LoadingDetectionFragment()
    }


}