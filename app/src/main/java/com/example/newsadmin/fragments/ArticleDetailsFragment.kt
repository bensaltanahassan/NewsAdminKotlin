package com.example.newsadmin.fragments

import SharedPreferencesManager
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import com.example.newsadmin.R
import com.example.newsadmin.data.NewsData
import com.example.newsadmin.data.RatingData
import com.example.newsadmin.databinding.FragmentArticleDetailsBinding
import com.example.newsadmin.models.News
import com.example.newsadmin.models.User
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import android.net.Uri
import com.example.newsadmin.utils.UpdateArticleResponse

class ArticleDetailsFragment : Fragment() {
    private lateinit var _binding : FragmentArticleDetailsBinding
    private val binding get() = _binding!!

    private lateinit var toolbar : Toolbar

    //image
    private var file: File? = null
    private var fileUri: Uri? = null
    private var bitmap: Bitmap? = null
    private var ext:String? = null

    val args: com.example.newsadmin.fragments.ArticleDetailsFragmentArgs by navArgs()
    private lateinit var news:News
    private final lateinit var rateData:RatingData
    private lateinit var sharedPref: SharedPreferencesManager
    private lateinit var user: User
    private lateinit var userId:String
    private  var token:String? = null
    private lateinit var newsData: NewsData


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
        newsData = NewsData(userId,token!!)



        binding.articleAuthor.setText(news.author)
        binding.articleName.setText(news.title)
        binding.articleContent.setText(news.content)
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
        binding.updateArticleImgBtn.setOnClickListener {
            pickImage()
        }
        binding.updateArticleButton.setOnClickListener {
            binding.updateArticleButton.visibility = View.GONE
            binding.progressBarButtonClick.visibility = View.VISIBLE
            if (file==null){
                updateArticle()
            }else{
                updateArticleWithImage()
            }
        }

        binding.deleteArticleBtn.setOnClickListener {
            deleteArticle(news._id)
        }



        return binding.root
    }

    private fun deleteArticle(id: String) {
        newsData.deleteArticle(
            id,
            {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Article supprimé avec succés !", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                }
            },
            {
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            },
        )

    }

    fun updateArticleWithImage(){
        newsData.updateArticleWithImage(
            news._id,
            binding.articleName.text.toString(),
            binding.articleAuthor.text.toString(),
            binding.articleContent.text.toString(),
            news.categoryId._id,
            file!!,
            onSuccess = { result ->
                requireActivity().runOnUiThread {
                    binding.updateArticleButton.visibility = View.VISIBLE
                    binding.progressBarButtonClick.visibility = View.GONE
                    Toast.makeText(requireContext(), "Article mis à jour avec succés !", Toast.LENGTH_SHORT).show()
                }
            },
            onFailure = { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        )
    }
    fun updateArticle(){
        newsData.updateArticle(
            news._id,
            binding.articleName.text.toString(),
            binding.articleAuthor.text.toString(),
            binding.articleContent.text.toString(),
            news.categoryId._id,
            onSuccess = { result ->
                requireActivity().runOnUiThread {
                    binding.updateArticleButton.visibility = View.VISIBLE
                    binding.progressBarButtonClick.visibility = View.GONE
                    Toast.makeText(requireContext(), "Article mis à jour avec succés !", Toast.LENGTH_SHORT).show()
                }
            },
            onFailure = { error ->
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun pickImage() {
        val intent: Intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }


    private fun prepareToUploadImage(){
        val byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()
        if (bitmap!=null){
            bitmap!!.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
            val bytes= byteArrayOutputStream.toByteArray()
            val imageFile = File(requireContext().cacheDir,"image.$ext")
            imageFile.writeBytes(bytes)
            file = imageFile

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1000){
            fileUri = data?.data
            try {
                fileUri?.let { uri ->
                    val contentResolver: ContentResolver = requireActivity().contentResolver
                    val inputStream = contentResolver.openInputStream(uri)
                    val type = contentResolver.getType(uri)
                    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(type)


                    if (inputStream != null && extension != null) {
                        file = File(requireContext().cacheDir, "image.$extension")
                        inputStream.use { input ->
                            file!!.outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                        ext = extension
                        bitmap = BitmapFactory.decodeStream(FileInputStream(file))
                        binding.imageArticleDetails.setImageBitmap(bitmap)

                        prepareToUploadImage()

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}