package com.composebootcamp.moviesearch

import android.app.Application
import com.composebootcamp.moviesearch.database.FavoriteMovieDatabase
import com.composebootcamp.moviesearch.repository.MovieResourceInterface
import com.composebootcamp.moviesearch.repository.MoviesRepository

import com.composebootcamp.moviesearch.screens.details.DetailsFragmentViewModel
import com.composebootcamp.moviesearch.screens.favorite.FavoriteFragmentViewModel
import com.composebootcamp.moviesearch.screens.search.SearchFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MoviesSearchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        /**
         * use Koin for Dependency Injection
         */
        val myModule = module {
            viewModel {
                MainActivityViewModel(get() as MovieResourceInterface)
            }
            viewModel { DetailsFragmentViewModel(get() as MovieResourceInterface) }
            viewModel { FavoriteFragmentViewModel(get() as MovieResourceInterface) }
            viewModel {
                SearchFragmentViewModel(get() as MovieResourceInterface)
            }
            // repository instance
            single {
                MoviesRepository(get()) as MovieResourceInterface
            }
            // database instance
            single {
                FavoriteMovieDatabase.getInstance(this@MoviesSearchApplication)
            }
        }
        // start Koin for Dependency Injection
        startKoin {
            androidContext(this@MoviesSearchApplication)
            modules(listOf(myModule))
        }
        // init timber log for debugging
        Timber.plant(Timber.DebugTree())
    }
}