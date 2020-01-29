package infinum.academy2019.shows_danijel_pecek.ui.episodes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.ui.comments.CommentsActivity
import kotlinx.android.synthetic.main.activity_episode.*
import kotlinx.android.synthetic.main.content_episode_details.*

class EpisodeActivity : AppCompatActivity() {

    companion object{
        const val EPISODE_ID = "EPISODE_ID"
        fun newInstance(context: Context, episodeId: String): Intent {
            val intent = Intent(context, EpisodeActivity::class.java)
            intent.putExtra(EPISODE_ID, episodeId)
            return intent
        }
    }

    private lateinit var viewModel: EpisodeViewModel

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episode)
        episodeProgressBar.visibility = View.VISIBLE

        setSupportActionBar(toolbarSingleEpisode)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val episodeId = intent.getStringExtra(EPISODE_ID)


        viewModel = ViewModelProviders.of(this).get(EpisodeViewModel::class.java)
        viewModel.getEpisode(episodeId)

        viewModel.resetApiError()
        viewModel.apiError()
        viewModel.apiErrorLiveData?.observe(this, Observer {
            if(it != null){
                if(Utils.networkAvailable(this)){
                    createSnackbar(it)
                }else{
                    createSnackbar(Constants.NO_INTERNET)
                }

            }
            episodeProgressBar.visibility = View.GONE
        })



        viewModel.episodeLiveData?.observe(this, Observer { episode ->
            if (episode != null) {
                episodeProgressBar.visibility = View.GONE
                episodeTitleTextView.text = episode.title
                episodeDetailsSeasonText.text = Utils.setSeasonString(episode.seasonNumber, episode.episodeNumber)
                episodeDescriptionTextView.text = episode.description

                Picasso.get().load(Constants.IMAGE_BASE_URL + episode.imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(episodeImageView)
            }
        })


        commentsTextView.setOnClickListener {
            startActivity(CommentsActivity.newInstance(this, episodeId))
        }


    }

    private fun createSnackbar(message: String) {
        val snackbar = Snackbar.make(showDetailsCoordinatorLayoutSnackbar, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color))
        snackbar.show()
    }
}