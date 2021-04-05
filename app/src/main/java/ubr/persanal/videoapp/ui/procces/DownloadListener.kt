package ubr.persanal.videoapp.ui.procces

import ubr.persanal.videoapp.data.MovieData

interface DownloadListener {

        fun startDownload(movieData: MovieData, position: Int)

        fun pauseDownload(movieData: MovieData, position: Int)

        fun stopDownload(movieData: MovieData, position: Int)

        fun updateMovieData(movieData: MovieData, position: Int)

}