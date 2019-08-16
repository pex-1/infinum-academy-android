package infinum.academy2019.shows_danijel_pecek.ui.welcome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.ui.shows.ShowsActivity
import kotlinx.android.synthetic.main.activity_welcome.*





class WelcomeActivity : AppCompatActivity() {
    var cancelNavigation = true

    companion object {
        const val USERNAME = "USERNAME"

        fun newInstance(context: Context, username: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(USERNAME, Utils.getUserName(username))
            return intent
        }
    }

    override fun onPause() {
        super.onPause()
        cancelNavigation = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        cancelNavigation = true

        val name = intent.getStringExtra(USERNAME)

        welcomeTextView.text = "Welcome $name"

        val sharedPreferences =  getSharedPreferences(Constants.PREFERENCE_NAME,Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor

        editor = sharedPreferences.edit()
        editor.putString(Constants.USER_NAME, name)
        editor.apply()

        Handler().postDelayed({
            if(cancelNavigation){
                startActivity(ShowsActivity.newInstance(this))
                finish()
            }
        }, 3000)
    }
}