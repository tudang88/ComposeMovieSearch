package com.example.moviessearch.screens.favorite

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.composebootcamp.moviesearch.MainCoroutineRule
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.getOrAwaitValue
import com.composebootcamp.moviesearch.repository.FakeRepository
import com.composebootcamp.moviesearch.screens.favorite.FavoriteFragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

/**
 * Test the FavoriteFragmentViewModel class
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class FavoriteFragmentViewModelTest {

    /**
     * prepare test data
     */
    private val movie1 =
        MovieEntry(1, "POSTER_URL1", "TITLE1", "2000/10/10", "THE OVERVIEW1", true)
    private val movie2 =
        MovieEntry(2, "POSTER_URL2", "TITLE2", "2000/11/10", "THE OVERVIEW2", true)
    private val movie3 =
        MovieEntry(3, "POSTER_URL3", "TITLE3", "2000/12/10", "THE OVERVIEW3", true)


    // get Rule
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    /**
     * declare test target view model
     */
    private lateinit var favoriteFragmentViewModel: FavoriteFragmentViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setupViewModel() {
        // init fake repository
        repository = FakeRepository(mutableListOf(movie1, movie2, movie3))
        favoriteFragmentViewModel = FavoriteFragmentViewModel(repository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    /**
     * case 1: existed favorite movies
     * Expect: All live data correct
     */
    @Test
    fun getAllFavorite_updateLiveData() {
        // GIVEN: there are 3 items in database
        // WHEN: open fragment
        // THEN: get data and update to live data
        assertThat(
            favoriteFragmentViewModel.allFavoriteData.getOrAwaitValue(),
            Is.`is`(listOf(movie1, movie2, movie3))
        )
        assertThat(
            favoriteFragmentViewModel.savedMovieCount.getOrAwaitValue(),
            Is.`is`(listOf(movie1, movie2, movie3).size)
        )
    }

    /**
     * case 2: existed favorite movies
     * then un-favorite one by one
     * Expect: All live data correct
     */
    @Test
    fun uncheckFavorite_updateDatabase() {
        // GIVEN: there are 3 items in database
        // WHEN: un-favorite movie 1
        favoriteFragmentViewModel.onFavoriteChanged(movie1, false)
        // THEN: get data and update to live data
        assertThat(
            favoriteFragmentViewModel.allFavoriteData.getOrAwaitValue(),
            Is.`is`(listOf(movie2, movie3))
        )
        assertThat(
            favoriteFragmentViewModel.savedMovieCount.getOrAwaitValue(),
            Is.`is`(listOf(movie2, movie3).size)
        )
        // WHEN: un-favorite movie 2
        favoriteFragmentViewModel.onFavoriteChanged(movie2, false)
        // THEN: get data and update to live data
        assertThat(
            favoriteFragmentViewModel.allFavoriteData.getOrAwaitValue(),
            Is.`is`(listOf(movie3))
        )
        assertThat(
            favoriteFragmentViewModel.savedMovieCount.getOrAwaitValue(),
            Is.`is`(listOf(movie3).size)
        )
        // WHEN: un-favorite movie 3
        favoriteFragmentViewModel.onFavoriteChanged(movie3, false)
        // THEN: get data and update to live data
        assertThat(
            favoriteFragmentViewModel.allFavoriteData.getOrAwaitValue(),
            Is.`is`(listOf())
        )
        assertThat(
            favoriteFragmentViewModel.savedMovieCount.getOrAwaitValue(),
            Is.`is`(0)
        )
    }
}