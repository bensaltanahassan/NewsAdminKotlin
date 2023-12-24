package com.example.newsadmin.data

import Crud
import android.util.Log
import com.example.newsadmin.utils.ResponseNewsData
import com.example.newsadmin.utils.ResponseRateData
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException

class RatingData {
    private val crud:Crud =  Crud();
    private val baseUrl : String = "https://news-api-8kaq.onrender.com/api"
    private lateinit var userId:String ;
    private lateinit var token:String ;

    constructor(userId:String,token:String){
        this.userId = userId
        this.token = token
    }

    fun handleRating (articleId:String, rating:Int,onSuccess : (ResponseRateData)->Unit,
                      onFailure : (String) -> Unit){
        val urlApi : String = "$baseUrl/ratings"
        val userId:String = userId
        val json = """
            {
                "userId": "$userId",
                "articleId": "$articleId",
                "rating": $rating
            }
        """.trimIndent()
        val token:String = token
        crud.post(urlApi,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val rateResponse = gson.fromJson(response, ResponseRateData::class.java)
                    onSuccess(rateResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("rating",e.message!!)
                }
            }
        )
    }
}