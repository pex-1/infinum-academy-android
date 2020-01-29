package infinum.academy2019.shows_danijel_pecek.ui.shows

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.ShowModel
import kotlinx.android.synthetic.main.item_show_linear.view.*

class ShowsAdapter(private val clickListener: OnShowClicked, private val gridLayout: Boolean): RecyclerView.Adapter<ShowsAdapter.ShowsViewHolder>() {

    private var shows = listOf<ShowModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        return ShowsViewHolder(LayoutInflater.from(parent.context).inflate(if(gridLayout) R.layout.item_show_grid else R.layout.item_show_linear,parent,false))
    }

    override fun getItemCount(): Int = shows.size

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        val show = shows[position]
        val height = if(gridLayout) 225 else 90
        val width = if(gridLayout) 160 else 64

        with(holder.itemView){
            showTitleTextView.text = show.title
            if(!gridLayout){
                showLikesTextView.text = show.likesCount.toString()
            }
            Picasso.get().load(show.getImage())
                .resize(width, height)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(showImageView)
            rootView.setOnClickListener{
                clickListener.onClick(show)
            }
        }


    }

    fun setData(shows: List<ShowModel>){
        this.shows = shows
        notifyDataSetChanged()
    }


    inner class ShowsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    interface OnShowClicked{
        fun onClick(show: ShowModel)
    }
}