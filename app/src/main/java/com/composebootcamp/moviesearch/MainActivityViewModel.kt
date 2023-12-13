package com.composebootcamp.moviesearch

import androidx.lifecycle.ViewModel
import com.composebootcamp.moviesearch.repository.MovieResourceInterface

class MainActivityViewModel(repository: MovieResourceInterface) : ViewModel() {
    val favoriteCount = repository.getNumOfDbRecord()
}