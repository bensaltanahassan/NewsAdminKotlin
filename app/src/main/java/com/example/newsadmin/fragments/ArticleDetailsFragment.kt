package com.example.newsadmin.fragments

import SharedPreferencesManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import com.example.newsadmin.R
import com.example.newsadmin.data.RatingData
import com.example.newsadmin.databinding.FragmentArticleDetailsBinding
import com.example.newsadmin.models.News
import com.example.newsadmin.models.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArticleDetailsFragment : Fragment() {
    private lateinit var _binding : FragmentArticleDetailsBinding
    private val binding get() = _binding!!

    private lateinit var toolbar : Toolbar
    val args: com.example.newsadmin.fragments.ArticleDetailsFragmentArgs by navArgs()
    private lateinit var news:News
    private final lateinit var rateData:RatingData
    private lateinit var sharedPref: SharedPreferencesManager
    private lateinit var user: User
    private lateinit var userId:String
    private  var token:String? = null


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_newsdetail, menu)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArticleDetailsBinding.inflate(inflater, container, false)
        news = args.news
        toolbar = binding.appbarNewsDetail.myToolBar
        toolbar.title = news.categoryId.name
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        sharedPref = SharedPreferencesManager.getInstance(requireContext())


        //Shared preferences
        user = sharedPref.getUser()!!
        userId = user._id
        token = user.token
        rateData = RatingData(userId,token!!)


        binding.articleAuthor.text = news.author
        binding.articleName.text = news.title
        binding.articleContent.text = news.content
        binding.articleDate.text = formatDate(news.createdAt)
        //load image
        val request = coil.request.ImageRequest.Builder(binding.imageArticleDetails.context)
            .data(news.image.url)
            .target(binding.imageArticleDetails)
            .target(
                onStart = {
                    binding.progressBarNewsDetail.visibility = View.VISIBLE
                },
                onSuccess = { result ->
                    binding.progressBarNewsDetail.visibility = View.GONE
                    binding.imageArticleDetails.visibility = View.VISIBLE
                    binding.imageArticleDetails.setImageDrawable(result)
                    Log.d("succes","succes")
                },
                onError = { _ ->
                    binding.progressBarNewsDetail.visibility = View.GONE
                    binding.imageArticleDetails.visibility = View.VISIBLE
                    Log.d("error","error in loading image")
                }
            ).build()
        coil.ImageLoader(binding.imageArticleDetails.context).enqueue(request)

        val ratingBar = binding.ratingBar
        ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            handleRating(news._id,ratingBar.rating.toInt())
        }


        return binding.root
    }
    fun formatDate(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val date: Date = inputFormat.parse(date)!!
        val formattedDate:String = outputFormat.format(date)
        return formattedDate
    }
    fun handleRating (articleId:String, rating:Int){
        rateData.handleRating(articleId,rating, onSuccess = {responseRateData ->
            Log.d("rating",responseRateData.toString())
            requireActivity().runOnUiThread(){
                binding.ratingId.visibility = View.VISIBLE
                binding.ratingId.text = responseRateData.data.rating.toString()
                Toast.makeText(requireContext(),"Avis ajouté avec succes",Toast.LENGTH_SHORT).show()
            }
        }, onFailure = {
            Toast.makeText(requireContext(),"Erreur lors de l'ajout de l'avis",Toast.LENGTH_SHORT).show()
        })
    }

}