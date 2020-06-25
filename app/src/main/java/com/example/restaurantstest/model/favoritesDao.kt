package com.example.restaurantstest.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoritesDao {

    @Query("SELECT * FROM favorite")
    suspend fun getAllFavorites(): List<Favorite>

    @Insert
    suspend fun insertFavorite(favorite: Favorite);

    @Query("DELETE FROM favorite WHERE restaurant_id = :restaurantId")
    suspend fun deleteRestaurantFromFavorites(restaurantId: Int?)

}