package com.insulin.app.ui.detection.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.insulin.app.R
import com.insulin.app.databinding.FragmentDetectionQuestionAgeBinding
import com.insulin.app.ui.detection.DetectionActivity
import com.insulin.app.utils.Helper

/* Question number 1 */
class SymptomAge : Fragment() {

    private lateinit var binding: FragmentDetectionQuestionAgeBinding

    @SuppressLint("UseCompatLoadingForDrawables", "NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetectionQuestionAgeBinding.inflate(layoutInflater)
        (activity as DetectionActivity).initNavigationQuestion(
            prevQuestion = binding.prevQuestion,
            prevFragment = DetectionActivity.fragmentGreetings,
            nextQuestion = binding.nextQuestion,
            nextFragment = DetectionActivity.fragmentGender,
            nextValidation = (activity as DetectionActivity).isAgeFilled()
        )
        binding.progressBar.progress = 0
        binding.btnOk.setOnClickListener {
            val ageString = binding.etAge.text.toString()
            when (val age: Int = if (ageString.isNotEmpty()) ageString.toInt() else 0) {
                in 1..99 -> {
                    (activity as DetectionActivity).postValueAge(age)
                    (activity as DetectionActivity).initNavigationQuestion(
                        prevQuestion = binding.prevQuestion,
                        prevFragment = DetectionActivity.fragmentGreetings,
                        nextQuestion = binding.nextQuestion,
                        nextFragment = DetectionActivity.fragmentGender,
                        nextValidation = true
                    )
                    (activity as DetectionActivity).switchFragment(DetectionActivity.fragmentGender)
                }
                else -> {
                    (activity as DetectionActivity).postValueAge(0)
                    val dialog = Helper.dialogInfoBuilder(
                        requireContext(),
                        "Isikan usia Anda",
                        "Usia belum diisi / tidak valid. \nSilahkan mengisi usia dari range 1 hingga 99 Tahun."
                    )
                    val btnOk: Button = dialog.findViewById(R.id.btn_ok)
                    btnOk.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()
                    binding.etAge.text.clear()
                    (activity as DetectionActivity).initNavigationQuestion(
                        prevQuestion = binding.prevQuestion,
                        prevFragment = DetectionActivity.fragmentGreetings,
                        nextQuestion = binding.nextQuestion,
                        nextFragment = DetectionActivity.fragmentGender,
                        nextValidation = false
                    )
                }
            }
        }
        binding.etAge.addTextChangedListener(textWatcher)
        return binding.root
    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(e: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null) {
                val text = s.toString()
                if (text.length >= 2) {
                    binding.etAge.clearFocus()
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
                }

            }
        }
    }


}