package com.example.newsadmin.data

import Crud
import com.example.newsadmin.utils.AddCategoryResponse
import com.example.newsadmin.utils.DeleteCategoryResponse
import com.example.newsadmin.utils.GetAllCategoriesResponse
import com.example.newsadmin.utils.GetSingleCategoryResponse
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import java.io.File

class CategoriesData {
    private val crud:Crud =  Crud();
    private val baseUrl : String = "https://news-api-8kaq.onrender.com/api/categories/"

    private lateinit var token:String ;

    constructor(token:String){
        this.token = token
    }







    fun addCategory(
        name:String,
        description:String,
        image:File,
        onSuccess: (AddCategoryResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val urlApi : String = baseUrl
        val fieldsMap = mapOf<String,String>(
            "name" to name,
            "description" to description
        )
        crud.postWithImage(
            urlApi,
            token,
            image,
            fieldsMap,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val addCategoryResponse = gson.fromJson(response, AddCategoryResponse::class.java)
                    onSuccess(addCategoryResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )

    }

    fun getAllCategories(
        onSuccess : (GetAllCategoriesResponse) -> Unit,
        onFailure : (String) -> Unit
    ){
        val urlApi : String = baseUrl
        crud.get(
            urlApi,
            null,
            token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val getAllCategoriesResponse = gson.fromJson(response, GetAllCategoriesResponse::class.java)
                    onSuccess(getAllCategoriesResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }
    fun getSingleCategory(
        id:String,
        onSuccess : (GetSingleCategoryResponse) -> Unit,
        onFailure : (String) -> Unit
    ){
        val urlApi : String = "$baseUrl$id"
        crud.get(
            urlApi,
            null,
            token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val getAllCategoriesResponse = gson.fromJson(response, GetSingleCategoryResponse::class.java)
                    onSuccess(getAllCategoriesResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }
    fun updateCategory(
        id:String,
        name:String,
        description:String,
        image:File,
        onSuccess: (AddCategoryResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val urlApi: String = "$baseUrl$id"
        val fieldsMap = mapOf<String, String>(
            "name" to name,
            "description" to description
        )
        crud.putWithImageAnas(
            urlApi,
            token,
            image,
            fieldsMap,
            object : Crud.ResponseCallback {
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val addCategoryResponse =
                        gson.fromJson(response, AddCategoryResponse::class.java)
                    onSuccess(addCategoryResponse)
                }

                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )


    }
    fun updateCategoryWithoutImage(
        id:String,
        name:String,
        description:String,
        onSuccess: (AddCategoryResponse) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val urlApi: String = "$baseUrl$id"
        val fieldsMap = mapOf<String, String>(
            "name" to name,
            "description" to description
        )
        crud.putWithoutImage(
            urlApi,
            token,
            fieldsMap,
            object : Crud.ResponseCallback {
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val addCategoryResponse =
                        gson.fromJson(response, AddCategoryResponse::class.java)
                    onSuccess(addCategoryResponse)
                }

                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }

    fun deleteCategory(
        id:String,
        onSuccess: (DeleteCategoryResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val urlApi: String = "$baseUrl$id"
        crud.delete(
            urlApi,
            "",
            token,
            object : Crud.ResponseCallback {
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val deleteCategoryResponse =
                        gson.fromJson(response, DeleteCategoryResponse::class.java)
                    onSuccess(deleteCategoryResponse)
                }

                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }
}