package com.insulin.app.ui.detection.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.insulin.app.R
import com.insulin.app.databinding.FragmentDetectionConfirmSympthomsBinding
import com.insulin.app.databinding.FragmentDetectionQuestionYesnoBinding
import com.insulin.app.databinding.FragmentDetectionResult0Binding
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.utils.Constanta
import com.insulin.app.utils.Helper
import java.lang.StringBuilder


class ConfirmSympthoms : Fragment() {
    private lateinit var binding: FragmentDetectionConfirmSympthomsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetectionConfirmSympthomsBinding.inflate(layoutInflater)

        binding.btnBack.setOnClickListener {
            (activity as DetectionActivity).switchFragment(DetectionActivity.fragmentObesity)
        }

        binding.btnDetection.setOnClickListener {
            (activity as DetectionActivity).switchFragment(DetectionActivity.fragmentLoading)
        }

        /* init table data */
        binding.resultDetail.age.text =
            StringBuilder( (activity as DetectionActivity).getAnsweredQuestion(Constanta.DiabetesSympthoms.Age.name).toString()).append(
                " Tahun"
            )
        binding.resultDetail.gender.text =
            if ((activity as DetectionActivity).parseAnsweredQuestion(Constanta.DiabetesSympthoms.Gender.name)) "Pria" else "Wanita"
        binding.resultDetail.isPolyuria.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.Polyuria.name)
        binding.resultDetail.isPolydipsia.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.Polydipsia.name)
        binding.resultDetail.isWeightLoss.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.WeightLoss.name)
        binding.resultDetail.isWeakness.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.Weakness.name)
        binding.resultDetail.isPolyphagia.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.Polyphagia.name)
        binding.resultDetail.isGenitalThrus.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.GenitalThrus.name)
        binding.resultDetail.isItching.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.Itching.name)
        binding.resultDetail.isIrritability.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.Irritability.name)
        binding.resultDetail.isDelayedHealing.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.DelayedHealing.name)
        binding.resultDetail.isPartialParesis.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.PartialParesis.name)
        binding.resultDetail.isMuscleStiffness.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.MuscleStiffness.name)
        binding.resultDetail.isAlopecia.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.Alopecia.name)
        binding.resultDetail.isObesity.text =
            (activity as DetectionActivity).parseAnsweredBoolean(Constanta.DiabetesSympthoms.Obesity.name)
        binding.resultDetail.isDiabetes.text = "???"

        return binding.root
    }

}