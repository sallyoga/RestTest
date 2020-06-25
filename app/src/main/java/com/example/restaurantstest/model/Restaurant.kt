package com.example.mykotlinapp.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity
data class Restaurant(

    @SerializedName("id")
    var restaurantId: Int?,

    @SerializedName("name")
    var restaurantName: String?,

    @SerializedName("minimumOrder")
    var minimumOrder: Int?,

    @SerializedName("open")
    var isOpen: Boolean?,

    @SerializedName("coverImageUrl")
    var imageUrl: String?,

    var isFavorite: Boolean?
)