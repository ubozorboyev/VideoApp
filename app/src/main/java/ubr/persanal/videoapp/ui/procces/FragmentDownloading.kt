package ubr.persanal.videoapp.ui.procces

import android.content.Context
import android.net.ConnectivityManager
import android.net.TrafficStats
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ubr.persanal.videoapp.ui.adapter.DownloadingAdapter
import ubr.persanal.videoapp.data.MovieData
import ubr.persanal.videoapp.databinding.FragmentDownloadingBinding
import ubr.persanal.videoapp.dialog.UrlAddDialog
import ubr.persanal.videoapp.util.VideoDownloadingManager
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class FragmentDownloading : Fragment(), DownloadListener {

    private val TAG = "FragmentDownloading"

    private lateinit var binding: FragmentDownloadingBinding
    private val adapter = DownloadingAdapter(this)
    private val viewModel by viewModels<DownloadingViewModel>()
    private val workManager by  lazy {  WorkManager.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDownloadingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
        observeData()
    }

    private fun initViews(){

        binding.recyclerView.adapter = adapter
        binding.fabButton.setOnClickListener {
            showDialog()
        }
    }

    private fun observeData(){

        viewModel.getMoviesDownloading().observe(viewLifecycleOwner){ list ->

            if (list.isEmpty()) viewModel.loadData()

            Log.d(TAG, "observeData: list $list")
            for (movie in list){
                if (movie.isProcess) {
                    Log.d(TAG, "observeData: data $movie")
                    movie.isProcess = false
                    movie.isPause = true
                }
            }

            if (adapter.itemCount == 0)  adapter.setData(list)
        }

        viewModel.movieData.observe(viewLifecycleOwner){
            adapter.addItem(it)
        }
    }

    private fun showDialog(){
        val dialog = UrlAddDialog(requireContext())
        dialog.show()
        dialog.setUrlAddListener {
            viewModel.addMovie(MovieData("", it, false, 0f))
        }
    }

    override fun startDownload(movieData: MovieData, position:Int) {

        if (movieData.filePath.isEmpty())
            movieData.filePath = requireContext().filesDir.absolutePath + "/" + movieData.name

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val data = Data.Builder()
            .putString("FILE_PATH", movieData.filePath)
            .putString("MOVIE_URL", movieData.uri)

        val workRequest = OneTimeWorkRequestBuilder<VideoDownloadingManager>()
            .setInputData(data.build())
            .setConstraints(constraints)
            .build()

        workManager.enqueue(workRequest)
        movieData.workID = workRequest.id
        movieData.isProcess = true
        movieData.isPause = false

        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(viewLifecycleOwner, { workInfo ->

            movieData.fileSize = workInfo.progress.getFloat("FileSize", movieData.fileSize)

            val value = workInfo.progress.getFloat("Progress", movieData.process)
            adapter.setProgress(value, position)

            when(workInfo.state){
                WorkInfo.State.FAILED ->{
                    val message = workInfo.outputData.getString("FAILURE")
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    pauseDownload(movieData, position)
                }
                WorkInfo.State.SUCCEEDED ->{
                    adapter.setProgress(100f, position)
                }
                else -> {}
            }
        })
    }

    override fun pauseDownload(movieData: MovieData, position: Int) {
        movieData.workID?.let {
            workManager.cancelWorkById(it)
            movieData.isProcess = false
            movieData.isPause = true
            adapter.notifyItemChanged(position, Unit)
        }
    }

    override fun stopDownload(movieData: MovieData, position: Int) {
        movieData.workID?.let {
            workManager.cancelWorkById(it)
            movieData.isProcess = false
            movieData.isPause = false
            File(movieData.filePath).delete()
            movieData.filePath = ""
            movieData.process = 0f
            adapter.notifyItemChanged(position)
        }
    }

   override fun updateMovieData(movieData: MovieData, position: Int){

       viewModel.updateMovie(movieData)
       binding.recyclerView.post {
           adapter.notifyItemRemoved(position)
       }
   }

    override fun onStop() {
        viewModel.updateList(adapter.getDataList())
        workManager.cancelAllWork()
        super.onStop()
    }
}