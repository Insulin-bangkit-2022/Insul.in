package com.insulin.app.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.insulin.app.data.model.Article
import com.insulin.app.databinding.FragmentArticleBinding
import com.insulin.app.ui.home.MainActivity
import com.insulin.app.utils.Helper

class ArticleFragment : Fragment() {

    private lateinit var binding: FragmentArticleBinding

    private val listArticle: ArrayList<Article> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        Helper.loadArticleData(
            context = requireContext(),
            rv = binding.rvArticle,
            articleList = listArticle,
            reference = Firebase.database.reference.child("article"), reversed = true,
            progressBar = binding.progressBarArticle
        )
        return binding.root
    }


}