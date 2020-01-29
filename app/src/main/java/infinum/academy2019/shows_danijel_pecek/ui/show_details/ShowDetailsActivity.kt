package infinum.academy2019.shows_danijel_pecek.ui.show_details

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.data.database.ShowLikes
import infinum.academy2019.shows_danijel_pecek.data.model.EpisodeModel
import infinum.academy2019.shows_danijel_pecek.ui.add_episode.AddEpisodeActivity
import infinum.academy2019.shows_danijel_pecek.ui.episodes.EpisodeActivity
import kotlinx.android.synthetic.main.activity_show_details.*
import kotlinx.android.synthetic.main.content_show_details.*

class ShowDetailsActivity : AppCompatActivity(), ShowDetailsAdapter.OnEpisodeClicked {

    companion object {
        const val SHOW_ID = "SHOW_ID"
        fun newInstance(context: Context, showId: String): Intent {
            val intent = Intent(context, ShowDetailsActivity::class.java)
            intent.putExtra(SHOW_ID, showId)
            return intent
        }
    }


    private lateinit var viewModel: ShowDetailsViewModel
    private lateinit var adapter: ShowDetailsAdapter

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private lateinit var showId: String

    override fun onResume() {
        if (showId.isNotEmpty()) {
            viewModel.getEpisode(showId)
            showDetailsProgressBar.visibility = View.VISIBLE
        }
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)


        showId = intent.getStringExtra(SHOW_ID)

        var likePressed = false
        var dislikePressed = false


        setSupportActionBar(toolbarEpisodes)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        adapter = ShowDetailsAdapter(this)


        viewModel = ViewModelProviders.of(this).get(ShowDetailsViewModel::class.java)
        viewModel.setProgressBar()
        viewModel.resetApiError()
        viewModel.apiError()
        showDetailsProgressBar.visibility = View.VISIBLE
        viewModel.getEpisode(showId)
        viewModel.getShowDetails(showId)

        viewModel.getLikes(showId)
        viewModel.showLikesLiveData?.observe(this, Observer {
            if (it != null) {
                if (it.dislike) {
                    highlightDislike()
                } else if (it.like) {
                    highlightLike()
                }

                if (likePressed || dislikePressed) {
                    var likes = it
                    if (!it.dislike && dislikePressed) {
                        dislikePressed = false
                        likes.dislike = true
                        likes.like = false
                        viewModel.updateLike(likes)

                        showDetailsLikesCount.text = (showDetailsLikesCount.text.toString().toInt() - 1).toString()
                        highlightDislike()
                        viewModel.dislikeShow(showId)
                        viewModel.dislikeShowLiveData?.observe(this, Observer {
                        })
                    } else if (!it.like && likePressed) {
                        likePressed = false
                        likes.dislike = false
                        likes.like = true
                        viewModel.updateLike(likes)

                        showDetailsLikesCount.text = (showDetailsLikesCount.text.toString().toInt() + 1).toString()
                        highlightLike()
                        viewModel.likeShow(showId)
                        viewModel.likeShowLiveData?.observe(this, Observer {
                        })
                    }
                }
            }


        })

        viewModel.apiErrorLiveData?.observe(this, Observer {
            if(it != null){
                if(Utils.networkAvailable(this)){
                    createSnackbar(it)
                }else{
                    createSnackbar(Constants.NO_INTERNET)
                }
                showDetailsProgressBar.visibility = View.GONE
                noShowsLinearLayoutShowDetails.visibility = View.VISIBLE
            }
        })

        viewModel.progressBarLiveData?.observe(this, Observer { progressBar ->
            if (progressBar) {
                showDetailsProgressBar.visibility = View.GONE
            } else {
                showDetailsProgressBar.visibility = View.VISIBLE
            }
        })

        viewModel.episodesLiveData?.observe(this, Observer { episodes ->
            if (episodes != null) {

                adapter.setData(episodes)


                if (episodes.isNotEmpty()) {
                    noShowsLinearLayoutShowDetails.visibility = View.GONE
                    episodesRecyclerView.visibility = View.VISIBLE

                    episodesRecyclerView.layoutManager = LinearLayoutManager(this)
                    episodesRecyclerView.adapter = adapter
                }
            }else{
                noShowsLinearLayoutShowDetails.visibility = View.VISIBLE
            }
        })

        viewModel.detailsLiveData?.observe(this, Observer { details ->
            if (details != null) {
                episodeTitleTextView.text = details.title
                episodeDescriptionTextView.text = details.description
                showDetailsLikesCount.text = details.likesCount.toString()

                viewModel.insertLike(ShowLikes(showId, details.likesCount.toString()))

                Picasso.get().load(Constants.IMAGE_BASE_URL + details.imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(showDetailsImageViewToolbar)
            }
        })


        showDetailsDislike.setOnClickListener {
            dislikePressed = true
            viewModel.getLikes(showId)
        }

        showDetailsLike.setOnClickListener {
            likePressed = true
            viewModel.getLikes(showId)

        }
        addEpisodesClickableTextView.setOnClickListener {
            startActivity(AddEpisodeActivity.newInstance(this, showId))
        }


        episodesFab.setOnClickListener {
            startActivity(AddEpisodeActivity.newInstance(this, showId))
        }

    }

    private fun createSnackbar(message: String) {
        val snackbar = Snackbar.make(showDetailsCoordinatorLayoutSnackbar, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color))
        snackbar.show()
    }

    private fun highlightLike() {
        showDetailsLike.setImageResource(R.drawable.show_like_pressed)
        showDetailsDislike.setImageResource(R.drawable.show_dislike)
    }

    private fun highlightDislike() {
        showDetailsLike.setImageResource(R.drawable.show_like)
        showDetailsDislike.setImageResource(R.drawable.show_dislike_pressed)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetLiveData()
    }


    override fun onClick(episode: EpisodeModel) {
        startActivity(EpisodeActivity.newInstance(this, episode.episodeId))
    }

}
