package ubr.persanal.videoapp.ui.finished

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import ubr.persanal.videoapp.R
import ubr.persanal.videoapp.databinding.FragmentDownloadBinding
import ubr.persanal.videoapp.ui.adapter.PlayMovieAdapter
import ubr.persanal.videoapp.ui.video.PlayVideoFragment

class FragmentDownload : Fragment() {

    private  val TAG = "FragmentDownload"
    private lateinit var binding: FragmentDownloadBinding
    private val viewModel by viewModels<DownloadedViewModel>()
    private val adapter = PlayMovieAdapter()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDownloadBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.recyclerView.adapter = adapter

        viewModel.movies.observe(viewLifecycleOwner){
            adapter.setData(it)
        }

        adapter.setPlayButtonClickListener {
            val bundle = Bundle()
            bundle.putString("MOVIE_PATH", it.filePath)
            findNavController().navigate(R.id.action_homeFragment_to_playVideoFragment, bundle)
        }

    }

}

