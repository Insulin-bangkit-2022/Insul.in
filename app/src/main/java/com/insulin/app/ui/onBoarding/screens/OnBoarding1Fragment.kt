package com.insulin.app.ui.onBoarding.screens

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.insulin.app.databinding.FragmentOnBoarding1Binding
import com.insulin.app.ui.onBoarding.OnBoardingActivity

class OnBoarding1Fragment : Fragment() {

    private lateinit var binding: FragmentOnBoarding1Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoarding1Binding.inflate(layoutInflater)

        val animation = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation

        binding.btnNext.setOnClickListener {
            (activity as OnBoardingActivity).switchScreen(
                OnBoardingActivity.screen2,
                binding.image,
                binding.tvTitle,
                binding.tvSubtitle,
                binding.indicatorOnboarding1,
                binding.indicatorOnboarding2,
                binding.indicatorOnboarding3,
                binding.btnPrev,
                binding.btnNext
            )
        }

        binding.btnSkip.setOnClickListener {
            (activity as OnBoardingActivity).redirectToLogin()
        }
        return binding.root
    }
}