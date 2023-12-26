package com.example.newsadmin.fragments

import SharedPreferencesManager
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsadmin.R
import com.example.newsadmin.adapters.CategoriesHomeAdapter
import com.example.newsadmin.adapters.NewsAdapter
import com.example.newsadmin.data.FavorisData
import com.example.newsadmin.data.HomeData
import com.example.newsadmin.data.NewsData
import com.example.newsadmin.databinding.FragmentHomeBinding
import com.example.newsadmin.models.Category
import com.example.newsadmin.models.Favoris
import com.example.newsadmin.models.News
import com.example.newsadmin.models.User
import com.google.android.material.divider.MaterialDividerItemDecoration
import io.getstream.avatarview.coil.loadImage

class HomeFragment : Fragment() {
    private lateinit var _binding : FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var toolbar : Toolbar
    private lateinit var homeData:HomeData
    private lateinit var newsData:NewsData
    private lateinit var favorisData:FavorisData
    private lateinit var user:User

    private lateinit var sharedPref: SharedPreferencesManager


    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newsArrayList: ArrayList<News>

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryArrayList: ArrayList<Category>

    private lateinit var listFavoris : ArrayList<Favoris>
    private lateinit var toggle:ActionBarDrawerToggle

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPref = SharedPreferencesManager.getInstance(requireContext())
        if (!sharedPref.isLoggedIn()) {
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        user = sharedPref.getUser()!!
        homeData = HomeData(user._id,user.token!!)
        newsData = NewsData(user._id,user.token!!)
        favorisData = FavorisData(user._id,user.token!!)

        newsArrayList = ArrayList<News>()
        listFavoris = ArrayList<Favoris>()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        toolbar = binding.appBarHome.myToolBar


        //floating action button
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addArticleFragment)
        }
        //end floating action button

        //drawer menu
        toggle = ActionBarDrawerToggle(requireActivity(),binding.drawerLayout,toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setCheckedItem(R.id.homePageDrawer)
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.nameHeaderMenu).text = user.firstName + " " + user.lastName

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homePageDrawer -> {
                    true
                }
                R.id.categoriesPageDrawer -> {
                    findNavController().navigate(R.id.action_homeFragment_to_categoriesFragment)
                    true
                }
                R.id.usersPageDrawer -> {
                    findNavController().navigate(R.id.action_homeFragment_to_usersFragment)
                    true
                }
                R.id.profilePageDrawer -> {
                    findNavController().navigate(R.id.action_homeFragment_to_accountFragment)
                    true
                }
                R.id.logOutDrawer -> {
                    sharedPref.logout()
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
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
            else -> R.id.home // Set a default value or handle other cases
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    true
                }
                R.id.categories -> {
                    findNavController().navigate(R.id.action_homeFragment_to_categoriesFragment)
                    true
                }
                R.id.users -> {
                    findNavController().navigate(R.id.action_homeFragment_to_usersFragment)
                    true
                }
                R.id.settings -> {
                    findNavController().navigate(R.id.action_homeFragment_to_accountFragment)
                    true
                }
                else -> false
            }
        }
        // End bottom nav bar settings


        //access to the image in the header in the drawer
        val headerView: View = binding.navView.getHeaderView(0)
        val avatarHeaderDrawer = headerView.findViewById<io.getstream.avatarview.AvatarView>(R.id.avatarViewHeader)
        val progressBarImageHeader = headerView.findViewById<ProgressBar>(R.id.progressBarImageHeader)
        avatarHeaderDrawer.loadImage(
            user.profilePhoto.url,
            onStart = {
                progressBarImageHeader.visibility = View.VISIBLE
                avatarHeaderDrawer.visibility = View.GONE
            },
            onComplete = {
                progressBarImageHeader.visibility = View.GONE
                avatarHeaderDrawer.visibility = View.VISIBLE
            },
            onError = { _, _ ->
                avatarHeaderDrawer.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.baseline_error_outline_24
                ))
                progressBarImageHeader.visibility = View.GONE
                avatarHeaderDrawer.visibility = View.VISIBLE
            },
        )

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val boxSearch: View = binding.appBarHome.boxSearchHome
        val searchView:EditText = binding.appBarHome.searchNewsHome
        val searchButton:View = binding.appBarHome.searchButtonHome
        binding.noNewsLayout.visibility = View.GONE



        val backButton:View = binding.appBarHome.backButtonHome

        backButton.setOnClickListener {
            boxSearch.visibility = View.GONE
            toolbar.title = "News App"
            toolbar.menu.findItem(R.id.action_search).isVisible = true
            backButton.visibility = View.GONE

        }


        searchButton.setOnClickListener{
            searchNews(searchView.text.toString())
        }

        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_search -> {
                    toolbar.title = ""
                    boxSearch.visibility = View.VISIBLE
                    toolbar.menu.findItem(R.id.action_search).isVisible = false
                    backButton.visibility = View.VISIBLE
                    true
                }
                else -> false
            }
        }

        categoryRecyclerView = binding.recyclerViewCategory
        categoryRecyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        categoryRecyclerView.setHasFixedSize(true)

        newRecyclerView = binding.recyclerViewNews
        newRecyclerView.layoutManager = LinearLayoutManager(context)
        newRecyclerView.setHasFixedSize(true)


        getHomeData()
        return binding.root
    }




    private fun getHomeData(){
        binding.progressBarHome.visibility = View.VISIBLE
        binding.recyclerViewNews.visibility = View.GONE
        binding.recyclerViewCategory.visibility = View.GONE
        homeData.getHomeData(
            onSuccess = { responseHomeData ->
                requireActivity().runOnUiThread {
                    binding.progressBarHome.visibility = View.GONE
                    binding.recyclerViewNews.visibility = View.VISIBLE
                    binding.recyclerViewCategory.visibility = View.VISIBLE
                    getAllCategories(responseHomeData.data.categories)
                    getAllNews(responseHomeData.data.news)
                }
            },
            onFailure = { error ->
                requireActivity().runOnUiThread {
                    binding.progressBarHome.visibility = View.GONE
                    binding.recyclerViewNews.visibility = View.GONE
                    binding.recyclerViewCategory.visibility = View.GONE
                    Log.d("error",error)
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }

        )
    }



    private fun getAllCategories(listCategory:List<Category>) {
        categoryArrayList = ArrayList<Category>()
        listCategory.forEach { categoryArrayList.add(it) }
        categoryRecyclerView.adapter  = CategoriesHomeAdapter(
            categoryArrayList,
            fun(category: Category) {
                getNewsByCategory(category)
            },
            null
            )


    }

    private fun getNewsByCategory(category: Category){
        binding.progressBarHome.visibility = View.VISIBLE
        binding.noNewsLayout.visibility = View.GONE
        binding.recyclerViewNews.visibility = View.GONE


        newsData.getNewsByCategory(category,
            onSuccess = { responseNewsData ->
                requireActivity().runOnUiThread {
                    binding.progressBarHome.visibility = View.GONE
                    binding.recyclerViewNews.visibility = View.VISIBLE

                    getAllNews(responseNewsData.data)
                }
            },
            onFailure = { error ->
                requireActivity().runOnUiThread {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun searchNews(key: String) {
        binding.progressBarHome.visibility = View.VISIBLE
        binding.recyclerViewNews.visibility = View.GONE


        newsData.searchNews(key,
            onSuccess = { responseNewsData ->
                requireActivity().runOnUiThread {
                    binding.progressBarHome.visibility = View.GONE
                    binding.recyclerViewNews.visibility = View.VISIBLE

                    getAllNews(responseNewsData.data)
                }
            },
            onFailure = { error ->
                requireActivity().runOnUiThread {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }




    private fun getAllNews(listNews:List<News>) {
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)

        requireActivity().runOnUiThread {
            newsArrayList.clear()
            listNews.forEach {
                it.isFavorite = listFavoris.any { f -> f.article._id == it._id }
                newsArrayList.add(it)
            }
            if (newRecyclerView.adapter == null) {

                newRecyclerView.adapter = NewsAdapter(
                    newsArrayList,
                    findNavController(),

                    )


                newRecyclerView.addItemDecoration(divider)

            } else {
                newRecyclerView.adapter?.notifyDataSetChanged()
            }
            if (listNews.isNotEmpty()) {
                binding.recyclerViewNews.visibility = View.VISIBLE
                binding.noNewsLayout.visibility = View.GONE
            } else {
                binding.noNewsLayout.visibility = View.VISIBLE
                binding.recyclerViewNews.visibility = View.GONE
            }
        }

    }









}