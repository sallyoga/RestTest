package com.example.mykotlinapp.model

import com.google.gson.GsonBuilder
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class RestaurantsApiService {

    private val BASE_URL = "https://my-json-server.typicode.com/gilgoldzweig/SampleTest/tree/master/"

    var gson = GsonBuilder()
        .setLenient()
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(RestaurantsApi::class.java)

    fun getRestaurants() : Single<List<Restaurant>> {
        return api.getRestaurants()
    }
}