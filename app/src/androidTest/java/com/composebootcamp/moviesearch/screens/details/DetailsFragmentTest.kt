package com.composebootcamp.moviesearch.screens.details

import android.app.Application
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.composebootcamp.moviesearch.R
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.network.model.GenresInfo
import com.composebootcamp.moviesearch.network.model.MovieDetailsResponse
import com.composebootcamp.moviesearch.network.model.getAllGenres
import com.composebootcamp.moviesearch.repository.FakeAndroidTestRepository
import com.composebootcamp.moviesearch.repository.MovieResourceInterface
import com.example.moviessearch.utils.convertTimeToString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get

/**
 * UI Testing for DetailsFragment
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class DetailsFragmentTest : AutoCloseKoinTest() {
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
    private lateinit var repository: MovieResourceInterface
    private lateinit var appContext: Application
    private val koinModule = module {
        //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
        viewModel {
            DetailsFragmentViewModel(
                get() as MovieResourceInterface
            )
        }
        // use fake repository instead of the real one
        single {
            FakeAndroidTestRepository(
                mutableListOf(
                    movie1,
                    movie2,
                    movie3
                )
            ) as MovieResourceInterface
        }
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    /**
     * As we use Koin as a Service Locator Library to develop our code, we'll also use Koin to test our code.
     * at this step we will initialize Koin related code to be able to use it in out testing.
     */
    @Before
    fun init() {
        stopKoin()//stop the original app koin
        appContext = ApplicationProvider.getApplicationContext()
        //declare a new koin module
        startKoin {
            androidContext(appContext)
            modules(listOf(koinModule))
        }
        repository = get()
        (repository as FakeAndroidTestRepository).setupMoviesDataDetails(movieDetailsResponse1)
    }

    /**
     * Case 1: show detail info when enter from favorite
     */
    @Test
    fun showDetailInfo_FromFavorite_confirmUiStatus() = runTest {
        // GIVEN - bundle with movie1
//        val arg = DetailsFragmentArgs.Builder(movie1).build()
//        val arg = DetailsFragmentArgs.fromBundle(Bundle().putParcelable("movieEntry", movie1))

        val bundle = Bundle()
        bundle.putParcelable("movieEntry", movie1)
        // WHEN - display fragment
        launchFragmentInContainer<DetailsFragment>(bundle, R.style.AppTheme)
        // THEN - the UI should reflect the data
        onView(withId(R.id.movie_title))
            .check(matches(withText(movie1.title)))
        onView(withId(R.id.movie_release_date))
            .check(matches(withText(movie1.releaseDate)))
        onView(withId(R.id.movie_genres))
            .check(matches(withText(movieDetailsResponse1.genres.getAllGenres())))
        onView(withId(R.id.movie_time))
            .check(matches(withText(convertTimeToString(movieDetailsResponse1.runtime))))
        onView(withId(R.id.movie_rating_percent))
            .check(
                matches(
                    withText(
                        appContext.getString(
                            R.string.rating_percent,
                            (movieDetailsResponse1.rating * 10).toInt()
                        )
                    )
                )
            )

        onView(withId(R.id.movie_overview))
            .check(
                matches(
                    withText(
                        movieDetailsResponse1.overview
                    )
                )
            )
        // favorite state
        onView(withId(R.id.favorite_button))
            .check(
                matches(
                    isChecked()
                )
            )
    }

    /**
     * Case 2: show detail info when enter from search
     */
    @Test
    fun showDetailInfo_FromSearch_confirmUiStatus() = runTest {
        // GIVEN - bundle with movie1
        movie1.favoriteFlag = false
//        val arg = DetailsFragmentArgs.Builder(movie1).build()
//        val bundle = arg.toBundle()
        val bundle = Bundle()
        bundle.putParcelable("movieEntry", movie1)
        // WHEN - display fragment
        launchFragmentInContainer<DetailsFragment>(bundle, R.style.AppTheme)
        // THEN - the UI should reflect the data
        onView(withId(R.id.movie_title))
            .check(matches(withText(movie1.title)))
        onView(withId(R.id.movie_release_date))
            .check(matches(withText(movie1.releaseDate)))
        onView(withId(R.id.movie_genres))
            .check(matches(withText(movieDetailsResponse1.genres.getAllGenres())))
        onView(withId(R.id.movie_time))
            .check(matches(withText(convertTimeToString(movieDetailsResponse1.runtime))))
        onView(withId(R.id.movie_rating_percent))
            .check(
                matches(
                    withText(
                        appContext.getString(
                            R.string.rating_percent,
                            (movieDetailsResponse1.rating * 10).toInt()
                        )
                    )
                )
            )

        onView(withId(R.id.movie_overview))
            .check(
                matches(
                    withText(
                        movieDetailsResponse1.overview
                    )
                )
            )
        // favorite state
        onView(withId(R.id.favorite_button))
            .check(
                matches(
                    isNotChecked()
                )
            )
    }

    /**
     * Case 3: change favorite status by click button
     */
    @Test
    fun showDetailInfo_changeFavorite_confirmUiStatus() = runTest {
        // GIVEN - bundle with movie1
        movie1.favoriteFlag = false
//        val arg = DetailsFragmentArgs.Builder(movie1).build()
//        val bundle = arg.toBundle()
        val bundle = Bundle()
        bundle.putParcelable("movieEntry", movie1)
        // WHEN - display fragment
        launchFragmentInContainer<DetailsFragment>(bundle, R.style.AppTheme)
        // THEN - the UI should reflect the data
        onView(withId(R.id.movie_title))
            .check(matches(withText(movie1.title)))
        onView(withId(R.id.movie_release_date))
            .check(matches(withText(movie1.releaseDate)))
        onView(withId(R.id.movie_genres))
            .check(matches(withText(movieDetailsResponse1.genres.getAllGenres())))
        onView(withId(R.id.movie_time))
            .check(matches(withText(convertTimeToString(movieDetailsResponse1.runtime))))
        onView(withId(R.id.movie_rating_percent))
            .check(
                matches(
                    withText(
                        appContext.getString(
                            R.string.rating_percent,
                            (movieDetailsResponse1.rating * 10).toInt()
                        )
                    )
                )
            )

        onView(withId(R.id.movie_overview))
            .check(
                matches(
                    withText(
                        movieDetailsResponse1.overview
                    )
                )
            )
        // favorite state
        onView(withId(R.id.favorite_button))
            .check(
                matches(
                    isNotChecked()
                )
            )
        // WHEN: click on favorite
        // favorite state
        onView(withId(R.id.favorite_button))
            .perform(click())
        // THEN: favorite status update
        // favorite state
        onView(withId(R.id.favorite_button))
            .check(
                matches(
                    isChecked()
                )
            )
    }
}