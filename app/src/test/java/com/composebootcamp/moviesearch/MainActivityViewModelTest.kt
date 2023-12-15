package com.composebootcamp.moviesearch

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.network.model.GenresInfo
import com.composebootcamp.moviesearch.network.model.MovieDetailsResponse
import com.composebootcamp.moviesearch.repository.FakeRepository
import com.composebootcamp.moviesearch.screens.details.DetailsFragmentViewModel
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
 * Test the MainActivityViewModel class
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MainActivityViewModelTest {

    /**
     * prepare test data
     */
    private val movie1 =
        MovieEntry(1, "POSTER_URL1", "TITLE1", "2000/10/10", "THE OVERVIEW1", true)
    private val movie2 =
        MovieEntry(2, "POSTER_URL2", "TITLE2", "2000/11/10", "THE OVERVIEW2", true)
    private val movie3 =
        MovieEntry(3, "POSTER_URL3", "TITLE3", "2000/12/10", "THE OVERVIEW3", true)
    private val movieDetailsResponse1 = MovieDetailsResponse(
        false,
        "BACK_DROP_URL1",
        listOf(
            GenresInfo(1, "FICTION")
        ),
        "",
        1,
        "POSTER_URL1",
        "EN",
        "ORG_TITLE1",
        "THE OVERVIEW1",
        "2000/10/10",
        100,
        "TITLE1",
        8.1f
    )

    // get Rule
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutor = InstantTaskExecutorRule()

    /**
     * declare test target view model
     */
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var detailFragmentViewModel: DetailsFragmentViewModel
    private lateinit var favoriteFragmentViewModel: FavoriteFragmentViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setupViewModel() {
        // init fake repository
        repository = FakeRepository(mutableListOf(movie1, movie2, movie3))
        repository.setupMoviesDataDetails(movieDetailsResponse1)
        mainActivityViewModel = MainActivityViewModel(repository)
        detailFragmentViewModel = DetailsFragmentViewModel(repository)
        favoriteFragmentViewModel = FavoriteFragmentViewModel(repository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    /**
     * Case 1: open main activity when favorite data already contains items
     * Expect: the live data observe favorite count will reflect correct value
     */
    @Test
    fun loadActivity_updateBadgeInMainActivity() {
        // GIVEN: 3 movies in favorite
        // WHEN: open main activity
        // THEN: the badge should reflect value 3
        assertThat(
            mainActivityViewModel.favoriteCount.getOrAwaitValue(),
            Is.`is`(listOf(movie1, movie2, movie3).size)
        )
    }

    /**
     * Case 2: change favorite in Details, make update on badge in Main Activity
     * Expect: the live data observe favorite count will reflect correct value
     */
    @Test
    fun changeFavoriteInDetailsFragment_updateBadgeInMainActivity() {
        // GIVEN: 3 movies in favorite, and badge show 3
        assertThat(
            mainActivityViewModel.favoriteCount.getOrAwaitValue(),
            Is.`is`(listOf(movie1, movie2, movie3).size)
        )
        // Details: show movie 1
        detailFragmentViewModel.getMovieDetailsInfo(movie1)
        // WHEN: un-favorite in Details
        detailFragmentViewModel.onFavoriteChanged(false)
        // THEN: the badge should reflect value 2
        assertThat(
            mainActivityViewModel.favoriteCount.getOrAwaitValue(),
            Is.`is`(listOf(movie2, movie3).size)
        )
        // WHEN: re-favorite in Details
        detailFragmentViewModel.onFavoriteChanged(true)
        // THEN: the badge should reflect value 3
        assertThat(
            mainActivityViewModel.favoriteCount.getOrAwaitValue(),
            Is.`is`(listOf(movie1, movie2, movie3).size)
        )
    }

    /**
     * Case 3: change favorite in Favorite, make update on badge in Main Activity
     * Expect: the live data observe favorite count will reflect correct value
     */
    @Test
    fun changeFavoriteFavoriteFragment_updateBadgeInMainActivity() {
        // GIVEN: 3 movies in favorite, and badge show 3
        assertThat(
            mainActivityViewModel.favoriteCount.getOrAwaitValue(),
            Is.`is`(listOf(movie1, movie2, movie3).size)
        )
        // WHEN: un-favorite movie1 in Favorite
        favoriteFragmentViewModel.onFavoriteChanged(movie1, false)
        // THEN: the badge should reflect value 2
        assertThat(
            mainActivityViewModel.favoriteCount.getOrAwaitValue(),
            Is.`is`(listOf(movie2, movie3).size)
        )
        // WHEN: un-favorite movie2 in Favorite
        favoriteFragmentViewModel.onFavoriteChanged(movie2, false)
        // THEN: the badge should reflect value 3
        assertThat(
            mainActivityViewModel.favoriteCount.getOrAwaitValue(),
            Is.`is`(listOf(movie3).size)
        )
        // WHEN: un-favorite movie3 in Favorite
        favoriteFragmentViewModel.onFavoriteChanged(movie3, false)
        // THEN: the badge should reflect value 3
        assertThat(
            mainActivityViewModel.favoriteCount.getOrAwaitValue(),
            Is.`is`(0)
        )
    }
}