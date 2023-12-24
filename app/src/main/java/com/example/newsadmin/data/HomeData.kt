package com.example.newsadmin.data

import Crud
import android.util.Log
import com.example.newsadmin.utils.ResponseHomeData
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException

class HomeData {
    private val crud:Crud =  Crud();
    private val baseUrl : String = "https://news-api-8kaq.onrender.com/api"
    private lateinit var userId:String ;
    private lateinit var token:String ;

    //constructor(userId:String,token:String)

    constructor(userId:String,token:String){
        this.userId = userId
        this.token = token
    }



    fun getHomeData(
         onSuccess : (ResponseHomeData) -> Unit,
            onFailure : (String) -> Unit
    ) {
        val homeUrl : String = "$baseUrl/home"
        val userId:String = "$userId"
        val json = """
            {
                "userId": "$userId"
            }
        """.trimIndent()
        val token:String = token
        crud.post(homeUrl,json,token,
            object: Crud.ResponseCallback{
                override fun onResponse(call: Call, response: Response) {
                    val response = response.body?.string()
                    val gson = Gson()
                    val homeResponse = gson.fromJson(response,ResponseHomeData::class.java)
                    onSuccess(homeResponse)
                }
                override fun onFailure(call: Call, e: IOException) {
                    onFailure(e.message!!)
                }
            }


        )
    }
}