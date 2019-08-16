package infinum.academy2019.shows_danijel_pecek.ui.shows

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.data.model.ShowModel
import infinum.academy2019.shows_danijel_pecek.ui.login.LoginActivity
import infinum.academy2019.shows_danijel_pecek.ui.show_details.ShowDetailsActivity
import kotlinx.android.synthetic.main.activity_shows.*

class ShowsActivity : AppCompatActivity(), ShowsAdapter.OnShowClicked {

    private lateinit var viewModel: ShowsViewModel
    private lateinit var adapter: ShowsAdapter


    companion object {
        fun newInstance(context: Context) = Intent(context, ShowsActivity::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(Constants.LOG_OUT_DIALOG_TITLE)
            alertDialog.setMessage(Constants.LOG_OUT_DIALOG_MESSAGE)
            alertDialog.setPositiveButton("Log out") { _, _ ->
                val sharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)
                var editor: SharedPreferences.Editor
                editor = sharedPreferences.edit()
                editor.putBoolean(Constants.SKIP_LOGIN, false)
                editor.apply()
                startActivity(LoginActivity.newInstance(this))
                finish()

            }

            alertDialog.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            alertDialog.create()
            alertDialog.show()
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shows)

        setSupportActionBar(toolbarShows)

        var showList: List<ShowModel> = listOf()

        viewModel = ViewModelProviders.of(this).get(ShowsViewModel::class.java)
        viewModel.resetApiError()
        viewModel.apiError()
        viewModel.apiErrorLiveData?.observe(this, Observer {
            if(it != null){
                if(Utils.networkAvailable(this)){
                    createSnackbar(it)
                }else{
                    createSnackbar(Constants.NO_INTERNET)
                }
                showsProgressBar.visibility = View.GONE

            }
        })

        val sharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE)

        val token = sharedPreferences.getString(Constants.TOKEN, "")
        token?.let { viewModel.setToken(it) }

        setAdapter(viewModel.gridLayout)
        setFabToggle(viewModel.gridLayout)

        viewModel.showsLiveData.observe(this, Observer { shows ->
            if (shows != null) {
                showsProgressBar.visibility = View.GONE
                noShowsLinearLayoutShows.visibility = View.GONE
                showsRecyclerView.visibility = View.VISIBLE
                adapter.setData(shows)
                showList = shows
            }else{
                noShowsLinearLayoutShows.visibility = View.VISIBLE
            }
        })

        viewModel.getShows()
        showsProgressBar.visibility = View.VISIBLE

        showsFabToggle.setOnClickListener {
            viewModel.gridLayout = !viewModel.gridLayout
            setAdapter(viewModel.gridLayout)
            setFabToggle(viewModel.gridLayout)
            adapter.setData(showList)
        }
    }

    private fun setFabToggle(gridLayout: Boolean) {
        if (!gridLayout) {
            showsFabToggle.setImageResource(R.drawable.ic_gridview_white)
        } else {
            showsFabToggle.setImageResource(R.drawable.ic_filter_list)
        }
    }

    private fun createSnackbar(message: String) {
        val snackbar = Snackbar.make(showsCoordinatorLayoutSnackbar, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color))
        snackbar.show()
    }

    private fun setAdapter(gridLayout: Boolean) {
        adapter = ShowsAdapter(this, gridLayout)
        showsRecyclerView.layoutManager =
            if (gridLayout) GridLayoutManager(this, 2) else LinearLayoutManager(this)
        showsRecyclerView.adapter = adapter
    }


    override fun onClick(show: ShowModel) {
        startActivity(ShowDetailsActivity.newInstance(this, show.showId))

    }
}
