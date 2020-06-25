package com.example.mykotlinapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mykotlinapp.model.Restaurant
import com.example.restaurantstest.R
import com.example.restaurantstest.databinding.ItemRestaurantBinding
import com.example.restaurantstest.view.FavClickListener
import kotlinx.android.synthetic.main.item_restaurant.view.*

class RestaurantsListAdapter(
    val restaurantsList: ArrayList<Restaurant>,
    val listener: RestaurantsListAdapterListener
) :
    RecyclerView.Adapter<RestaurantsListAdapter.RestaurantsViewHolder>(), FavClickListener {

    fun updateRestaurantsList(newRestaurantsList: List<Restaurant>) {
        restaurantsList.clear()
        restaurantsList.addAll(newRestaurantsList)
        notifyDataSetChanged()
    }

    class RestaurantsViewHolder(var view: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(view.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemRestaurantBinding>(
            inflater,
            R.layout.item_restaurant,
            parent,
            false
        )
        return RestaurantsViewHolder(view)
    }

    override fun getItemCount() = restaurantsList.size;

    override fun onBindViewHolder(holder: RestaurantsViewHolder, position: Int) {

        holder.view.restaurant = restaurantsList[position];
        holder.view.favClickListener = this

    }

    override fun onfavoriteClicked(v: View) {

        val restaurantId = v.fav_btn.contentDescription.toString().toInt()
        val isFav = v.fav_btn.isChecked
        listener.favoriteClicked(restaurantId, isFav)
    }


    interface RestaurantsListAdapterListener {
        fun favoriteClicked(restaurantId: Int, isFavorite: Boolean)
    }
}