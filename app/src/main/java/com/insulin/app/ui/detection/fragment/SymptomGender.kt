package com.insulin.app.ui.detection.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.insulin.app.databinding.FragmentDetectionQuestionGenderBinding
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.utils.Constanta
/* Question number 2 */
class SymptomGender : Fragment() {

    private lateinit var binding: FragmentDetectionQuestionGenderBinding

    @SuppressLint("UseCompatLoadingForDrawables", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* binding layout */
        binding = FragmentDetectionQuestionGenderBinding.inflate(layoutInflater)

        /* === init selected answer if back from next question ===
        *  initSelectedOption -> select option yes or now based submitted value
        *  initNavigationQuestion -> disable / enable navigation question based submitted option (if answer blank -> disable next question)
        *  */
        (activity as DetectionActivity).initSelectedOptions(
            key = Constanta.DiabetesSympthoms.Gender.name,
            option0 = binding.option0,
            option1 = binding.option1
        )
        (activity as DetectionActivity).initNavigationQuestion(
            prevQuestion = binding.prevQuestion,
            prevFragment = DetectionActivity.fragmentAge,
            nextQuestion = binding.nextQuestion,
            nextFragment = DetectionActivity.fragmentPolyuria,
            nextValidation = (activity as DetectionActivity).checkQuestionIsFilled(Constanta.DiabetesSympthoms.Gender.name)
        )

        /* init progress filled -> percentage current step of 15 question */
        binding.progressBar.progress = 7

        /* if user clicked options yes / no -> submit answer + update UI (selected option, enabled nav question) */
        binding.option1.setOnClickListener {
            /* if user click yes option */
            (activity as DetectionActivity).postValue(
                Constanta.DiabetesSympthoms.Gender.name,
                Constanta.AnsweredSympthoms.SelectedYes.name
            )
            updateUI()
        }
        binding.option0.setOnClickListener {
            /* if user click no option */
            (activity as DetectionActivity).postValue(
                Constanta.DiabetesSympthoms.Gender.name,
                Constanta.AnsweredSympthoms.SelectedNo.name
            )
            updateUI()
        }
        return binding.root
    }

    private fun updateUI() {
        (activity as DetectionActivity).initNavigationQuestion(
            prevQuestion = binding.prevQuestion,
            prevFragment = DetectionActivity.fragmentAge,
            nextQuestion = binding.nextQuestion,
            nextFragment = DetectionActivity.fragmentPolyuria,
            nextValidation = true
        )
        (activity as DetectionActivity).initSelectedOptions(
            key = Constanta.DiabetesSympthoms.Gender.name,
            option0 = binding.option0,
            option1 = binding.option1
        )
        (activity as DetectionActivity).switchFragment(DetectionActivity.fragmentPolyuria)
    }


}