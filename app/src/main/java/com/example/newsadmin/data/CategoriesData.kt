package com.example.newsadmin.data

import Crud
import com.example.newsadmin.utils.GetAllCategoriesResponse
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
        image:File
    ){

        val urlApi : String = baseUrl

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
}