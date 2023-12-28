package com.example.newsadmin.fragments

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
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.newsadmin.R
import com.example.newsadmin.data.CategoriesData
import com.example.newsadmin.databinding.FragmentCategoryDetailsBinding
import com.example.newsadmin.databinding.FragmentUsersBinding
import com.example.newsadmin.models.Category
import com.example.newsadmin.models.User
import io.getstream.avatarview.coil.loadImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class CategoryDetailsFragment : Fragment() {
    private lateinit var _binding : FragmentCategoryDetailsBinding
    private val binding get() = _binding!!


    val args: com.example.newsadmin.fragments.CategoryDetailsFragmentArgs by navArgs()
    private lateinit var category: Category
    private lateinit var user: User
    private lateinit var categoriesData : CategoriesData
    private lateinit var sharedPref: SharedPreferencesManager

    private lateinit var toolbar : Toolbar
    //image updload variables
    private var file:File? = null
    private var fileUri: Uri? = null
    private var bitmap:Bitmap? = null
    private var ext:String? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_appbar_userdetail, menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_search -> {
                findNavController().navigate(R.id.action_categoryDetailsFragment_to_categoriesFragment)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryDetailsBinding.inflate(inflater, container, false)
        //setting up shared preferences
        sharedPref = SharedPreferencesManager.getInstance(requireContext())
        user = sharedPref.getUser()!!
        //setting up categories data
        categoriesData = CategoriesData(user.token!!)
        category = args.category
        //appbar set up
        toolbar = binding.appbarUserDetail.myToolBar
        toolbar.title = category.name
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)


        getSingleCategory()

        //setting up update logic
        binding.updateCategoryImgBtn.setOnClickListener {
            pickImage()
        }
        postData(category._id)

        return binding.root
    }
    fun postData(categoryId:String){
        requireActivity().runOnUiThread {
            binding.updateCategoryBtn.setOnClickListener {
                val name = binding.categoryName.text.toString()
                val description = binding.categoryDescription.text.toString()
                if (file==null){
                    Toast.makeText(requireContext(),"Veuillez choisir une image",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (name.isNotEmpty() && description.isNotEmpty()){
                    binding.progressBar.visibility = View.VISIBLE
                    binding.categoyHome.visibility = View.GONE
                    categoriesData.updateCategory(
                        categoryId,
                        name,
                        description,
                        file!!,
                        {
                            requireActivity().runOnUiThread {
                                binding.progressBar.visibility = View.GONE
                                binding.categoyHome.visibility = View.VISIBLE
                                Toast.makeText(requireContext(),"Catégorie modifiée avec succès",Toast.LENGTH_SHORT).show()
                            }
                        },
                        {
                            requireActivity().runOnUiThread {
                                binding.progressBar.visibility = View.GONE
                                binding.categoyHome.visibility = View.VISIBLE
                            }
                        }
                    )
                }else{
                    Toast.makeText(requireContext(),"Veuillez remplir tous les champs",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
        }
    }
    fun getSingleCategory(){
        categoriesData.getSingleCategory(
            category._id,
            {
                val categoryData = it.data.category
                requireActivity().runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    binding.categoyHome.visibility = View.VISIBLE
                    val request = coil.request.ImageRequest.Builder(binding.imageCategoryDetails.context)
                        .data(categoryData.image.url)
                        .target(binding.imageCategoryDetails)
                        .target(
                            onStart = {
                                binding.imageCategoryDetails.visibility = View.VISIBLE
                            },
                            onSuccess = { result ->
                                binding.progressBarImage.visibility = View.GONE
                                binding.imageCategoryDetails.visibility = View.VISIBLE
                                binding.imageCategoryDetails.setImageDrawable(result)
                                Log.d("succes","succes")
                            },
                            onError = { _ ->
                                binding.progressBarImage.visibility = View.GONE
                                binding.imageCategoryDetails.visibility = View.VISIBLE
                                Log.d("error","error in loading image")
                            }
                        ).build()
                    coil.ImageLoader(binding.imageCategoryDetails.context).enqueue(request)
                    binding.categoryName.setText(categoryData.name)
                    binding.categoryDescription.setText(categoryData.description)
                    binding.nbArticleInCategory.text = it.data.numberOfArticles.toString()


                }
            },
            {
                println(it)
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
                        binding.imageCategoryDetails.setImageBitmap(bitmap)

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