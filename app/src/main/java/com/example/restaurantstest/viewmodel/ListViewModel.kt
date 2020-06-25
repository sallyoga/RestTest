package com.example.mykotlinapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.mykotlinapp.model.Restaurant
import com.example.mykotlinapp.model.RestaurantsApiService
import com.example.mykotlinapp.model.RestaurantsDatabase
import com.example.mykotlinapp.util.getFileDataFromAsset
import com.example.restaurantstest.model.Favorite
import com.example.restaurantstest.model.MockRestaurantObject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : BaseViewModel(application) {

    private val restaurantsService = RestaurantsApiService()
    private val disposable = CompositeDisposable()

    val restaurants = MutableLiveData<List<Restaurant>>()

    val restaurantsLoadError = MutableLiveData<Boolean>()

    val loading = MutableLiveData<Boolean>()

    val favoritesArray = ArrayList<Int>()

    fun getRestaurantsData() {
        getFavorites()
        //fetchFromRemote()
        fetchFromMockJson()
    }

    private fun getFavorites() {
        launch {
            val favorites = RestaurantsDatabase(getApplication()).favoritesDao().getAllFavorites()
            for (item in favorites) {
                item.restaurantId?.let { favoritesArray.add(it) }
            }
        }
    }


    private fun fetchFromMockJson() {

        launch {
            loading.value = true

            launch {
                try {
                    val jsonFileString =
                        getFileDataFromAsset(getApplication(), "restuarants_mock.json")

                    val listRestaurantType = object : TypeToken<MockRestaurantObject>() {}.type

                    val gson = Gson()
                    val mockRestaurantObject: MockRestaurantObject =
                        gson.fromJson(jsonFileString, listRestaurantType)

                    val restaurantsList = mockRestaurantObject.restaurants

                    checkForFavorites(restaurantsList)
                    restaurantsRetrieved(restaurantsList)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            restaurantsService.getRestaurants()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Restaurant>>() {
                    override fun onSuccess(restaurantsList: List<Restaurant>) {
                        checkForFavorites(restaurantsList)
                        restaurantsRetrieved(restaurantsList)
                    }

                    override fun onError(e: Throwable) {
                        restaurantsLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun checkForFavorites(list: List<Restaurant>) {
        launch {
            for (item in list) {
                if (favoritesArray.contains(item.restaurantId)) {
                    item.isFavorite = true
                } else {
                    item.isFavorite = false;
                }
            }
        }
    }

    private fun restaurantsRetrieved(restaurantsList: List<Restaurant>) {
        restaurants.value = restaurantsList
        restaurantsLoadError.value = false
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun updateFavInfo(favorite: Favorite, isFav: Boolean) {
        launch {
            if (isFav) {
                RestaurantsDatabase(getApplication()).favoritesDao().insertFavorite(favorite)
            } else {
                RestaurantsDatabase(getApplication()).favoritesDao()
                    .deleteRestaurantFromFavorites(favorite.restaurantId)
            }
        }
    }
}