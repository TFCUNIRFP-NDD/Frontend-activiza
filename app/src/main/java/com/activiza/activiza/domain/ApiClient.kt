package com.activiza.activiza.domain

import com.activiza.activiza.core.Constants
import retrofit2.Retrofit
import com.activiza.activiza.domain.ApiServiceRegister

import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {

    private lateinit var apiServiceLogin: ApiService
    private lateinit var apiServiceRegister: ApiServiceRegister

    fun getApiServiceLogin(): ApiService {
        if (!::apiServiceLogin.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiServiceLogin = retrofit.create(ApiService::class.java)
        }
        return apiServiceLogin
    }

    fun getApiServiceRegister(): ApiServiceRegister {
        if (!::apiServiceRegister.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiServiceRegister = retrofit.create(ApiServiceRegister::class.java)
        }
        return apiServiceRegister
    }
}
