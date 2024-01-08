package com.example.newsadmin.data

import Crud
import android.util.Log
import com.example.newsadmin.models.Category
import com.example.newsadmin.utils.AddCategoryResponse
import com.example.newsadmin.utils.DeleteNewsResponse
import com.example.newsadmin.utils.GetSingleNewsResponse
import com.example.newsadmin.utils.ResponseNewsData
import com.example.newsadmin.utils.UpdateArticleResponse
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import java.io.File

class NewsData {
    private val crud:Crud =  Crud();
    private val baseUrl : String = "https://news-api-8kaq.onrender.com/api"
    private lateinit var userId:String ;
    private lateinit var token:String ;

    constructor(userId:String,token:String){
        this.userId = userId
        this.token = token
    }



    fun getSingleNews(id:String,
                      onSuccess : (GetSingleNewsResponse) -> Unit,
                      onFailure : (String) -> Unit
    ){
        val urlApi = "$baseUrl/articles/$id"
        val userId:String = userId
        val json = """
            {
                "userId": "$userId"
            }
        """.trimIndent()
        val token:String = token
        crud.post(urlApi,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val singleNewsResponse = gson.fromJson(response, GetSingleNewsResponse::class.java)
                    onSuccess(singleNewsResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }


    fun getNewsByCategory(category: Category,
                          onSuccess : (ResponseNewsData) -> Unit,
                          onFailure : (String) -> Unit
                          ){
        val urlApi : String = "$baseUrl/articles?categoryId=${category._id}"
        val userId:String = userId
        val json = """
            {
                "userId": "$userId"
            }
        """.trimIndent()
        val token:String = token
        crud.get(urlApi,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val newsResponse = gson.fromJson(response, ResponseNewsData::class.java)
                    onSuccess(newsResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }

    fun searchNews(key: String,
                          onSuccess : (ResponseNewsData) -> Unit,
                          onFailure : (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/articles?title=$key"
        val userId:String = userId
        val json = """
            {
                "userId": "$userId"
            }
        """.trimIndent()
        val token:String = token
        crud.get(urlApi,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val newsResponse = gson.fromJson(response, ResponseNewsData::class.java)
                    onSuccess(newsResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }
    fun addArticle(
        title:String,
        author:String,
        description:String,
        image:File,
        categoryId:String,
        onSuccess: (AddCategoryResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/articles"
        val fieldsMap = mapOf<String,String>(
            "title" to title,
            "author" to author,
            "content" to description,
            "categoryId" to categoryId
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
                    val addArticleResponse = gson.fromJson(response, AddCategoryResponse::class.java)
                    onSuccess(addArticleResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )

    }

    fun updateArticle(
        id:String,
        title:String,
        author: String,
        description:String,
        categoryId:String,
        onSuccess: (UpdateArticleResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/articles/$id"
        val fieldsMap = mapOf<String,String>(
            "title" to title,
            "author" to author,
            "content" to description,
            "categoryId" to categoryId
        )
        crud.putWithoutImage(
            urlApi,
            token,
            fieldsMap,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val updateArticleResponse = gson.fromJson(response, UpdateArticleResponse::class.java)
                    onSuccess(updateArticleResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }
    fun updateArticleWithImage(
        id:String,
        title:String,
        author: String,
        description:String,
        categoryId:String,
        image:File,
        onSuccess: (UpdateArticleResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/articles/$id"
        val fieldsMap = mapOf<String,String>(
            "title" to title,
            "author" to author,
            "content" to description,
            "categoryId" to categoryId
        )
        crud.putWithImageAnas(
            urlApi,
            token,
            image,
            fieldsMap,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val updateArticleResponse = gson.fromJson(response, UpdateArticleResponse::class.java)
                    onSuccess(updateArticleResponse)
                    Log.d("response",updateArticleResponse.toString())
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }



    fun deleteArticle(
        id:String,
        onSuccess: (DeleteNewsResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        crud.delete(
            "$baseUrl/articles/$id",
            "",
            token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val deleteArticleResponse = gson.fromJson(response, DeleteNewsResponse::class.java)
                    onSuccess(deleteArticleResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }





}