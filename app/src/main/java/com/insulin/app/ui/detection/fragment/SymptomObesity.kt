package com.insulin.app.ui.detection.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.insulin.app.R
import com.insulin.app.databinding.FragmentDetectionQuestionYesnoBinding
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.utils.Constanta

class SymptomObesity : Fragment() {

    private lateinit var binding: FragmentDetectionQuestionYesnoBinding
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetectionQuestionYesnoBinding.inflate(layoutInflater)
        binding.imageQuestion.setImageDrawable((activity as DetectionActivity).getDrawable(R.drawable.ic_baseline_history))
        binding.question.text = "Apakah obesitas?"
        binding.option1.setOnClickListener {
            /* if user click yes option */
            (activity as DetectionActivity).postValue(
                Constanta.DiabetesSympthoms.Obesity.name,
                Constanta.AnsweredSympthoms.SelectedYes.name
            )
            binding.nextQuestion.performClick()
            setBackground()
        }
        binding.option0.setOnClickListener {
            /* if user click no option */
            (activity as DetectionActivity).postValue(
                Constanta.DiabetesSympthoms.Obesity.name,
                Constanta.AnsweredSympthoms.SelectedNo.name
            )
            binding.nextQuestion.performClick()
            setBackground()
        }
        binding.prevQuestion.setOnClickListener {
            /* if user click previous question */
            (activity as DetectionActivity).switchFragment(DetectionActivity.fragmentAlopecia)
        }
        binding.nextQuestion.setOnClickListener {
            /* if user click next question */
            (activity as DetectionActivity).switchFragment(DetectionActivity.fragmentConfirm)
        }

        setBackground()


        return binding.root
    }

    fun setBackground(){
        (activity as DetectionActivity).setBackgroundState(
            key = Constanta.DiabetesSympthoms.Obesity.name,
            option0 = binding.option0,
            option1 = binding.option1
        )
    }



}