package infinum.academy2019.shows_danijel_pecek.ui.shows

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.Show
import infinum.academy2019.shows_danijel_pecek.ui.episodes.EpisodesActivity
import kotlinx.android.synthetic.main.activity_shows.*


class ShowsActivity : AppCompatActivity(), ShowsAdapter.OnShowClicked {

    private lateinit var viewModel: ShowsViewModel
    private lateinit var adapter: ShowsAdapter

    companion object {
        fun newInstance(context: Context): Intent = Intent(context, ShowsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        adapter = ShowsAdapter(this)
        showsRecyclerView.layoutManager = LinearLayoutManager(this)
        showsRecyclerView.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
        viewModel.showsLiveData.observe(this, Observer { shows ->
            if (shows != null) {
                adapter.setData(shows)
            }
        })
    }

    override fun onClick(show: Show) {
        viewModel.setId(show.id)
        startActivity(EpisodesActivity.newInstance(this))
    }
}
