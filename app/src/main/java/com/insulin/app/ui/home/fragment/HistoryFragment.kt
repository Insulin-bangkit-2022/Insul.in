package com.insulin.app.ui.home.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.insulin.app.data.model.Detection
import com.insulin.app.databinding.FragmentHistoryBinding
import com.insulin.app.ui.detection.HistoryAdapter
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.utils.Constanta
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