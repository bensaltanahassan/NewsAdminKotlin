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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsadmin.R
import com.example.newsadmin.adapters.UsersAdapter
import com.example.newsadmin.data.CategoriesData
import com.example.newsadmin.data.UsersData
import com.example.newsadmin.databinding.FragmentCategoriesBinding
import com.example.newsadmin.databinding.FragmentUsersBinding
import com.example.newsadmin.models.Category
import com.example.newsadmin.models.User
import com.google.android.material.divider.MaterialDividerItemDecoration

class UsersFragment : Fragment() {
    private lateinit var _binding: FragmentUsersBinding
    private lateinit var toolbar : Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sharedPref: SharedPreferencesManager
    private lateinit var user: User
    private lateinit var userData: UsersData
    private lateinit var users:ArrayList<User>
    private lateinit var usersRecyclerView: RecyclerView
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

        //binding
        _binding = FragmentUsersBinding.inflate(inflater, container, false)




        //category
        userData = UsersData(user._id,user.token!!)
        usersRecyclerView = binding.recyclerViewUsers
        usersRecyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)

        getAllUsers()

        toolbar = binding.appBar.myToolBar
        toolbar.title="Utilisateurs"


        //drawer menu
        toggle = ActionBarDrawerToggle(requireActivity(),binding.drawerLayout,toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setCheckedItem(R.id.usersPageDrawer)
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.nameHeaderMenu).text = user.firstName + " " + user.lastName

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homePageDrawer -> {
                    findNavController().navigate(R.id.action_usersFragment_to_homeFragment)
                    true
                }
                R.id.categoriesPageDrawer -> {
                    findNavController().navigate(R.id.action_usersFragment_to_categoriesFragment)
                    true
                }
                R.id.usersPageDrawer -> {
                    true
                }
                R.id.profilePageDrawer -> {
                    findNavController().navigate(R.id.action_usersFragment_to_accountFragment)
                    true
                }
                R.id.logOutDrawer -> {
                    sharedPref.logout()
                    findNavController().navigate(R.id.action_usersFragment_to_loginFragment)
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
                    findNavController().navigate(R.id.action_usersFragment_to_homeFragment)
                    true
                }
                R.id.categories -> {
                    findNavController().navigate(R.id.action_usersFragment_to_categoriesFragment)
                    true
                }
                R.id.users -> {
                    true
                }
                R.id.settings -> {
                    findNavController().navigate(R.id.action_usersFragment_to_accountFragment)
                    true
                }
                else -> false
            }
        }
        // End bottom nav bar settings


        return binding.root
    }

    private fun getAllUsers() {
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)

        requireActivity().runOnUiThread {
            binding.progressBar.visibility = View.VISIBLE
            usersRecyclerView.visibility = View.GONE
        }

        users = ArrayList()
        userData.getAllUsers(
            onSuccess = { result ->
                if (result.users.isNotEmpty()){
                    result.users.forEach {
                        users.add(it)
                    }
                }

                requireActivity().runOnUiThread {
                    binding.recyclerViewUsers.adapter = UsersAdapter(
                        users,
                        findNavController(),

                    )
                    binding.recyclerViewUsers.addItemDecoration(divider)
                    binding.progressBar.visibility = View.GONE
                    usersRecyclerView.visibility = View.VISIBLE
                }


            },
            onFailure = { error ->
                println(error)
            }
        )
    }
}