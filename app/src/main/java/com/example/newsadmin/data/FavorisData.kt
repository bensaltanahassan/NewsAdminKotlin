package com.example.newsadmin.data

import Crud
import android.util.Log
import com.example.newsadmin.models.Favoris
import com.example.newsadmin.models.News
import com.example.newsadmin.utils.ResponseAddFavoris
import com.example.newsadmin.utils.ResponseDeleteFavoris
import com.example.newsadmin.utils.ResponseGetFavoris
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException

class FavorisData {
    private val crud:Crud =  Crud();
    private val baseUrl : String = "https://news-api-8kaq.onrender.com/api"

    private lateinit var userId:String ;
    private lateinit var token:String ;

    constructor(userId:String,token:String){
        this.userId = userId
        this.token = token
    }


    fun addToFavoris(news: News,
                             onSuccess : (ResponseAddFavoris) -> Unit,
                             onFailure : (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/favoris"
        val json = """
            {
                "userId": "$userId",
                "articleId": "${news._id}"
            }
        """.trimIndent()
        crud.post(urlApi,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val addToFavorisResponse = gson.fromJson(response, ResponseAddFavoris::class.java)
                    onSuccess(addToFavorisResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }


    fun deleteFromFavoris(favoris: Favoris,
                     onSuccess : (ResponseDeleteFavoris) -> Unit,
                     onFailure : (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/favoris"
        val json = """
            {
                "favorisId": "${favoris._id}"
            }
        """.trimIndent()
        val token:String = token
        crud.delete(urlApi,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val deleteFromFavorisResponse = gson.fromJson(response, ResponseDeleteFavoris::class.java)
                    onSuccess(deleteFromFavorisResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }



     fun getAllFavoris(
                             onSuccess : (ResponseGetFavoris) -> Unit,
                             onFailure : (String) -> Unit
    ){
        val urlApi : String = "$baseUrl/favoris/getfavoris"
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
                    val getFavorisResponse = gson.fromJson(response, ResponseGetFavoris::class.java)
                    onSuccess(getFavorisResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }
        )
    }


}