package com.composebootcamp.moviesearch.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.databinding.SearchResultListItemBinding


/**
 * the adapter for recyclerView on SearchFragment
 */
class MoviesRecyclerViewAdapter(
    private val onClickListener: OnMovieItemClickListener,
    private val favoriteChangedListener: OnFavoriteChangedListener?
) :
    ListAdapter<MovieEntry, ResultEntryViewHolder>(ResultEntryDiffCallback) {
    /**
     * The ViewHolder will be separate to individual class as bellow
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultEntryViewHolder {
        return ResultEntryViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ResultEntryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClickListener, favoriteChangedListener)
    }
}

/**
 * The viewHolder implementation is separated
 */
class ResultEntryViewHolder(val binding: SearchResultListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: MovieEntry,
        clickListener: OnMovieItemClickListener,
        favoriteChangedListener: OnFavoriteChangedListener?
    ) {
        // binding the entry layout associate with data here
        binding.resultEntry = item
        binding.clickListener = clickListener
        if (favoriteChangedListener == null) {
            binding.favoriteStateButton.visibility = View.GONE
        } else {
            binding.favoriteChangedListener = favoriteChangedListener
        }
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ResultEntryViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = SearchResultListItemBinding.inflate(layoutInflater, parent, false)
            return ResultEntryViewHolder(binding)
        }
    }
}

/**
 * Diff util for ResultEntry
 */
object ResultEntryDiffCallback : DiffUtil.ItemCallback<MovieEntry>() {
    override fun areItemsTheSame(oldItem: MovieEntry, newItem: MovieEntry): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieEntry, newItem: MovieEntry): Boolean {
        return oldItem == newItem
    }
}

/**
 * OnClickListener interface for each item
 */
class OnMovieItemClickListener(val clickListener: (movie: MovieEntry) -> Unit) {
    fun onClick(movie: MovieEntry) = clickListener(movie)
}

/**
 * OnFavoriteChangeListener interface for each item
 */
class OnFavoriteChangedListener(val favoriteChangeListener: (movie: MovieEntry, isCheck: Boolean) -> Unit) {
    fun onChanged(movie: MovieEntry, isCheck: Boolean) = favoriteChangeListener(movie, isCheck)
}
