package com.example.restaurantstest.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(

    @ColumnInfo(name = "restaurant_id")
    var restaurantId: Int?
) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int = 0
}