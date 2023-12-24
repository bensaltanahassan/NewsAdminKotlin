package com.example.newsadmin.fragments

import SharedPreferencesManager
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.newsadmin.R
import com.example.newsadmin.data.UsersData
import com.example.newsadmin.databinding.FragmentAccountBinding
import com.example.newsadmin.models.User
import io.getstream.avatarview.coil.loadImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream


class AccountFragment : Fragment() {


    private lateinit var _binding : FragmentAccountBinding
    private lateinit var toolbar : Toolbar
    private lateinit var toggle:ActionBarDrawerToggle
    private lateinit var sharedPref: SharedPreferencesManager
    private lateinit var user: User

    private lateinit var avatarView:io.getstream.avatarview.AvatarView
    private val binding get() = _binding!!

    private lateinit var usersData: UsersData
    private var file:File? = null
    private var fileUri:Uri? = null
    private var bitmap:Bitmap? = null
    private var ext:String? = null



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //middleware login
        sharedPref = SharedPreferencesManager.getInstance(requireContext())
        user = sharedPref.getUser()!!



        usersData = UsersData(user._id,user.token!!)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        toolbar = binding.appBarProfile.myToolBar
        toolbar.title="Profile"

        avatarView = binding.avatarView

        avatarView.loadImage(
            user.profilePhoto.url,
            onStart = {
                binding.progressBarImage.visibility = View.VISIBLE
                binding.avatarView.visibility = View.GONE
            },
            onComplete = {
                binding.progressBarImage.visibility = View.GONE
                binding.avatarView.visibility = View.VISIBLE
            },
            onError = { _, _ ->
                avatarView.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.baseline_error_outline_24
                ))
                binding.progressBarImage.visibility = View.GONE
                binding.avatarView.visibility = View.VISIBLE
            },
        )




        //drawer menu
        toggle = ActionBarDrawerToggle(requireActivity(),binding.drawerLayout,toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setCheckedItem(R.id.profilePageDrawer)
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.nameHeaderMenu).text = user.firstName + " " + user.lastName



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



        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.homePageDrawer -> {
                    findNavController().navigate(R.id.action_accountFragment_to_homeFragment)
                    true
                }
                R.id.savedNewsPageDrawer -> {
                    true
                }
                R.id.logOutDrawer -> {
                    sharedPref.logout()
                    findNavController().navigate(R.id.action_accountFragment_to_loginFragment)
                    true
                }
                else -> false
            }
        }
        //end drawer menu



        //bottom navigation view
        val bottomNavigationView = binding.bottomNavigationView
        val currentDestinationId = findNavController().currentDestination?.id
        bottomNavigationView.selectedItemId = when (currentDestinationId) {
            R.id.homeFragment -> R.id.home
            R.id.accountFragment -> R.id.settings
            else -> R.id.home
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.settings -> {
                    true
                }
                R.id.saved -> {
                    true
                }
                R.id.home -> {
                    findNavController().navigate(R.id.action_accountFragment_to_homeFragment)
                    true
                }
                else -> false
            }
        }
        //end bottom navigation view
        binding.firstName.setText(user.firstName)
        binding.lastName.setText(user.lastName)
        binding.email.setText(user.email)


        binding.updateProfileImageBtn.setOnClickListener {
            pickImage()

        }


        binding.updateProfileBtn.setOnClickListener {
            val firstName = binding.firstName.text.toString()
            val lastName = binding.lastName.text.toString()
            val email = binding.email.text.toString()
            if (!handlePasswordInput()){
                return@setOnClickListener
            }
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()  ) {
                Toast.makeText(requireContext(), "Vueillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }else{
                binding.progressBar.visibility = View.VISIBLE
                binding.updateProfileBtn.visibility = View.GONE


                if (file==null){
                    usersData.updateUser(
                        firstName,
                        lastName,
                        email,
                        binding.password.text.toString(),
                        onSuccess = {
                            if (it.status){
                                requireActivity().runOnUiThread {
                                    var updatedUser = it.user!!
                                    updatedUser.token = user.token
                                    sharedPref.saveUser(updatedUser)
                                    user = updatedUser
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                requireActivity().runOnUiThread {
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            requireActivity().runOnUiThread {
                                binding.progressBar.visibility = View.GONE
                                binding.updateProfileBtn.visibility = View.VISIBLE
                            }
                        },
                        onFailure = {
                            requireActivity().runOnUiThread {
                                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                                binding.progressBar.visibility = View.GONE
                                binding.updateProfileBtn.visibility = View.VISIBLE
                            }
                        }
                    )
                }else{
                    usersData.updateUserWithImage(
                        firstName,
                        lastName,
                        email,
                        binding.password.text.toString(),
                        file!!,
                        onSuccess = {
                            if (it.status){
                                requireActivity().runOnUiThread {
                                    var updatedUser = it.user!!
                                    updatedUser.token = user.token
                                    sharedPref.saveUser(updatedUser)
                                    user = updatedUser
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                requireActivity().runOnUiThread {
                                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                            requireActivity().runOnUiThread {
                                binding.progressBar.visibility = View.GONE
                                binding.updateProfileBtn.visibility = View.VISIBLE
                            }
                        },
                        onFailure = {
                            requireActivity().runOnUiThread {
                                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                                binding.progressBar.visibility = View.GONE
                                binding.updateProfileBtn.visibility = View.VISIBLE
                            }
                        }
                    )
                }

            }
        }
        return binding.root
    }

    private fun pickImage() {
        val intent:Intent =Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }


    private fun prepareToUploadImage(){
        val byteArrayOutputStream:ByteArrayOutputStream = ByteArrayOutputStream()
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
                        avatarView.loadImage(uri.toString())

                        prepareToUploadImage()

                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun handlePasswordInput():Boolean{
        val password = binding.password
        if (password.text.toString().isNotEmpty()){
            if (password.text.toString().length < 8){
                requireActivity().runOnUiThread {
                    Toast.makeText(requireContext(), "Le mot de passe doit contenir au moins 8 caractères", Toast.LENGTH_SHORT).show()
                }
                return false
            }else{
                //TODO
            }

        }
        return true


    }




}

