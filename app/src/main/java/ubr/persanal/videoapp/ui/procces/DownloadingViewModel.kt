package ubr.persanal.videoapp.ui.procces

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ubr.persanal.videoapp.data.AppDataBase
import ubr.persanal.videoapp.data.MovieData

class DownloadingViewModel : ViewModel() {

    private  val TAG = "FragmentDownloading"

    private val _movieData = MutableLiveData<MovieData>()
    val movieData: LiveData<MovieData> get() = _movieData

    private val movieDao = AppDataBase.instance!!.getMovieDao()

    fun addMovie(movieData: MovieData) {
        viewModelScope.launch {
            val id = movieDao.addMovie(movieData)
            _movieData.postValue(movieDao.getMovie(id))
        }
    }

    fun updateMovie(movieData: MovieData) {
        viewModelScope.launch {
            movieDao.updateMovie(movieData)
        }
    }

    fun updateList(list: List<MovieData>) {
        viewModelScope.launch {
            val data =  movieDao.addMovieTest(list)
            Log.d(TAG, "updateMovie: data $data")
        }
    }

    fun getMoviesDownloading() = movieDao.getDownloading()


    fun loadData() {
        viewModelScope.launch {
            val testData = listOf(
                MovieData(
                    "",
                    "https://m.parvona.com/mp3/xamdam-sobirov-ayriliq-soundtrack-video-klip.mp4",
                    false,
                    0f
                ), MovieData(
                    "",
                    "https://m.parvona.com/mp3/doston-ergashev-tuyi-bulyapti-video-klip.mp4",
                    false,
                    0f
                ), MovieData(
                    "",
                    "https://m.parvona.com/mp3/oybek-ft-nigora-sen-aybdor-video-klip.mp4",
                    false,
                    0f
                ), MovieData(
                    "",
                    "https://m.parvona.com/mp3/mango-guruhi-bemorman-video-klip.mp4",
                    false,
                    0f
                ), MovieData(
                    "",
                    "https://m.parvona.com/mp3/rayhon-yig-lama-qizaloq-video-klip.mp4",
                    false,
                    0f
                ), MovieData(
                    "",
                    "https://m.parvona.com/mp3/ozoda-nursaidova-qaniydi-video-klip.mp4",
                    false,
                    0f
                ), MovieData(
                    "",
                    "https://m.parvona.com/mp3/ozoda-nursaidova-oltin-baliq-video-klip.mp4",
                    false,
                    0f
                ), MovieData(
                    "",
                    "http://topfilm.info/2/tarjima_kinolar/QO'NG'IROQSOCH_SYU_480.mp4",
                    false
                ), MovieData(
                    "",
                    "http://topfilm.info/2/tarjima_kinolar/Bolqaymoq_480.mp4",
                    false
                ), MovieData(
                    "",
                    "http://topfilm.info/2/tarjima_kinolar/TEREZA_XONIM_480.mp4",
                    false
                ), MovieData(
                    "",
                    "http://topfilm.info/2/tarjima_kinolar/SAYYOH_480.mp4",
                    false
                ), MovieData(
                    "",
                    "http://topfilm.info/2/tarjima_kinolar/Turfa_taqdirlar_480.mp4",
                    false
                ), MovieData(
                    "",
                    "http://topfilm.info/2/tarjima_kinolar/O'RTAMIZDA_TOG'LAR_480.mp4",
                    false
                ), MovieData(
                    "",
                    "http://topfilm.info/2/tarjima_kinolar/LOYQA_SUV_720.mp4",
                    false
                )
            )
            movieDao.addMovieTest(testData)
        }
    }

}