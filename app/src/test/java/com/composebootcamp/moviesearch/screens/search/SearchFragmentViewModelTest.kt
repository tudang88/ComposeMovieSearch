package com.example.moviessearch.screens.search

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.composebootcamp.moviesearch.MainCoroutineRule
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.getOrAwaitValue
import com.composebootcamp.moviesearch.repository.FakeRepository
import com.composebootcamp.moviesearch.screens.search.SearchFragmentViewModel
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
 * Test the SearchFragmentViewModel class
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class SearchFragmentViewModelTest {

    /**
     * prepare test data
     */
    private val movie1 =
        MovieEntry(1, "POSTER_URL1", "TITLE1", "2000/10/10", "THE OVERVIEW1", false)
    private val movie2 =
        MovieEntry(2, "POSTER_URL2", "TITLE2", "2000/11/10", "THE OVERVIEW2", false)
    private val movie3 =
        MovieEntry(3, "POSTER_URL3", "TITLE3", "2000/12/10", "THE OVERVIEW3", false)


    // get Rule
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    /**
     * declare test target view model
     */
    private lateinit var searchFragmentViewModel: SearchFragmentViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setupViewModel() {
        // init fake repository
        repository = FakeRepository()
        repository.setupMoviesDataForSearch(movie1, movie2, movie3)
        searchFragmentViewModel = SearchFragmentViewModel(repository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    /**
     * case 1: found movie from remote
     * Expect: All live data correct
     */
    @Test
    fun searchMovie_updateLiveData() {
        //GIVEN: there are 3 movies on remotes, and search keyword = TITLE
        searchFragmentViewModel.keyword.value = "TITLE"
        //WHEN: search
        searchFragmentViewModel.onSearchMovies()
        //THEN:
        assertThat(
            searchFragmentViewModel.searchResult.getOrAwaitValue(),
            Is.`is`(listOf(movie1, movie2, movie3))
        )
        assertThat(
            searchFragmentViewModel.foundMovieCount.getOrAwaitValue(),
            Is.`is`(listOf(movie1, movie2, movie3).size)
        )
    }

    /**
     * case 2, 3: not found movie from remote, then change keyword -> found result
     * Expect: All live data correct
     */
    @Test
    fun searchMovie_notFound_updateLiveData() {
        //GIVEN: there are 3 movies on remotes, and search keyword = TITLE
        searchFragmentViewModel.keyword.value = "MY_TITLE"
        //WHEN: search
        searchFragmentViewModel.onSearchMovies()
        //THEN:
        assertThat(
            searchFragmentViewModel.searchResult.getOrAwaitValue(),
            Is.`is`(listOf())
        )
        assertThat(
            searchFragmentViewModel.foundMovieCount.getOrAwaitValue(),
            Is.`is`(0)
        )
        // THEN: Change keyword, found result
        searchFragmentViewModel.keyword.value = "TITLE1"
        //WHEN: search one more time
        searchFragmentViewModel.onSearchMovies()
        //THEN:
        assertThat(
            searchFragmentViewModel.searchResult.getOrAwaitValue(),
            Is.`is`(listOf(movie1))
        )
        assertThat(
            searchFragmentViewModel.foundMovieCount.getOrAwaitValue(),
            Is.`is`(listOf(movie1).size)
        )

        // THEN: Change keyword, found result
        searchFragmentViewModel.keyword.value = "TITLE"
        //WHEN: search one more time
        searchFragmentViewModel.onSearchMovies()
        //THEN:
        assertThat(
            searchFragmentViewModel.searchResult.getOrAwaitValue(),
            Is.`is`(listOf(movie1, movie2, movie3))
        )
        assertThat(
            searchFragmentViewModel.foundMovieCount.getOrAwaitValue(),
            Is.`is`(listOf(movie1, movie2, movie3).size)
        )
    }
}