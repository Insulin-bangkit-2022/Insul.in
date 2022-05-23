package com.insulin.app.ui.detection.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.insulin.app.R
import com.insulin.app.databinding.FragmentDetectionGreetingsBinding
import com.insulin.app.databinding.FragmentDetectionQuestionYesnoBinding
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.utils.Constanta

class Greetings : Fragment() {

    private lateinit var binding: FragmentDetectionGreetingsBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetectionGreetingsBinding.inflate(layoutInflater)
        binding.btnNext.setOnClickListener {
            (activity as DetectionActivity).switchFragment(DetectionActivity.fragmentAge)
        }

        return binding.root
    }


}