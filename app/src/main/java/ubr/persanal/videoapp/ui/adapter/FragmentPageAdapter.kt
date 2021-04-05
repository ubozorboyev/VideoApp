package ubr.persanal.videoapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ubr.persanal.videoapp.ui.finished.FragmentDownload
import ubr.persanal.videoapp.ui.procces.FragmentDownloading

class FragmentPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {

        return if (position == 0) FragmentDownloading()
        else FragmentDownload()
    }
}