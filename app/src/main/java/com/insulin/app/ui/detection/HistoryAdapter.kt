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
        @SuppressLint("UseCompatLoadingForDrawables", "NewApi")
        fun bind(history: Detection) {
            if (history.isDiabetes) {
                binding.ivDiagnoseResultIcon.setImageDrawable(binding.root.context.getDrawable(R.drawable.ic_detection_no))
                binding.tvDiagnoseResult.let {
                    it.text = "Risiko Tinggi"
                    it.setTextColor(itemView.context.getColor(R.color.state_danger_dark))
                }
                binding.container.setBackgroundResource(R.drawable.custom_background_rv_danger)
            } else {
                binding.ivDiagnoseResultIcon.setImageDrawable(binding.root.context.getDrawable(R.drawable.ic_detection_yes))
                binding.tvDiagnoseResult.let {
                    it.text = "Risiko Rendah"
                    it.setTextColor(itemView.context.getColor(R.color.state_success_dark))
                }
                binding.container.setBackgroundResource(R.drawable.custom_background_rv_success)
            }
            binding.tvDiagnoseTime.text = Helper.reformatDateToSimpleDate(history.detectionTime)
            binding.root.setOnClickListener {
                Helper.showDialogDiagnoseResult(
                    binding.root.context,
                    isDiabetes = history.isDiabetes,
                    data = history
                )
            }
        }
    }

}