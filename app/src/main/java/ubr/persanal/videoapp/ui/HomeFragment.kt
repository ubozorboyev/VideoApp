package ubr.persanal.videoapp.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ubr.persanal.videoapp.databinding.FragmentHomeBinding
import ubr.persanal.videoapp.ui.adapter.FragmentPageAdapter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = FragmentHomeBinding.inflate(inflater, container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.viewpager.adapter = FragmentPageAdapter(requireActivity().supportFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = if (position == 0) "DOWNLOADING" else "DOWNLOADED"
        }.attach()

    }



}