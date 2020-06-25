package com.example.mykotlinapp.model

import io.reactivex.Single
import retrofit2.http.GET

interface RestaurantsApi {

    @GET("restaurants/")
    fun getRestaurants(): Single<List<Restaurant>>
}