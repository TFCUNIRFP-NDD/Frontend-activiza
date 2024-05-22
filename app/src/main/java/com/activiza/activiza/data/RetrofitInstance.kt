package com.activiza.activiza.data

import com.activiza.activiza.domain.APIListener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://34.163.215.184/activiza/"

    val api: APIListener by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIListener::class.java)
    }
}