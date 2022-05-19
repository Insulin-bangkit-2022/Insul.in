package com.insulin.app.ui.detection


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.insulin.app.R
import com.insulin.app.data.model.Detection
import com.insulin.app.databinding.ItemHistoryListBinding
import com.insulin.app.utils.Helper


class HistoryAdapter(private val data: ArrayList<Detection>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHistoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class ViewHolder(private val binding: ItemHistoryListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(history: Detection) {
            if (history.isDiabetes) {
                binding.ivDiagnoseResultIcon.setImageDrawable(binding.root.context.getDrawable(R.drawable.ic_detection_no))
                binding.tvDiagnoseResult.text = "Risiko Tinggi"
            } else {
                binding.ivDiagnoseResultIcon.setImageDrawable(binding.root.context.getDrawable(R.drawable.ic_detection_yes))
                binding.tvDiagnoseResult.text = "Risiko Rendah"
            }
            binding.tvDiagnoseTime.text = history.detectionTime
            binding.root.setOnClickListener {
                Helper.showDialogDiagnoseResult(binding.root.context, isDiabetes = history.isDiabetes)
            }
        }
    }

}