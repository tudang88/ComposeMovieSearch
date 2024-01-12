package com.composebootcamp.moviesearch.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.composebootcamp.moviesearch.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class FavoriteMovieDatabaseTest {
    /**
     * prepare test data
     */
    private val movie1 =
        MovieEntry(1, "POSTER_URL1", "TITLE1", "2000/10/10", "THE OVERVIEW1", true)
    private val movie2 =
        MovieEntry(2, "POSTER_URL2", "TITLE2", "2000/11/10", "THE OVERVIEW2", true)
    private val movie3 =
        MovieEntry(3, "POSTER_URL3", "TITLE3", "2000/12/10", "THE OVERVIEW3", true)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: FavoriteMovieDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FavoriteMovieDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        database.close()
    }


    /**
     * Case 1: Insert records to database
     */
    @Test
    fun insertMovieEntry_getAllSaveItems() = runTest {
        // WHEN - insert movies
        database.favoriteDbDao.insertMovieEntry(movie1)
        database.favoriteDbDao.insertMovieEntry(movie2)
        // THEN - get
        val favoriteList = database.favoriteDbDao.getAllFavoriteMovies()
        assertThat(favoriteList.getOrAwaitValue(), `is`(listOf(movie1, movie2)))

    }

    /**
     * Case 2: delete one record to database
     */
    @Test
    fun deleteMovieEntry_getAllSaveItems() = runTest {
        // WHEN - insert movies
        database.favoriteDbDao.insertMovieEntry(movie1)
        database.favoriteDbDao.insertMovieEntry(movie2)
        database.favoriteDbDao.insertMovieEntry(movie3)
        // THEN - get
        val favoriteList = database.favoriteDbDao.getAllFavoriteMovies()
        assertThat(favoriteList.getOrAwaitValue(), `is`(listOf(movie1, movie2, movie3)))
        // WHEN - remove record
        database.favoriteDbDao.deleteFavoriteMovie(movie1.id)
        // THEN - confirm database
        val favoriteList2 = database.favoriteDbDao.getAllFavoriteMovies()
        assertThat(favoriteList2.getOrAwaitValue(), `is`(listOf(movie2, movie3)))
    }

    /**
     * Case 3: delete all record to database
     */
    @Test
    fun deleteAllMovieEntry_getAllSaveItems() = runTest {
        // WHEN - insert movies
        database.favoriteDbDao.insertMovieEntry(movie1)
        database.favoriteDbDao.insertMovieEntry(movie2)
        database.favoriteDbDao.insertMovieEntry(movie3)
        // THEN - get
        val favoriteList = database.favoriteDbDao.getAllFavoriteMovies()
        assertThat(favoriteList.getOrAwaitValue(), `is`(listOf(movie1, movie2, movie3)))
        // WHEN - remove record
        database.favoriteDbDao.clearAllFavoriteMovies()
        // THEN - confirm database
        val favoriteList2 = database.favoriteDbDao.getAllFavoriteMovies()
        assertThat(favoriteList2.getOrAwaitValue(), `is`(listOf()))
    }

    /**
     * Case 4: check one record exist ore not
     */
    @Test
    fun checkMovieEntryExist_getAllSaveItems() = runTest {
        // WHEN - insert movies
        database.favoriteDbDao.insertMovieEntry(movie1)
        database.favoriteDbDao.insertMovieEntry(movie2)
        database.favoriteDbDao.insertMovieEntry(movie3)
        // THEN - get
        val status = database.favoriteDbDao.isMovieAlreadyFavorite(movie2.id)
        assertThat(status, `is`(true))
        // WHEN - remove record
        database.favoriteDbDao.clearAllFavoriteMovies()
        // THEN - confirm database
        val status2 = database.favoriteDbDao.isMovieAlreadyFavorite(movie2.id)
        assertThat(status2, `is`(true))
    }
}