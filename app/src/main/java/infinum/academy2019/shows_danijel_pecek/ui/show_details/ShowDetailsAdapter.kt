package infinum.academy2019.shows_danijel_pecek.ui.show_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.data.model.EpisodeModel
import kotlinx.android.synthetic.main.item_episode.view.*

class ShowDetailsAdapter(private val clickListener: OnEpisodeClicked): RecyclerView.Adapter<ShowDetailsAdapter.EpisodesViewHolder>() {

    private var episodes = listOf<EpisodeModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        return EpisodesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false))
    }

    override fun getItemCount(): Int = episodes.size

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val episode = episodes[position]

        with(holder.itemView){
            episodeNameTextView.text = episode.title
            seasonTextView.text = Utils.setSeasonString(episode.seasonNumber, episode.episodeNumber)
            rootView.setOnClickListener{clickListener.onClick(episode)}
        }
    }

    fun setData(episodes: List<EpisodeModel>){
        this.episodes = episodes
        notifyDataSetChanged()
    }

    inner class EpisodesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    interface OnEpisodeClicked{
        fun onClick(episode: EpisodeModel)
    }
}