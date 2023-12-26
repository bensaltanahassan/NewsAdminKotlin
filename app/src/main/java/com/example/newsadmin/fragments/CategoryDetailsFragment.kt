package com.example.newsadmin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsadmin.databinding.FragmentCategoryDetailsBinding
import com.example.newsadmin.databinding.FragmentUsersBinding
import com.example.newsadmin.models.Category
import com.example.newsadmin.models.User

class CategoryDetailsFragment : Fragment() {
    private lateinit var _binding : FragmentCategoryDetailsBinding
    private val binding get() = _binding!!


    val args: com.example.newsadmin.fragments.CategoryDetailsFragmentArgs by navArgs()
    private lateinit var category: Category


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)
        category = args.category
        return binding.root
    }
}