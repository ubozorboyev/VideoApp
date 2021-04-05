package ubr.persanal.videoapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ubr.persanal.videoapp.data.MovieData
import ubr.persanal.videoapp.databinding.ItemMovieBinding

class PlayMovieAdapter : RecyclerView.Adapter<PlayMovieAdapter.ViewHolder>() {

    private var dataList = listOf<MovieData>()
    private var listener: ((MovieData) -> Unit)? = null


    inner class ViewHolder(private val itemBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(data: MovieData) {
            itemBinding.movieName.text = data.name.dropLast(4)
            itemBinding.playButton.setOnClickListener {
                listener?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemMovieBinding.inflate(inflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(ls: List<MovieData>) {
        dataList = ls
        notifyDataSetChanged()
    }

    fun setPlayButtonClickListener(ls: (MovieData) -> Unit) {
        listener = ls
    }

}
