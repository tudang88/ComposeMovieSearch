package com.example.moviessearch.screens.details

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.composebootcamp.moviesearch.MainCoroutineRule
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.getOrAwaitValue
import com.composebootcamp.moviesearch.network.model.GenresInfo
import com.composebootcamp.moviesearch.network.model.MovieDetailsResponse
import com.composebootcamp.moviesearch.network.model.getAllGenres
import com.composebootcamp.moviesearch.repository.FakeRepository
import com.composebootcamp.moviesearch.screens.details.DetailsFragmentViewModel
import com.example.moviessearch.utils.convertTimeToString

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.annotation.Config

/**
 * Test the DetailFragmentViewModel class
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class DetailsFragmentViewModelTest {

    /**
     * prepare test data
     */
    private val movie1 =
        MovieEntry(1, "POSTER_URL1", "TITLE1", "2000/10/10", "THE OVERVIEW1", false)
    private val movie2 =
        MovieEntry(2, "POSTER_URL2", "TITLE2", "2000/11/10", "THE OVERVIEW2", false)
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
    private lateinit var detailFragmentViewModel: DetailsFragmentViewModel
    private lateinit var repository: FakeRepository

    @Before
    fun setupViewModel() {
        // init fake repository
        repository = FakeRepository()
        repository.setupMoviesDataDetails(movieDetailsResponse1)
        detailFragmentViewModel = DetailsFragmentViewModel(repository)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    /**
     * case 1: open detail from SearchResultFragment
     * Expect: All live data correct
     */
    @Test
    fun getMovieDetailsInfo_transitionFromSearch_notFavorite() {
        //GIVEN: the remote already have movie 1
        // movie 1 still not favorite
        // WHEN: call get detail
        detailFragmentViewModel.getMovieDetailsInfo(movie1)
        // THEN: check the live data
        assertThat(
            detailFragmentViewModel.backdropImageUrl.getOrAwaitValue(),
            `is`(movieDetailsResponse1.backdropPath)
        )
        assertThat(
            detailFragmentViewModel.title.getOrAwaitValue(),
            `is`(movieDetailsResponse1.title)
        )
        assertThat(
            detailFragmentViewModel.releaseDate.getOrAwaitValue(),
            `is`(movieDetailsResponse1.releaseDate)
        )
        assertThat(
            detailFragmentViewModel.movieTime.getOrAwaitValue(),
            `is`(convertTimeToString(movieDetailsResponse1.runtime))
        )
        assertThat(
            detailFragmentViewModel.rating.getOrAwaitValue(),
            `is`((movieDetailsResponse1.rating * 10).toInt())
        )
        assertThat(
            detailFragmentViewModel.genres.getOrAwaitValue(),
            `is`(movieDetailsResponse1.genres.getAllGenres())
        )
        assertThat(
            detailFragmentViewModel.favorite.getOrAwaitValue(),
            `is`(false)
        )
    }

    /**
     * case 2: open detail from FavoriteFragment
     * Expect: All live data correct
     */
    @Test
    fun getMovieDetailsInfo_transitionFromFavorite_Favorite() {
        //GIVEN: the remote already have movie 1
        // movie 1 is favorite
        // WHEN: call get detail
        movie1.favoriteFlag = true
        detailFragmentViewModel.getMovieDetailsInfo(movie1)
        // THEN: check the live data
        assertThat(
            detailFragmentViewModel.backdropImageUrl.getOrAwaitValue(),
            `is`(movieDetailsResponse1.backdropPath)
        )
        assertThat(
            detailFragmentViewModel.title.getOrAwaitValue(),
            `is`(movieDetailsResponse1.title)
        )
        assertThat(
            detailFragmentViewModel.releaseDate.getOrAwaitValue(),
            `is`(movieDetailsResponse1.releaseDate)
        )
        assertThat(
            detailFragmentViewModel.movieTime.getOrAwaitValue(),
            `is`(convertTimeToString(movieDetailsResponse1.runtime))
        )
        assertThat(
            detailFragmentViewModel.rating.getOrAwaitValue(),
            `is`((movieDetailsResponse1.rating * 10).toInt())
        )
        assertThat(
            detailFragmentViewModel.genres.getOrAwaitValue(),
            `is`(movieDetailsResponse1.genres.getAllGenres())
        )
        assertThat(
            detailFragmentViewModel.favorite.getOrAwaitValue(),
            `is`(true)
        )
    }

    /**
     * case 3: change favorite on DetailsFragment will trigger to update database
     * change favorite from false -> true
     */
    @Test
    fun getMovieDetailsInfo_changeFavorite_updateFavoriteDatabase() {
        //GIVEN: the remote already have movie 1
        // movie 1 still not favorite
        // WHEN: call get detail
        detailFragmentViewModel.getMovieDetailsInfo(movie1)
        movie1.favoriteFlag = true
        // change favorite
        detailFragmentViewModel.onFavoriteChanged(true)
        // THEN: check the live data favorite flag and database
        // check database
        assertThat(
            repository.getAllFavoriteMovies().getOrAwaitValue(),
            `is`(listOf(movie1))
        )
        assertThat(
            detailFragmentViewModel.favorite.getOrAwaitValue(),
            `is`(true)
        )

    }

    /**
     * case 4: change favorite on DetailsFragment will trigger to update database
     * change favorite from true -> false
     * remove from database
     */
    @Test
    fun getMovieDetailsInfo_changeUnFavorite_updateFavoriteDatabase() {
        //GIVEN: the remote already have movie 1
        movie1.favoriteFlag = true
        // movie 1 still not favorite
        // WHEN: call get detail

        detailFragmentViewModel.getMovieDetailsInfo(movie1)
        // THEN: check the live data favorite flag and database
        detailFragmentViewModel.onFavoriteChanged(true)
        // check database
        assertThat(
            repository.getAllFavoriteMovies().getOrAwaitValue(),
            `is`(listOf(movie1))
        )
        assertThat(
            detailFragmentViewModel.favorite.getOrAwaitValue(),
            `is`(true)
        )
        // WHEN: change favorite to false
        detailFragmentViewModel.onFavoriteChanged(false)
        // THEN: check database
        assertThat(
            repository.getAllFavoriteMovies().getOrAwaitValue(),
            `is`(listOf())
        )
        assertThat(
            detailFragmentViewModel.favorite.getOrAwaitValue(),
            `is`(false)
        )

    }
}