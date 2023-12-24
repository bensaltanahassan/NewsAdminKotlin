package com.example.newsadmin.data

import Crud
import android.util.Log
import com.example.newsadmin.utils.ChangePasswordResponse
import com.example.newsadmin.utils.ForgetPasswordResponse
import com.example.newsadmin.utils.LoginResponse
import com.example.newsadmin.utils.SendVerifyCodeResponse
import com.example.newsadmin.utils.SignUpResponse
import com.example.newsadmin.utils.VerifyCodeResponse
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response
import okio.IOException

class AuthData {
    private val crud = Crud()
    private val gson = Gson()
    private val baseAuth = "https://news-api-8kaq.onrender.com/api/auth"





    fun login(
        email : String,
        password : String,
        onSuccess: (LoginResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val loginUrl : String = "$baseAuth/login"
        val json = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()
        crud.post(loginUrl,json,null,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val responseLogin = gson.fromJson(responseData, LoginResponse::class.java)
                onSuccess(responseLogin)
            }
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e.message!!)
            }
        }
        )
    }

    fun signUp(
        firstName : String,
        lastName : String,
        email : String,
        password : String,
        onSuccess: (SignUpResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val signUpUrl : String = "$baseAuth/signup"
        val json = """
            {
                "firstName": "$firstName",
                "lastName": "$lastName",
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()
        crud.post(signUpUrl,json,null,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val responseSignUp = gson.fromJson(responseData, SignUpResponse::class.java)
                onSuccess(responseSignUp)
            }
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e.message!!)
            }
        }
        )
    }


    fun forgetPassword(
        email : String,
        onSuccess: (ForgetPasswordResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val forgetPasswordUrl : String = "$baseAuth/forgetpassword"
        val json = """
            {
                "email": "$email"
            }
        """.trimIndent()
        crud.post(forgetPasswordUrl,json,null,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val responseForgetPassword = gson.fromJson(responseData, ForgetPasswordResponse::class.java)
                onSuccess(responseForgetPassword)
            }
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e.message!!)
            }
        }
        )
    }

    fun verifyCode(
        email : String,
        code : Int,
        onSuccess: (VerifyCodeResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val verifyCodeUrl : String = "$baseAuth/verifycode"
        val json = """
            {
                "email": "$email",
                "verifyCode": $code
            }
        """.trimIndent()
        crud.post(verifyCodeUrl,json,null,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val responseVerifyCode = gson.fromJson(responseData, VerifyCodeResponse::class.java)
                onSuccess(responseVerifyCode)
            }
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e.message!!)
            }
        }
        )
    }


    fun sendVerificationCode(
        email : String,
        onSuccess: (SendVerifyCodeResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val sendVerificationCodeUrl : String = "$baseAuth/sendverificationcode"
        val json = """
            {
                "email": "$email"
            }
        """.trimIndent()
        crud.post(sendVerificationCodeUrl,json,null,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val responseSendVerificationCode = gson.fromJson(responseData, SendVerifyCodeResponse::class.java)
                onSuccess(responseSendVerificationCode)
            }
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e.message!!)
            }
        }
        )
    }

    fun updatePassword(
        email : String,
        password : String,
        onSuccess: (ChangePasswordResponse) -> Unit,
        onFailure: (String) -> Unit
    ){
        val updatePasswordUrl : String = "$baseAuth/updatepassword"
        val json = """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()
        crud.post(updatePasswordUrl,json,null,object: Crud.ResponseCallback{
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val responseUpdatePassword = gson.fromJson(responseData, ChangePasswordResponse::class.java)
                onSuccess(responseUpdatePassword)
            }
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e.message!!)
            }
        }
        )
    }



}