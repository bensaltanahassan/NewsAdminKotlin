import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

class Crud {
    private val client = OkHttpClient()

    interface ResponseCallback {
        fun onResponse(call: Call, response: Response)
        fun onFailure(call: Call, e: IOException)
    }

    fun get(url: String,jsonBody: String?,authToken: String?, callback: ResponseCallback) {

        val requestBuilder  = Request.Builder()
            .url(url)
            .get()


        if(authToken != null){
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }


        val getRequest = requestBuilder.build()

        client.newCall(getRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }

    fun post(url: String, jsonBody: String,authToken: String?, callback: ResponseCallback) {
        val postBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
        val requestBuilder  = Request.Builder()
            .url(url)
            .post(postBody)

        if(authToken != null){
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }

        val postRequest = requestBuilder.build()


        client.newCall(postRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }
    fun update(url: String, jsonBody: String,authToken: String?, callback: ResponseCallback) {
        val putBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
        val requestBuilder  = Request.Builder()
            .url(url)
            .put(putBody)

        if(authToken != null){
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }

        val putRequest = requestBuilder.build()


        client.newCall(putRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }
            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }

    fun delete(url: String,jsonBody: String,authToken: String?, callback: ResponseCallback) {
        val deleteBody = jsonBody.toRequestBody("application/json".toMediaTypeOrNull())
        val requestBuilder  = Request.Builder()
            .url(url)
            .delete(deleteBody)

        if(authToken != null){
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }

        val deleteRequest = requestBuilder.build()

        client.newCall(deleteRequest).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }

    fun postWithImage(
        url:String,
        authToken: String?,
        file: File,
        fields: Map<String, Any>,
        callback: ResponseCallback,

    ){
        val requestBody: RequestBody
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
        fields.forEach { (key, value) ->
            builder.addFormDataPart(key, value.toString())
        }
        builder.addFormDataPart("image", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
        requestBody = builder.build()

        val requestBuilder  = Request.Builder()

        if (authToken != null) {
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }

        val request = requestBuilder
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }

    fun putWithImage(
        url: String,
        firstName:String,
        lastName:String,
        email:String,
        password:String?,
        authToken: String?,
        file:File,
        callback: ResponseCallback) {
        val requestBody: RequestBody
        if (password != null) {
            requestBody= MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
                .addFormDataPart("firstName", firstName)
                .addFormDataPart("lastName", lastName)
                .addFormDataPart("email", email)
                .addFormDataPart("password", password)
                .build()
        } else {
            requestBody= MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
                .addFormDataPart("firstName", firstName)
                .addFormDataPart("lastName", lastName)
                .addFormDataPart("email", email)
                .build()
        }
        val requestBuilder  = Request.Builder()

        if (authToken != null) {
            val headerValue = "Bearer $authToken"
            requestBuilder.addHeader("Authorization", headerValue)
        }

        val request = requestBuilder
            .url(url)
            .put(requestBody)
            .build()




        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                callback.onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onFailure(call,e)
            }
        })
    }


}
