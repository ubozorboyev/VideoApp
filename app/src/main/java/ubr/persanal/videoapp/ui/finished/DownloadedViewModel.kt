package ubr.persanal.videoapp.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ubr.persanal.videoapp.data.AppDataBase
import ubr.persanal.videoapp.data.MovieData

class DownloadedViewModel() : ViewModel() {

    private val movieDao = AppDataBase.instance!!.getMovieDao()

    val movies: LiveData<List<MovieData>> get() = movieDao.getDownloadedMovies()


}