package com.example.mykotlinapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.restaurantstest.model.Favorite
import com.example.restaurantstest.model.FavoritesDao

@Database(entities = arrayOf(Favorite::class), version = 1)
abstract class RestaurantsDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    companion object {
        @Volatile
        private var instance: RestaurantsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            RestaurantsDatabase::class.java,
            "restaurantsdatabase"
        ).build()
    }
}