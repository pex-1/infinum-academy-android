package infinum.academy2019.shows_danijel_pecek.ui.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.Show
import kotlinx.android.synthetic.main.item_show.view.*

class ShowsAdapter(private val clickListener: OnShowClicked): RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    private var shows = listOf<Show>()
    val index = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        return ShowsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_show,parent,false))
    }

    override fun getItemCount(): Int = shows.size

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        val show = shows[position]

        with(holder.itemView){
            showTitleTextView.text = show.name
            showYearTextView.text = show.date
            showImageView.setImageResource(show.image)
            rootView.setOnClickListener{clickListener.onClick(show)}
        }
    }

    fun setData(shows: List<Show>){
        this.shows = shows
        notifyDataSetChanged()
    }

    inner class ShowsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    interface OnShowClicked{
        fun onClick(show: Show)
    }
}