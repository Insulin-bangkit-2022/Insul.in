package com.insulin.app.ui.detection.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.insulin.app.R
import com.insulin.app.databinding.FragmentDetectionQuestionYesnoBinding
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.utils.Constanta

class SymptomVisualBlurring : Fragment() {

    private lateinit var binding: FragmentDetectionQuestionYesnoBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /* binding layout */
        binding = FragmentDetectionQuestionYesnoBinding.inflate(layoutInflater)

        /* === init selected answer if back from next question ===
        *  initSelectedOption -> select option yes or now based submitted value
        *  initNavigationQuestion -> disable / enable navigation question based submitted option (if answer blank -> disable next question)
        *  */

        (activity as DetectionActivity).initSelectedOptions(
            key = Constanta.DiabetesSympthoms.VirtualBlurring.name,
            option0 = binding.option0,
            option1 = binding.option1
        )
        (activity as DetectionActivity).initNavigationQuestion(
            prevQuestion = binding.prevQuestion,
            prevFragment = DetectionActivity.fragmentObesity,
            nextQuestion = binding.nextQuestion,
            nextFragment = DetectionActivity.fragmentConfirm,
            nextValidation = (activity as DetectionActivity).checkQuestionIsFilled(Constanta.DiabetesSympthoms.VirtualBlurring.name)
        )

        /* init progress filled -> percentage current step of 16 question */
        binding.progressBar.progress = 98

        binding.imageQuestion.setImageDrawable((activity as DetectionActivity).getDrawable(R.drawable.img_visual_blurring))
        binding.question.text = resources.getString(R.string.question_about_visual_blurring)

        /* if user clicked options yes / no -> submit answer + update UI (selected option, enabled nav question) */
        binding.option1.setOnClickListener {
            /* if user click yes option */
            (activity as DetectionActivity).postValue(
                Constanta.DiabetesSympthoms.VirtualBlurring.name,
                Constanta.AnsweredSympthoms.SelectedYes.name
            )
            updateUI()
        }

        binding.option0.setOnClickListener {
            /* if user click no option */
            (activity as DetectionActivity).postValue(
                Constanta.DiabetesSympthoms.VirtualBlurring.name,
                Constanta.AnsweredSympthoms.SelectedNo.name
            )
            updateUI()
        }

        return binding.root
    }

    private fun updateUI() {
        (activity as DetectionActivity).initNavigationQuestion(
            prevQuestion = binding.prevQuestion,
            prevFragment = DetectionActivity.fragmentObesity,
            nextQuestion = binding.nextQuestion,
            nextFragment = DetectionActivity.fragmentConfirm,
            nextValidation = true
        )
        (activity as DetectionActivity).initSelectedOptions(
            key = Constanta.DiabetesSympthoms.VirtualBlurring.name,
            option0 = binding.option0,
            option1 = binding.option1
        )
        (activity as DetectionActivity).switchFragment(DetectionActivity.fragmentConfirm)
    }
}