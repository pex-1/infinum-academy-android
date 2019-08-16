package infinum.academy2019.shows_danijel_pecek.ui.comments

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.MessageModel
import kotlinx.android.synthetic.main.activity_comments.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.Utils


class CommentsActivity : AppCompatActivity() {

    companion object {
        const val COMMENTS_ID = "COMMENTS_ID"
        fun newInstance(context: Context, episodeId: String): Intent {
            val intent = Intent(context, CommentsActivity::class.java)
            intent.putExtra(COMMENTS_ID, episodeId)
            return intent
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private lateinit var viewModel: CommentsViewModel
    private lateinit var adapter: CommentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)

        val episodeId = intent.getStringExtra(COMMENTS_ID)


        val sharedPreferences =  getSharedPreferences(Constants.PREFERENCE_NAME,Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(Constants.USER_NAME, "Anonymus")

        adapter = CommentsAdapter()

        setSupportActionBar(toolbarComments)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(CommentsViewModel::class.java)
        viewModel.getComments(episodeId)
        viewModel.resetApiError()
        viewModel.apiError()
        viewModel.apiErrorLiveData?.observe(this, Observer {
            if(it != null){
                if(Utils.networkAvailable(this)){
                    createSnackbar(it)
                }else{
                    createSnackbar(Constants.NO_INTERNET)
                }
                commentsProgressBar.visibility = View.GONE
            }
        })

        commentsProgressBar.visibility = View.VISIBLE
        viewModel.commentsLiveData?.observe(this, Observer {
            commentsProgressBar.visibility = View.GONE
            if (it != null && it.isNotEmpty()) {
                commentsEmptyLayout.visibility = View.GONE
                commentsRecyclerView.visibility = View.VISIBLE
                adapter.setData(it)

                commentsRecyclerView.layoutManager = LinearLayoutManager(this)
                commentsRecyclerView.adapter = adapter
            }
            swipeRefreshComments.isRefreshing = false
        })

        viewModel.commentResponseLiveData?.observe(this, Observer {
            if (it != null) {
                commentsProgressBar.visibility = View.GONE
                viewModel.getComments(episodeId)
            }
        })

        postCommentTextView.setOnClickListener {
            if (commentEditText.text.trim().isNotEmpty()) {
                commentsProgressBar.visibility = View.VISIBLE
                val comment = MessageModel(commentEditText.text.toString(), episodeId)
                viewModel.postComment(comment)
                commentEditText.hideKeyboard()
                commentEditText.setText("")
                viewModel.getComments(episodeId)
            }

        }

        swipeRefreshComments.setOnRefreshListener {
            viewModel.getComments(episodeId)
        }

    }

    private fun createSnackbar(message: String) {
        val snackbar = Snackbar.make(commentsConstraintLayoutSnackbar, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color))
        snackbar.show()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
