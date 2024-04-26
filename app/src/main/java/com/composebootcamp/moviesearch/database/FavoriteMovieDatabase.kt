package com.composebootcamp.moviesearch.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

/**
 * Dao interface for accessing favorite database
 */
@Dao
interface FavoriteDbDao {
    /**
     * check an entry already record or not
     */
    @Query("SELECT (SELECT COUNT(id) FROM favorite_table WHERE id =:movieId) > 0")
    fun isMovieAlreadyFavorite(movieId: Int): Boolean

    /**
     * Insert one record to database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieEntry(movie: MovieEntry)

    /**
     * get all current saved movie
     */
    @Query("SELECT * from favorite_table")
    fun getAllFavoriteMovies(): Flow<List<MovieEntry>>

    /**
     * delete one entry
     */
    @Query("DELETE FROM favorite_table WHERE id = :id")
    fun deleteFavoriteMovie(id: Int)

    /**
     * clear all entries
     */
    @Query("DELETE FROM favorite_table")
    fun clearAllFavoriteMovies()
}

@Database(entities = [MovieEntry::class], version = 1, exportSchema = false)
abstract class FavoriteMovieDatabase : RoomDatabase() {
    abstract val favoriteDbDao: FavoriteDbDao

    companion object {
        @Volatile
        private var INSTANCE: FavoriteMovieDatabase? = null
        fun getInstance(context: Context): FavoriteMovieDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteMovieDatabase::class.java,
                        "favorite_movie_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}