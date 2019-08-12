package infinum.academy2019.shows_danijel_pecek.ui.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.ui.FragmentContainerActivity
import kotlinx.android.synthetic.main.activity_welcome.*





class WelcomeActivity : AppCompatActivity() {
    var cancelNavigation = true

    companion object {
        const val USERNAME = "USERNAME"

        fun newInstance(context: Context, username: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(USERNAME, username.split("@")[0])
            return intent
        }
    }

    override fun onPause() {
        super.onPause()
        cancelNavigation = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        cancelNavigation = true

        welcomeTextView.text = "Welcome ${intent.getStringExtra(USERNAME)}"


        Handler().postDelayed({
            if(cancelNavigation){
                startActivity(FragmentContainerActivity.newInstance(this))
                finish()
            }
        }, 3000)
    }
}