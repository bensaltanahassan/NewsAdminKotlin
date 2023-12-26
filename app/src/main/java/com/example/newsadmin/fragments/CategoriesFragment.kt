package com.example.newsadmin.fragments

import SharedPreferencesManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.newsadmin.R
import com.example.newsadmin.databinding.FragmentCategoriesBinding
import com.example.newsadmin.models.User

class CategoriesFragment : Fragment() {
    private lateinit var _binding: FragmentCategoriesBinding
    private lateinit var toolbar : Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sharedPref: SharedPreferencesManager
    private lateinit var user: User
    private val binding get() = _binding
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //middleware login
        sharedPref = SharedPreferencesManager.getInstance(requireContext())
        user = sharedPref.getUser()!!

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        toolbar = binding.appBar.myToolBar
        toolbar.title="Catégories"


        //floating action button
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_categoriesFragment_to_addCategoryFragment)
        }
        //end floating action button

        //drawer menu
        toggle = ActionBarDrawerToggle(requireActivity(),binding.drawerLayout,toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setCheckedItem(R.id.categoriesPageDrawer)
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.nameHeaderMenu).text = user.firstName + " " + user.lastName

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homePageDrawer -> {
                    findNavController().navigate(R.id.action_categoriesFragment_to_homeFragment)
                    true
                }
                R.id.categoriesPageDrawer -> {
                    true
                }
                R.id.usersPageDrawer -> {
                    findNavController().navigate(R.id.action_categoriesFragment_to_usersFragment)
                    true
                }
                R.id.profilePageDrawer -> {
                    findNavController().navigate(R.id.action_categoriesFragment_to_accountFragment)
                    true
                }
                R.id.logOutDrawer -> {
                    sharedPref.logout()
                    findNavController().navigate(R.id.action_categoriesFragment_to_loginFragment)
                    true
                }
                else -> false
            }
        }
        //end drawer menu

        // Bottom nav bar settings
        val bottomNavigationView = binding.bottomNavigationView
        val currentDestinationId = findNavController().currentDestination?.id
        bottomNavigationView.selectedItemId = when (currentDestinationId) {
            R.id.homeFragment -> R.id.home
            R.id.accountFragment -> R.id.settings
            R.id.categoriesFragment -> R.id.categories
            R.id.usersFragment -> R.id.users
            else -> R.id.home // Set a default value or handle other cases
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    findNavController().navigate(R.id.action_categoriesFragment_to_homeFragment)
                    true
                }
                R.id.categories -> {
                    true
                }
                R.id.users -> {
                    findNavController().navigate(R.id.action_categoriesFragment_to_usersFragment)
                    true
                }
                R.id.settings -> {
                    findNavController().navigate(R.id.action_categoriesFragment_to_accountFragment)
                    true
                }
                else -> false
            }
        }
        // End bottom nav bar settings


        return binding.root
    }
}