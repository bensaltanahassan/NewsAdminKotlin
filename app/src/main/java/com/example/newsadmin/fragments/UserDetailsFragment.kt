package com.example.newsadmin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newsadmin.R
import com.example.newsadmin.databinding.FragmentUserDetailsBinding
import com.example.newsadmin.databinding.FragmentUsersBinding
import com.example.newsadmin.models.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class UserDetailsFragment : Fragment() {
    private lateinit var _binding : FragmentUserDetailsBinding
    private val binding get() = _binding!!

    private lateinit var toolbar : Toolbar


    val args: UserDetailsFragmentArgs by navArgs()
    private lateinit var user:User

    //appbar set up
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_userdetail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_search -> {
                findNavController().navigate(R.id.action_userDetailsFragment_to_usersFragment)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        user = args.user
        //appbar set up
        toolbar = binding.appbarUserDetail.myToolBar
        toolbar.title = "Profile"
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        //seting up user data
        //Loading image
        val request = coil.request.ImageRequest.Builder(binding.imageUserDetails.context)
            .data(user.profilePhoto.url)
            .target(binding.imageUserDetails)
            .target(
                onStart = {
                    binding.progressBarUserDetail.visibility = View.VISIBLE
                },
                onSuccess = { result ->
                    binding.progressBarUserDetail.visibility = View.GONE
                    binding.imageUserDetails.visibility = View.VISIBLE
                    binding.imageUserDetails.setImageDrawable(result)
                    Log.d("succes","succes")
                },
                onError = { _ ->
                    binding.progressBarUserDetail.visibility = View.GONE
                    binding.imageUserDetails.visibility = View.VISIBLE
                    Log.d("error","error in loading image")
                }
            ).build()
        coil.ImageLoader(binding.imageUserDetails.context).enqueue(request)
        binding.userName.text = "${user.firstName} ${user.lastName}"
        binding.uerCreatedAt.text = "Membre depuis ${formatDate(user.createdAt)}"
        if (user.isAccountVerified){
            binding.customUserCreateAt.text = "Compte vérifié"
        }else{
            binding.customUserCreateAt.text = "Compte non vérifié"
        }
        binding.userEmail.text = user.email

        Log.d("user",user.toString())
        return binding.root
    }
    fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date: Date = inputFormat.parse(date)!!
        val formattedDate:String = outputFormat.format(date)
        return formattedDate
    }
    fun Boolean.capitalizeFirstLetter(): String {
        return toString().capitalize()
    }
}
