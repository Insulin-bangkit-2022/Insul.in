package com.insulin.app.ui.detection.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.insulin.app.R
import com.insulin.app.databinding.FragmentDetectionQuestionYesnoBinding
import com.insulin.app.databinding.FragmentDetectionResult0Binding
import com.insulin.app.databinding.FragmentDetectionResultBinding
import com.insulin.app.ui.detection.DetectionActivity
import kotlin.random.Random


class DetectionResult0 : Fragment() {
    private lateinit var binding: FragmentDetectionResult0Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetectionResult0Binding.inflate(layoutInflater)
        binding.containerResultButton.btnCloseResult.setOnClickListener {
            (activity as DetectionActivity).finish()
        }
        binding.containerResultButton.btnSaveResult.setOnClickListener {
            (activity as DetectionActivity).saveDataToFirebase(isDiabetes = true)
        }

        return binding.root
    }

}