package com.insulin.app.ui.detection.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.insulin.app.R
import com.insulin.app.adapter.article.AffiliationProductAdapterHorizontal
import com.insulin.app.data.model.AffiliationProduct
import com.insulin.app.databinding.FragmentDetectionResult1Binding
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.ui.maps.MapsActivity
import com.insulin.app.utils.Helper


class DetectionResult1 : Fragment() {
    private lateinit var binding: FragmentDetectionResult1Binding

    private val listAffiliationProduct: ArrayList<AffiliationProduct> = ArrayList()

    @SuppressLint("NewApi", "UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as DetectionActivity).window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            statusBarColor = (activity as DetectionActivity).getColor(R.color.state_danger_dark)
        }
        binding = FragmentDetectionResult1Binding.inflate(layoutInflater)

        (activity as DetectionActivity).viewModel.data.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                binding.containerResultButton.btnCloseResult.setOnClickListener {
                    (activity as DetectionActivity).finish()
                }
                binding.containerResultButton.btnSaveResult.setOnClickListener {
                    (activity as DetectionActivity).saveDataToFirebase(data)
                }
                binding.resultDetail.age.text = StringBuilder(data.age.toString()).append(" Tahun")
                binding.resultDetail.gender.text = Helper.parseGenderAnswerDetection(data.gender)
                binding.resultDetail.isPolyuria.text =
                    Helper.parseBooleanAnswerDetection(data.isPolyuria)
                binding.resultDetail.isPolydipsia.text =
                    Helper.parseBooleanAnswerDetection(data.isPolydipsia)
                binding.resultDetail.isWeightLoss.text =
                    Helper.parseBooleanAnswerDetection(data.isWeightLoss)
                binding.resultDetail.isWeakness.text =
                    Helper.parseBooleanAnswerDetection(data.isWeakness)
                binding.resultDetail.isPolyphagia.text =
                    Helper.parseBooleanAnswerDetection(data.isPolyphagia)
                binding.resultDetail.isGenitalThrus.text =
                    Helper.parseBooleanAnswerDetection(data.isGenitalThrus)
                binding.resultDetail.isItching.text =
                    Helper.parseBooleanAnswerDetection(data.isItching)
                binding.resultDetail.isIrritability.text =
                    Helper.parseBooleanAnswerDetection(data.isIrritability)
                binding.resultDetail.isDelayedHealing.text =
                    Helper.parseBooleanAnswerDetection(data.isDelayedHealing)
                binding.resultDetail.isPartialParesis.text =
                    Helper.parseBooleanAnswerDetection(data.isPartialParesis)
                binding.resultDetail.isMuscleStiffness.text =
                    Helper.parseBooleanAnswerDetection(data.isMuscleStiffness)
                binding.resultDetail.isAlopecia.text =
                    Helper.parseBooleanAnswerDetection(data.isAlopecia)
                binding.resultDetail.isObesity.text =
                    Helper.parseBooleanAnswerDetection(data.isObesity)
                binding.resultDetail.isVisualBlurring.text =
                    Helper.parseBooleanAnswerDetection(data.isVisualBlurring)
                binding.resultDetail.isDiabetes.text =
                    Helper.parseBooleanAnswerDetection(data.isDiabetes)

                /* init layout -> hide table data */
                binding.resultDetail.root.isVisible = false
                binding.iconToggle.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_arrow_down))
                binding.resultToggle.setOnClickListener {
                    /* if detail expanded */
                    if (binding.resultDetail.root.isVisible) {
                        binding.resultDetail.root.isVisible = false
                        binding.iconToggle.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_arrow_down))
                    } else {
                        binding.resultDetail.root.isVisible = true
                        binding.iconToggle.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_arrow_up))
                    }
                }
            }

        }

        (activity as DetectionActivity).viewModel.response.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                binding.rvAffiliation.let {
                    val data = response.affiliationProduct
                    if (data != null) {
                        it.setHasFixedSize(true)
                        it.layoutManager = LinearLayoutManager(
                            (activity as DetectionActivity),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        it.isNestedScrollingEnabled = false
                        listAffiliationProduct.clear()
                        listAffiliationProduct.addAll(data)
                        it.adapter = AffiliationProductAdapterHorizontal(
                            listAffiliationProduct,
                            background = R.drawable.button_result_1
                        )
                        binding.progressBarAffiliation.isVisible = false
                    }
                }
            } else {
                binding.progressBarAffiliation.isVisible = true
            }
        }
        binding.btnHospital.setOnClickListener {
            (activity as DetectionActivity).startActivity(
                Intent(
                    (activity as DetectionActivity),
                    MapsActivity::class.java
                )
            )
        }
        return binding.root
    }
}