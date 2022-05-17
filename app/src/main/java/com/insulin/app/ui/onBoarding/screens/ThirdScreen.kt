package com.insulin.app.ui.onBoarding.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.common.SignInButton
import com.insulin.app.databinding.FragmentThirdScreenBinding

class ThirdScreen : Fragment() {

    private lateinit var fragmentThirdScreenBinding: FragmentThirdScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentThirdScreenBinding = FragmentThirdScreenBinding.inflate(inflater, container, false)
        fragmentThirdScreenBinding.btnGoogleLogin.setSize(SignInButton.SIZE_WIDE)

        fragmentThirdScreenBinding.btnGoogleLogin.setOnClickListener {
            onBoardingFinished()
        }

        return fragmentThirdScreenBinding.root
    }

    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Login", true)
        editor.apply()
    }
}