package com.example.newsadmin.fragments

import CustomSpinnerAdapter
import SharedPreferencesManager
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.newsadmin.adapters.CategoriesAdapter
import com.example.newsadmin.data.CategoriesData
import com.example.newsadmin.data.NewsData
import com.example.newsadmin.databinding.FragmentAddArticleBinding
import com.example.newsadmin.models.Category
import com.example.newsadmin.models.User
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class AddArticleFragment : Fragment() {
    private lateinit var _binding: FragmentAddArticleBinding
    private val binding get() = _binding
    private lateinit var categories_spinner:Spinner;
    //image
    private var file: File? = null
    private var fileUri: Uri? = null
    private var bitmap: Bitmap? = null
    private var ext:String? = null
    // end image variables
    private lateinit var sharedPref: SharedPreferencesManager
    private lateinit var user: User
    //category data
    private lateinit var categoryData: CategoriesData
    private lateinit var selectedCategory: Category
    private lateinit var categoriesData:ArrayList<Category>
    private lateinit var newsData: NewsData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //shared prefs
        sharedPref = SharedPreferencesManager.getInstance(requireContext())
        user = sharedPref.getUser()!!
        categoryData = CategoriesData(user.token!!)
        newsData = NewsData(user._id,user.token!!)

        _binding = FragmentAddArticleBinding.inflate(inflater, container, false)

        //image set up
        binding.imageAddArticle.setOnClickListener {
            pickImage()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                categoriesData = getAllCategories()
                setUpSpinnerData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        categories_spinner = binding.categorySpinner
        return binding.root
    }


    private suspend fun getAllCategories(): ArrayList<Category> {
        return suspendCoroutine { continuation ->
            val categories = ArrayList<Category>()
            categoryData.getAllCategories(
                onSuccess = { result ->
                    if (result.data.isNotEmpty()) {
                        requireActivity().runOnUiThread {
                            binding.progressBar.visibility = View.GONE
                            binding.categorySpinner.visibility = View.VISIBLE
                        }

                        result.data.forEach {
                            categories.add(it)
                        }

                        // Return the categories list
                        continuation.resume(categories)
                    }

                    requireActivity().runOnUiThread {

                    }
                },
                onFailure = { error ->
                    println(error)
                    // You may handle the error or return an empty list
                    continuation.resume(categories)
                }
            )
        }
    }

    private fun setUpSpinnerData() {
        val categoriesNames = categoriesData.map { category -> category.name }
        val adapter = CustomSpinnerAdapter(requireContext(), categoriesNames.toTypedArray())
        requireActivity().runOnUiThread {
            categories_spinner.adapter = adapter
            categories_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {
                    selectedCategory = categoriesData[position]
                    postArticle(selectedCategory._id)
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {
                }
            }
        }
    }
    private fun postArticle(categoryId:String){
        requireActivity().runOnUiThread {
            binding.addArticleButton.setOnClickListener {
                val title = binding.articleName.text.toString()
                val author = binding.articleAuthor.text.toString()
                val description = binding.descriptionArticle.text.toString()
                if (title.isEmpty() || author.isEmpty() || description.isEmpty()){
                    Toast.makeText(requireContext(),"Veuillez remplir tous les champs",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (file==null){
                    Toast.makeText(requireContext(),"Veuillez choisir une image",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                binding.progressBarAddArticle.visibility = View.VISIBLE
                binding.addArticleButton.visibility = View.GONE
                newsData.addArticle(
                    title,
                    author,
                    description,
                    file!!,
                    categoryId,
                    {
                        requireActivity().runOnUiThread {
                            binding.articleName.text.clear()
                            binding.descriptionArticle.text.clear()
                            binding.articleAuthor.text.clear()
                            binding.progressBarAddArticle.visibility = View.GONE
                            binding.addArticleButton.visibility = View.VISIBLE
                            Toast.makeText(requireContext(),"Article ajouté avec succès",Toast.LENGTH_LONG).show()
                        }

                    },
                    {
                        requireActivity().runOnUiThread {
                            binding.progressBar.visibility = View.GONE
                            binding.addArticleButton.visibility = View.VISIBLE
                        }

                    }
                )
            }
        }
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
                        binding.imageAddArticle.setImageBitmap(bitmap)

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