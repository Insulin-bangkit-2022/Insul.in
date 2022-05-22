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

        return binding.root
    }

}