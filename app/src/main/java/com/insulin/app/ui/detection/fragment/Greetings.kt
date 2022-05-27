package com.insulin.app.ui.detection.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.insulin.app.databinding.FragmentDetectionGreetingsBinding
import com.insulin.app.ui.detection.DetectionActivity

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