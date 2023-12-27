package com.example.newsadmin.fragments

import SharedPreferencesManager
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.newsadmin.data.CategoriesData
import com.example.newsadmin.databinding.FragmentAddCategoryBinding
import com.example.newsadmin.models.User
import io.getstream.avatarview.coil.loadImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class AddCategoryFragment : Fragment() {
    private lateinit var _binding: FragmentAddCategoryBinding
    private val binding get() = _binding


    private lateinit var categoryData: CategoriesData
    private lateinit var sharedPref: SharedPreferencesManager
    private lateinit var user: User

    //image
    private var file: File? = null
    private var fileUri: Uri? = null
    private var bitmap: Bitmap? = null
    private var ext:String? = null
    // end image variables



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //middleware login
        sharedPref = SharedPreferencesManager.getInstance(requireContext())
        user = sharedPref.getUser()!!
        _binding = FragmentAddCategoryBinding.inflate(inflater, container, false)
        categoryData = CategoriesData(user.token!!)

        binding.imageAddCategory.setOnClickListener {
            pickImage()
        }

        binding.addCategoryButton.setOnClickListener {
            val name : String = binding.nameCategory.text.toString()
            val description : String = binding.descriptionCategory.text.toString()
            if (name.length<2){
                Toast.makeText(requireContext(),"Le nom doit contenir au moins 3 caractères",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (description.length<5){
                Toast.makeText(requireContext(),"La description doit contenir au moins 5 caractères",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (file==null){
                Toast.makeText(requireContext(),"Veuillez choisir une image",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.progressBar.visibility = View.VISIBLE
            binding.addCategoryButton.visibility = View.GONE

            categoryData.addCategory(
                name,
                description,
                file!!,
                {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(),"Catégorie ajoutée avec succès",Toast.LENGTH_LONG).show()
                        binding.nameCategory.text.clear()
                        binding.descriptionCategory.text.clear()
                        binding.progressBar.visibility = View.GONE
                        binding.addCategoryButton.visibility = View.VISIBLE
                    }

                },
                {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                        binding.addCategoryButton.visibility = View.VISIBLE
                    }

                }
            )
        }



        return binding.root
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
                        binding.imageAddCategory.setImageBitmap(bitmap)

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