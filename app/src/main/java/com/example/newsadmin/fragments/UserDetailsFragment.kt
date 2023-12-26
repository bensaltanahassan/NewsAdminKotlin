package com.example.newsadmin.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.newsadmin.databinding.FragmentArticleDetailsBinding
import com.example.newsadmin.databinding.FragmentUsersBinding
import com.example.newsadmin.models.User


class UserDetailsFragment : Fragment() {
    private lateinit var _binding : FragmentUsersBinding
    private val binding get() = _binding!!


    val args: com.example.newsadmin.fragments.UserDetailsFragmentArgs by navArgs()
    private lateinit var user:User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        user = args.user
        return binding.root
    }

}