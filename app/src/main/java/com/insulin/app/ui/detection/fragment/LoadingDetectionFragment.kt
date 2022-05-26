package com.insulin.app.ui.detection.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.insulin.app.R
import com.insulin.app.data.model.Detection
import com.insulin.app.data.model.DetectionResponse
import com.insulin.app.data.repository.remote.api.ApiConfig
import com.insulin.app.databinding.CustomDialogInfoBinding
import com.insulin.app.databinding.FragmentLoadingDetectionBinding
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.utils.Constanta
import com.insulin.app.utils.Helper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
@SuppressLint("UseCompatLoadingForDrawables", "NewApi")
class LoadingDetectionFragment : Fragment() {

    private lateinit var binding: FragmentLoadingDetectionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoadingDetectionBinding.inflate(layoutInflater)
        (activity as DetectionActivity).viewModel.isLoadingFinished.observe(viewLifecycleOwner) { finished ->
            if (finished) {
                binding.container.setBackgroundColor(requireContext().getColor(R.color.state_success_light))
                binding.textMessage.setTextColor(requireContext().getColor(R.color.state_success_dark))
                binding.imageLoading.apply {
                    setAnimation(R.raw.success_information)
                    playAnimation()
                    loop(true)
                }
                binding.textMessage.text = "Deteksi Selesai"
            } else {
                binding.container.setBackgroundColor(requireContext().getColor(R.color.state_primary_light))
                binding.imageLoading.apply {
                    setAnimation(R.raw.upload)
                    playAnimation()
                    loop(true)
                }
                binding.textMessage.setTextColor(requireContext().getColor(R.color.state_primary_dark))
                binding.textMessage.text = "Mendeteksi Status Diabetes"
            }
        }
        (activity as DetectionActivity).viewModel.response.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                Handler().postDelayed({
                    if (data.isDiabetes) {
                        (activity as DetectionActivity).switchFragment(DetectionActivity.fragmentResult1)
                    } else {
                        (activity as DetectionActivity).switchFragment(DetectionActivity.fragmentResult0)
                    }
                }, 2000)
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initViewModelData()
        diagnoseDiabetes()
    }

    private fun initViewModelData() {
        (activity as DetectionActivity).viewModel.data.postValue(null)
        (activity as DetectionActivity).viewModel.response.postValue(null)
        (activity as DetectionActivity).viewModel.isLoadingFinished.postValue(false)
    }

    private fun diagnoseDiabetes() {
        (activity as DetectionActivity).supportActionBar?.hide()

        /* init viewModel*/
        initViewModelData()


        /* call detection API while loading UI shown */
        val client = ApiConfig.getApiService().diagnoseDiabetes()
        client.enqueue(object : Callback<DetectionResponse> {
            override fun onResponse(
                call: Call<DetectionResponse>,
                response: Response<DetectionResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: Constanta.TEMP_UID
                        val detection = Detection(
                            detection_id = Helper.getUniqueUserRelatedId(uid),
                            isDiabetes = data.isDiabetes,
                            detectionTime = Helper.getCurrentDateString(),
                            age = (activity as DetectionActivity).getAnsweredQuestion(Constanta.DiabetesSympthoms.Age.name)
                                .toString().toInt(),
                            gender = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.Gender.name
                            ),
                            isPolyuria = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.Polyuria.name
                            ),
                            isPolydipsia = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.Polydipsia.name
                            ),
                            isWeightLoss = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.WeightLoss.name
                            ),
                            isWeakness = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.Weakness.name
                            ),
                            isPolyphagia = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.Polyphagia.name
                            ),
                            isGenitalThrus = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.GenitalThrus.name
                            ),
                            isItching = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.Itching.name
                            ),
                            isIrritability = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.Irritability.name
                            ),
                            isDelayedHealing = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.DelayedHealing.name
                            ),
                            isPartialParesis = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.PartialParesis.name
                            ),
                            isMuscleStiffness = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.MuscleStiffness.name
                            ),
                            isAlopecia = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.Alopecia.name
                            ),
                            isObesity = (activity as DetectionActivity).parseAnsweredQuestion(
                                Constanta.DiabetesSympthoms.Obesity.name
                            )
                        )
                        /* trigger updates on fragmentLoading -> add delay time for extend loading animation */
                        Handler().postDelayed({
                            (activity as DetectionActivity).viewModel.isLoadingFinished.postValue(
                                true
                            )
                            (activity as DetectionActivity).viewModel.data.postValue(detection)
                            (activity as DetectionActivity).viewModel.response.postValue(data)
                        }, 1000)
                    }
                }
            }

            override fun onFailure(call: Call<DetectionResponse>, t: Throwable) {
                Log.e("api", "onFailure Call: ${t.message}")
                val dialog = Dialog((activity as DetectionActivity))
                dialog.setCancelable(false)
                dialog.window!!.apply {
                    attributes.windowAnimations = android.R.transition.fade
                    setGravity(Gravity.CENTER)
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    setLayout(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
                val binding = CustomDialogInfoBinding.inflate(layoutInflater)
                dialog.setContentView(binding.root)
                binding.dialogTitle.text = "Error"
                binding.dialogTitle.gravity = Gravity.CENTER_HORIZONTAL
                binding.dialogBody.text =
                    "Terjadi Error saat melakukan deteksi : \n\n ${t.message}"
                binding.dialogBody.gravity = Gravity.CENTER_HORIZONTAL
                binding.btnOk.setOnClickListener {
                    (activity as DetectionActivity).finish()
                }
                dialog.show()
            }
        })
    }


}