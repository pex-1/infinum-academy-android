package infinum.academy2019.shows_danijel_pecek.episodes

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.addEpisode.AddEpisodeActivity
import infinum.academy2019.shows_danijel_pecek.model.Show
import kotlinx.android.synthetic.main.activity_episodes.*

const val EPISODES_ACTIVITY_REQUEST_CODE = 2

lateinit var show: Show
class EpisodesActivity : AppCompatActivity() {

    companion object {
        const val SHOW = "SHOW"

        fun newInstance(context: Context, show: String): Intent {
            val intent = Intent(context, EpisodesActivity::class.java)
            intent.putExtra(SHOW, show)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_episodes)

        setSupportActionBar(toolbarEpisodes as Toolbar?)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        show =
            Utils.deserialize(intent.getStringExtra(SHOW)!!)
        (toolbarEpisodes as Toolbar?)!!.title = show.name

        showDescriptionTextView.text = show.description

        addEpisodesClickableTextView.setOnClickListener {
            startActivityForResult(AddEpisodeActivity.newInstance(this@EpisodesActivity),
                EPISODES_ACTIVITY_REQUEST_CODE
            )
        }

        episodesListView.setOnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, "Description: ${show.episodeList[position].description}",Toast.LENGTH_SHORT).show()
        }

        updateEpisodes()

        val mFab = findViewById<FloatingActionButton>(R.id.episodesFab)
        mFab.setOnClickListener {
            startActivityForResult(AddEpisodeActivity.newInstance(this),
                EPISODES_ACTIVITY_REQUEST_CODE
            )
        }

        returnResult(show)
    }

    private fun updateEpisodes() {
        if (show.episodeList.size < 1) {
            noShowsLinearLayout.visibility = View.VISIBLE
        } else {
            noShowsLinearLayout.visibility = View.GONE
            episodesListView.visibility = View.VISIBLE


            val list: MutableList<String> = ArrayList()
            var num = 1
            for(i in show.episodeList){
                list.add("$num. ${i.title}")
                num++
            }
            episodesListView.adapter = ArrayAdapter(this,
                R.layout.item_list_view, list)
        }
    }

    private fun returnResult(show: Show) {
        val resultIntent = Intent()
        resultIntent.putExtra(Constants.SHOW_WITH_EPISODES, Utils.serialize(show))
        setResult(Activity.RESULT_OK, resultIntent)
    }

    override fun onSupportNavigateUp(): Boolean {
        returnResult(show)
        onBackPressed()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == EPISODES_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null){
            show.episodeList.add(
                Utils.deserializeEpisode(
                    data.getStringExtra(Constants.EPISODES_LIST)!!
                )
            )
        }

    }

    override fun onResume() {
        super.onResume()
        updateEpisodes()
    }
}
