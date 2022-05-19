package com.insulin.app.ui.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.insulin.app.R
import com.insulin.app.adapter.ViewPagerAdapter
import com.insulin.app.ui.onBoarding.screens.OnBoarding1Fragment
import com.insulin.app.ui.onBoarding.screens.OnBoarding2Fragment
import com.insulin.app.ui.onBoarding.screens.OnBoarding3Fragment

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            OnBoarding1Fragment(),
            OnBoarding2Fragment(),
            OnBoarding3Fragment()
        )

        val viewPagerAdapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.findViewById<ViewPager2>(R.id.viewPager).adapter = viewPagerAdapter
        return view
    }

}