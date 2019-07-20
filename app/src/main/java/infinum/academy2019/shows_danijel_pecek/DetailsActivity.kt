package infinum.academy2019.shows_danijel_pecek

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso
import infinum.academy2019.shows_danijel_pecek.model.Episode
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    companion object {

        const val EPISODE = "EPISODE"

        fun newInstance(context: Context, episode: Episode): Intent {

            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(EPISODE, episode)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val episode = intent.getParcelableExtra<Episode>(EPISODE)

        picassoUpload(episode.image, detailsEpisodeImageView)
        detailTitleTextView.text = episode.title
        detailDescriptionTextView.text = episode.description
    }



    private fun picassoUpload(imageUri: Uri?, imageView: ImageView){
        Picasso.with(this).load(imageUri).resize(0, 500)
            .into(imageView)
    }
}
