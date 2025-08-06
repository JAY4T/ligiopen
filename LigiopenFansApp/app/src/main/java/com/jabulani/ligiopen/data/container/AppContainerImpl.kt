package com.jabulani.ligiopen.data.container

import android.content.Context
import com.jabulani.ligiopen.data.db.AppDatabase
import com.jabulani.ligiopen.data.db.DBRepository
import com.jabulani.ligiopen.data.db.DBRepositoryImpl
import com.jabulani.ligiopen.data.network.ApiRepository
import com.jabulani.ligiopen.data.network.ApiRepositoryImpl
import com.jabulani.ligiopen.data.network.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

class AppContainerImpl(context: Context) : AppContainer {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
//    private val baseUrl = "https://ligiopen-036cef24cac8.herokuapp.com/api/"
    private val baseUrl = "http://167.71.35.181:8080/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    override val apiRepository: ApiRepository by lazy {
        ApiRepositoryImpl(retrofitService)
    }

    override val dbRepository: DBRepository by lazy {
        DBRepositoryImpl(AppDatabase.getDatabase(context).appDao())
    }
}