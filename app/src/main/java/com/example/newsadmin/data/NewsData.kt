package com.example.newsadmin.data

import Crud
import com.example.newsadmin.models.Category
import com.example.newsadmin.utils.GetSingleNewsResponse
import com.example.newsadmin.utils.ResponseNewsData
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException

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
        crud.get(urlApi,json,token,
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







}