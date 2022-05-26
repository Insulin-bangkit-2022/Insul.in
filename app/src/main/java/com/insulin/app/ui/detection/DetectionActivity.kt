package com.insulin.app.ui.detection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

@SuppressLint("UseCompatLoadingForDrawables")
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
            onBackPressed()
        }
        initSymptom()
        switchFragment(fragmentGreetings)

        viewModel.response.observe(this){

        }

    }


    private fun initSymptom() {
        diagnoseSymptoms.clear()
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Age.name] = 0
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Gender.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Polyuria.name] =
            Constanta.AnsweredSympthoms.NotSelected.name
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

    fun postValue(key: String, value: String) {
        diagnoseSymptoms[key] = value
    }

    fun postValueAge(value: Int) {
        diagnoseSymptoms[Constanta.DiabetesSympthoms.Age.name] = value
    }

    fun getValue(): String {
        return "polyurea : ${diagnoseSymptoms[Constanta.DiabetesSympthoms.Polyuria.name].toString()} " +
                "\n polydipsia : ${diagnoseSymptoms[Constanta.DiabetesSympthoms.Polydipsia.name].toString()}"
    }

    fun getAnsweredQuestion(key: String): Any? {
        return diagnoseSymptoms[key]
    }

    fun parseAnsweredQuestion(key: String): Boolean {
        return when (getAnsweredQuestion(key).toString()) {
            Constanta.AnsweredSympthoms.SelectedYes.name -> true
            else -> false
        }
    }

    fun checkQuestionIsFilled(key: String): Boolean {
        return when (getAnsweredQuestion(key).toString()) {
            Constanta.AnsweredSympthoms.SelectedYes.name -> true
            Constanta.AnsweredSympthoms.SelectedNo.name -> true
            else -> false
        }
    }

    fun parseAnsweredBoolean(key: String): String {
        return if (parseAnsweredQuestion(key)) "Ya" else "Tidak"
    }

    fun isAgeFilled(): Boolean {
        return diagnoseSymptoms[Constanta.DiabetesSympthoms.Age.name] != 0
    }

    fun initSelectedOptions(
        key: String,
        option0: LinearLayout,
        option1: LinearLayout
    ) {
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

    @SuppressLint("UseCompatLoadingForDrawables")
    fun initNavigationQuestion(
        prevQuestion: ImageView,
        prevFragment: Fragment,
        nextQuestion: ImageView,
        nextFragment: Fragment,
        nextValidation: Boolean
    ) {
        prevQuestion.apply {
            this.background =
                this@DetectionActivity.getDrawable(R.drawable.custom_background_detection_navigation)
            this.isEnabled = true
            this.setOnClickListener {
                switchFragment(prevFragment)
            }
        }
        if (nextValidation) {
            nextQuestion.apply {
                this.background =
                    this@DetectionActivity.getDrawable(R.drawable.custom_background_detection_navigation)
                this.isEnabled = true
                this.setOnClickListener {
                    switchFragment(nextFragment)
                }
            }
        } else {
            nextQuestion.apply {
                this.background =
                    this@DetectionActivity.getDrawable(R.drawable.custom_background_detection_navigation_disable)
                this.isEnabled = false
            }
        }
    }

    override fun onBackPressed() {
        val alertdialogbuilder = AlertDialog.Builder(this@DetectionActivity)
        alertdialogbuilder.setTitle("Batalkan Deteksi")
        alertdialogbuilder.setMessage("Apakah anda ingin keluar dari proses deteksi?")
        alertdialogbuilder.setPositiveButton("Ya") { alertDialog, _ ->
            alertDialog.dismiss()
            diagnoseSymptoms.clear()
            finish()
        }
        alertdialogbuilder.setNegativeButton("Tidak") { alertDialog, _ ->
            alertDialog.dismiss()
        }
        alertdialogbuilder.create().show()
    }


    fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }


    fun saveDataToFirebase(data: Detection) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            val database = Firebase.database
            val uid = user.uid
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
    }


    companion object {
        val fragmentGreetings = Greetings()
        val fragmentAge = SymptomAge()
        val fragmentGender = SymptomGender()
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