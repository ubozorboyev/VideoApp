package ubr.persanal.videoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ubr.persanal.videoapp.data.MovieData

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies WHERE isDownload = 1")
    fun getDownloadedMovies(): LiveData<List<MovieData>>

    @Query("SELECT * FROM movies WHERE isDownload = 0")
    fun getDownloading(): LiveData<List<MovieData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movieData: MovieData): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieTest(list: List<MovieData>):List<Long>

    @Update
    suspend fun updateMovie(movieData: MovieData)

    @Query("SELECT * FROM movies WHERE id=:id")
    suspend fun getMovie(id: Long):MovieData


}