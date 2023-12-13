package com.composebootcamp.moviesearch.utils

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.composebootcamp.moviesearch.R
import com.composebootcamp.moviesearch.database.MovieEntry

/**
 * binding list data to recycler view adapter
 */
@BindingAdapter("listData")
fun bindSearchMovieViewData(recyclerView: RecyclerView, data: List<MovieEntry>?) {
    val adapter = recyclerView.adapter as MoviesRecyclerViewAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindingImageUrl(imageView: ImageView, imgUrl: String?) {
    if (imgUrl == null || imgUrl == "") {
        imageView.setImageResource(R.drawable.image_unavailable)
    } else {
        imgUrl.let {
            val imgUri = it.toUri().buildUpon().scheme("https").build()
            Glide.with(imageView.context)
                .load(imgUri)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.image_unavailable)
                        .timeout(5000)
                )
                .into(imageView)
        }
    }
}