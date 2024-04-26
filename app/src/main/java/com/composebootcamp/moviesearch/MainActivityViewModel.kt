package com.composebootcamp.moviesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.composebootcamp.moviesearch.repository.MovieResourceInterface
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainActivityViewModel(repository: MovieResourceInterface) : ViewModel() {
    val favoriteCount =
        repository.getNumOfDbRecord().stateIn(viewModelScope, SharingStarted.Eagerly, 0)
}