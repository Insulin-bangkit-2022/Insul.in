package com.insulin.app.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.insulin.app.data.model.Detection
import com.insulin.app.databinding.FragmentHistoryBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.utils.Helper

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    private val listHistory: ArrayList<Detection> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        Helper.loadHistoryData(
            context = (activity as MainActivity),
            containerNeverDetecting = binding.labelNeverDetecting,
            btnDetect = binding.btnDetection,
            progressBar = binding.progressBarHistory,
            recyclerView = binding.rvDiagnose,
            listHistory = listHistory
        )
        return binding.root
    }



}