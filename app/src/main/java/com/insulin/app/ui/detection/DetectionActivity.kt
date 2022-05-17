package com.insulin.app.ui.detection

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.insulin.app.R
import com.insulin.app.databinding.ActivityDetectionBinding
import com.insulin.app.ui.detection.fragment.*
import com.insulin.app.utils.Constanta


class DetectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectionBinding

    private val diagnoseSymptoms: MutableMap<String, Any> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    fun diagnoseDiabetes() {
        supportActionBar?.hide()
        switchFragment(fragmentResult0)
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
    }


}