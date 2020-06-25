package com.example.mykotlinapp.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mykotlinapp.viewmodel.ListViewModel
import com.example.restaurantstest.R
import com.example.restaurantstest.model.Favorite
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RestaurantsListAdapter.RestaurantsListAdapterListener {

    private lateinit var viewModel: ListViewModel
    private val restaurantsListAdapter = RestaurantsListAdapter(arrayListOf(), this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.getRestaurantsData()

        restaurants_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = restaurantsListAdapter
        }

        refresh_layout.setOnRefreshListener {
            restaurants_list.visibility = View.GONE
            error_text.visibility = View.GONE
            loading_view.visibility = View.VISIBLE
            viewModel.getRestaurantsData()
            refresh_layout.isRefreshing = false
        }

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.restaurants.observe(this, Observer { restaurants ->
            restaurants?.let {
                restaurants_list.visibility = View.VISIBLE
                restaurantsListAdapter.updateRestaurantsList(restaurants)
            }
        })

        viewModel.restaurantsLoadError.observe(this, Observer { isError ->
            isError?.let {
                error_text.visibility = if (it) View.VISIBLE else View.GONE;
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loading_view.visibility = if (it) View.VISIBLE else View.GONE;
                if (it) {
                    error_text.visibility = View.GONE;
                    restaurants_list.visibility = View.GONE;
                }
            }
        })
    }

    override fun favoriteClicked(restaurantId: Int, isFavorite: Boolean) {
        val favorite = Favorite(restaurantId = restaurantId)
        viewModel.updateFavInfo(favorite, isFavorite)
    }
}