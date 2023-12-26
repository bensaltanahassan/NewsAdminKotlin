package com.example.newsadmin.fragments

import Crud
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.newsadmin.R
import com.example.newsadmin.data.AuthData
import com.example.newsadmin.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private lateinit var _binding : FragmentLoginBinding
    private val binding get() = _binding!!
    val crud = Crud()
    private val authData:AuthData = AuthData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferencesManager = SharedPreferencesManager.getInstance(requireContext())
        if (sharedPreferencesManager.isLoggedIn()) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginButton.setOnClickListener {
            binding.loginButton.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            val email: String = binding.username.text.toString();
            val password: String = binding.password.text.toString()
            login(email,password)
        }

        binding.forgotPasswordButton.setOnClickListener{

        }
        binding.signupButton.setOnClickListener{
        }
        return binding.root
    }


    private fun login(email: String, password: String) {
        authData.login(
            email,
            password,
            onSuccess = { loginResponse ->
                val sharedPreferencesManager = SharedPreferencesManager.getInstance(requireContext())
                if (loginResponse.user != null) {
                    if (!loginResponse.user.isAdmin) {
                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "Vous n'avez le droit d'accéder à cette application",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        return@login
                    }
                    sharedPreferencesManager.saveUser(loginResponse.user)
                    sharedPreferencesManager.saveToken(loginResponse.user.token!!)
                    requireActivity().runOnUiThread {
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }else{
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(), loginResponse.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                requireActivity().runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.visibility = View.VISIBLE
                }


            },
            onFailure = { error ->
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    binding.loginButton.visibility = View.VISIBLE
                }
            }
        )
    }




}