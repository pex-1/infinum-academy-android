package infinum.academy2019.shows_danijel_pecek.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.model.Show
import kotlinx.android.synthetic.main.item_show.view.*

class ShowsAdapter(private val listShows: ArrayList<Show>, private val clickListener: OnShowClicked): RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        return ShowsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_show,parent,false))
    }

    override fun getItemCount(): Int = listShows.size

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        val show = listShows[position]

        with(holder.itemView){
            showTitleTextView.text = show.name
            showYearTextView.text = show.date
            showImageView.setImageResource(show.image)
            rootView.setOnClickListener{clickListener.onClick(show)}
        }
    }


    inner class ShowsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    interface OnShowClicked{
        fun onClick(show: Show)
    }
}