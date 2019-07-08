package infinum.academy2019.homework2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    companion object {
        const val USERNAME = "USERNAME"

        fun newInstance(context: Context, username: String): Intent {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.putExtra(USERNAME, username)
            Log.e("username", username)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        welcomeTextView.append(intent.getStringExtra(USERNAME))
    }
}
