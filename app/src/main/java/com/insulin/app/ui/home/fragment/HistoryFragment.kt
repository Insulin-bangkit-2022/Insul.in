package com.insulin.app.ui.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.insulin.app.data.model.Detection
import com.insulin.app.databinding.FragmentHistoryBinding
import com.insulin.app.ui.detection.HistoryAdapter
import com.insulin.app.utils.Constanta


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    private val listHistory: ArrayList<Detection> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        loadHistoryData()
        return binding.root
    }

    private fun loadHistoryData() {
        binding.progressBarHistory.isVisible = true
        val tag = "FIREBASE"
        val database =
            Firebase.database.reference.child("detection_history").child(Constanta.TEMP_UID)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listHistory.clear()
                for (history in dataSnapshot.children) {
                    val data = history.getValue<Detection>()
                    data?.let {
                        listHistory.add(it)
                    }
                }
                binding.rvDiagnose.let {
                    it.setHasFixedSize(true)
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.isNestedScrollingEnabled = false
                    listHistory.reverse()
                    it.adapter = HistoryAdapter(listHistory)
                }
                binding.progressBarHistory.isVisible = false
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(tag, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

}