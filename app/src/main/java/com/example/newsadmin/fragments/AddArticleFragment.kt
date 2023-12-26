package com.example.newsadmin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.newsadmin.databinding.FragmentAddArticleBinding


class AddArticleFragment : Fragment() {
    private lateinit var _binding: FragmentAddArticleBinding
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddArticleBinding.inflate(inflater, container, false)
        return binding.root
    }
}