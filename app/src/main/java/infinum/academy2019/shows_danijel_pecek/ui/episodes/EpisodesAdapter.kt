package infinum.academy2019.shows_danijel_pecek.ui.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.data.model.Episode
import kotlinx.android.synthetic.main.item_episode.view.*

class EpisodesAdapter(private val clickListener: OnEpisodeClicked): RecyclerView.Adapter<EpisodesAdapter.EpisodesViewHolder>() {

    private var episodes = listOf<Episode>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        return EpisodesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode, parent, false))
    }

    override fun getItemCount(): Int = episodes.size

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val episode = episodes[position]

        with(holder.itemView){
            episodeNameTextView.text = episode.title
            seasonTextView.text = Utils.setSeasonString(episode.season, episode.episode)
            rootView.setOnClickListener{clickListener.onClick(episode)}
        }
    }

    fun setData(episodes: List<Episode>){
        this.episodes = episodes
        notifyDataSetChanged()
    }

    inner class EpisodesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    interface OnEpisodeClicked{
        fun onClick(episode: Episode)
    }
}