package com.composebootcamp.moviesearch.screens.favorite

import android.app.Application
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.composebootcamp.moviesearch.MainActivityViewModel
import com.composebootcamp.moviesearch.R
import com.composebootcamp.moviesearch.database.MovieEntry
import com.composebootcamp.moviesearch.repository.FakeAndroidTestRepository
import com.composebootcamp.moviesearch.repository.MovieResourceInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
import org.mockito.Mockito

/**
 * UI Testing for FavoriteFragment
 */
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class FavoriteFragmentTest : AutoCloseKoinTest() {
    /**
     * prepare test data
     */
    private val movie1 =
        MovieEntry(1, "POSTER_URL1", "TITLE1", "2000/10/10", "THE OVERVIEW1", true)
    private val movie2 =
        MovieEntry(2, "POSTER_URL2", "TITLE2", "2000/11/10", "THE OVERVIEW2", true)
    private val movie3 =
        MovieEntry(3, "POSTER_URL3", "TITLE3", "2000/12/10", "THE OVERVIEW3", true)
    private lateinit var repository: MovieResourceInterface
    private lateinit var appContext: Application
    private val koinModule = module {
        //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()

        viewModel {
            MainActivityViewModel(get() as MovieResourceInterface)
        }
        viewModel {
            FavoriteFragmentViewModel(
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
    }

    /**
     * Case 1: show favorite list
     */
    @Test
    fun openFavoriteFragment_navigateToDetails() = runBlockingTest {
        // GIVEN: show list with data from database
        val scenario = launchFragmentInContainer<FavoriteFragment>(Bundle(), R.style.AppTheme)
        val navController = Mockito.mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        // The title should show number of records
        onView(withId(R.id.favorite_title)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    appContext.getString(
                        R.string.favorite_list_title, listOf(movie1, movie2, movie3).size
                    )
                )
            )
        )
        // WHEN: click on movie 1
        onView(withId(R.id.favorite_list)).perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                ViewMatchers.hasDescendant(ViewMatchers.withText("TITLE1")),
                ViewActions.click()
            )
        )
        // THEN: navigate to DetailsFragment
        Mockito.verify(navController)
            .navigate(FavoriteFragmentDirections.showMovieDetailFromFavorite(movie1))
    }
}