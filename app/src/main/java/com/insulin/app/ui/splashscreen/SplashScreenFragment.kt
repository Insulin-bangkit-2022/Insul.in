package com.insulin.app.ui.splashscreen

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.insulin.app.R
import com.insulin.app.utils.Constanta.TIME_SCREEN_ACTIVITY

class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Handler().postDelayed({
            if (onBoardingFinished()) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_mainActivity)
            } else {
                findNavController().navigate(R.id.action_splashScreenFragment_to_viewPagerFragment)
            }
        }, TIME_SCREEN_ACTIVITY)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Login", false)
    }

}