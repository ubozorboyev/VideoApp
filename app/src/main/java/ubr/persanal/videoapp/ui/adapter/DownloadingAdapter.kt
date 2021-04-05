package ubr.persanal.videoapp.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ubr.persanal.videoapp.R
import ubr.persanal.videoapp.data.MovieData
import ubr.persanal.videoapp.databinding.ItemDownloadingBinding
import ubr.persanal.videoapp.ui.procces.DownloadListener


class DownloadingAdapter(val downloadListener: DownloadListener) :
    RecyclerView.Adapter<DownloadingAdapter.ViewHolder>() {

    private val dataList = arrayListOf<MovieData>()

    inner class ViewHolder(private val itemBinding: ItemDownloadingBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(data: MovieData) {

            itemBinding.playPauseButton.isClickable = true
            data.name = data.uri.takeLastWhile { it != '/' }
            itemBinding.filmName.text = data.name
            itemBinding.progressValue.text = StringBuilder(data.process.toString() + "%")
            itemBinding.progressBar.progress = (data.process * 10).toInt()
            itemBinding.fileSize.text = if (data.fileSize != 0f) "${data.fileSize} MB" else ""

            if (data.process.toInt() == 100) {

                itemBinding.progressLayout.visibility = View.GONE
                itemBinding.playPauseButton.visibility = View.GONE
                itemBinding.cancelButton.visibility = View.GONE
                data.isDownload = true
                dataList.removeAt(adapterPosition)
                downloadListener.updateMovieData(movieData = data, adapterPosition)
            }

            itemBinding.progressLayout.apply {
                visibility = if (data.isProcess || data.isPause) View.VISIBLE
                else View.GONE
            }

            itemBinding.playPauseButton.apply {
                setText(
                    when {
                        data.isPause -> R.string.resume
                        data.isProcess -> R.string.pause
                        else -> R.string.download
                    }
                )

                setBackgroundResource(
                    when {
                        data.isPause -> R.drawable.resume_button_bg
                        else -> R.drawable.outline_text
                    }
                )

                setTextColor(
                    if (data.isPause) Color.WHITE
                    else itemView.context.resources.getColor(R.color.sky_700)
                )
            }

            itemBinding.cancelButton.apply {
                visibility = if (data.isPause || data.isProcess)
                    View.VISIBLE
                else View.GONE
            }

            itemBinding.cancelButton.setOnClickListener {
                downloadListener.stopDownload(data, adapterPosition)
            }

            itemBinding.playPauseButton.setOnClickListener {
                itemBinding.playPauseButton.isClickable = false

                if (!data.isProcess) {
                    downloadListener.startDownload(data, adapterPosition)
                } else {
                    downloadListener.pauseDownload(data, adapterPosition)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemDownloadingBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    fun setProgress(value: Float, position: Int) {

        dataList[position].process = (value * 10).toInt().toFloat() / 10
        dataList[position].fileSize = (dataList[position].fileSize * 100).toInt().toFloat()/100
        notifyItemChanged(position, Unit)
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addItem(data: MovieData) {
        dataList.add(data)
        notifyItemInserted(dataList.size - 1)
    }

    fun setData(data: List<MovieData>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun getDataList() = dataList

}