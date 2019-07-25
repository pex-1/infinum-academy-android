package infinum.academy2019.shows_danijel_pecek.ui.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.ui.shows.ShowsActivity
import kotlinx.android.synthetic.main.activity_welcome.*





class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "USERNAME"

        fun newInstance(context: Context, username: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(USERNAME, username)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        welcomeTextView.text = "Welcome ${intent.getStringExtra(USERNAME)}"


        Handler().postDelayed({
            startActivity(ShowsActivity.newInstance(this))
        }, 3000)
    }
}