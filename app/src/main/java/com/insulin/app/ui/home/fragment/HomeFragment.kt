package com.insulin.app.ui.home.fragment


import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.DetectedActivity
import com.insulin.app.R
import com.insulin.app.databinding.ActivityMainBinding
import com.insulin.app.databinding.FragmentHomeBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.ui.maps.MapsActivity
import com.insulin.app.ui.webview.WebViewActivity
import com.insulin.app.utils.Constanta
import com.insulin.app.utils.Helper
import java.security.Permission

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val context: Context = (activity as MainActivity)

        binding.let {

            /* RS terdekat */
            it.shortuctHospital.setOnClickListener {
                if (Helper.isPermissionGranted(
                        context, Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    val intent =
                        Intent(context, MapsActivity::class.java)
                    (activity as MainActivity).startActivity(intent)
                } else {
                    (activity as MainActivity).requestPermission(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ), Constanta.LOCATION_PERMISSION_CODE
                    )
                }
            }

            /* konsultasi dokter */
            it.shortuctConsultation.setOnClickListener {
                val intent =
                    Intent(context, WebViewActivity::class.java)
                intent.putExtra(WebViewActivity.EXTRA_WEBVIEW, "https://apriantoa917.github.io")
                (activity as MainActivity).startActivity(intent)
            }

            it.shortuctInfo.setOnClickListener {
                val intent =
                    Intent(context, WebViewActivity::class.java)
                intent.putExtra(
                    WebViewActivity.EXTRA_WEBVIEW,
                    "https://hellosehat.com/diabetes/diabetes-melitus/"
                )
                (activity as MainActivity).startActivity(intent)
            }

            it.btnHelp.setOnClickListener{
                Helper.showDialogDiagnoseResult(requireContext(),isDiabetes = false)
            }
        }

        return binding.root
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}