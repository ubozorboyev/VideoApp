package ubr.persanal.videoapp.ui.video

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SeekBar
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ubr.persanal.videoapp.R
import ubr.persanal.videoapp.databinding.FragmentPlayVideoBinding

class PlayVideoFragment : Fragment() {

    private val TAG = "PlayVideoFragment"

    private lateinit var binding: FragmentPlayVideoBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        //   requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val uri = requireArguments().getString("MOVIE_PATH")
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        binding.videoView.setVideoURI(uri?.toUri())

        initViews()

        binding.videoView.setOnPreparedListener {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    while (binding.videoView.currentPosition < binding.videoView.duration) {
                        if (binding.videoView.isPlaying)
                            binding.seekBar.progress = binding.videoView.currentPosition
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }



        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                p0?.progress?.let { binding.videoView.seekTo(it) }
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                p0?.progress?.let { binding.videoView.seekTo(it) }
            }

        })

    }

    private fun initViews() {


        binding.videoView.setOnCompletionListener {
            binding.videoView.stopPlayback()
            binding.playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle)
            binding.seekBar.progress = 0
        }
        
        binding.replay.setOnClickListener {
            binding.videoView.seekTo(binding.videoView.currentPosition - 30000)
            binding.seekBar.progress = binding.videoView.currentPosition
        }
        binding.forward.setOnClickListener {
            binding.videoView.seekTo(binding.videoView.currentPosition + 30000)
            binding.seekBar.progress = binding.videoView.currentPosition
        }


        binding.playPauseButton.setOnClickListener {

            if (binding.videoView.isPlaying) {
                binding.videoView.pause()
                binding.playPauseButton.setImageResource(R.drawable.ic_baseline_play_circle)
            } else {
                binding.videoView.start()
                binding.playPauseButton.setImageResource(R.drawable.ic_baseline_pause)
            }
            binding.seekBar.max = binding.videoView.duration

        }
    }


}