package com.insulin.app.ui.afiliation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.insulin.app.databinding.ActivityRecomendationProductBinding

class RecomendationProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecomendationProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecomendationProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}