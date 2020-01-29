package infinum.academy2019.shows_danijel_pecek.ui.splash

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.*
import androidx.core.view.doOnLayout
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.ui.login.LoginActivity
import infinum.academy2019.shows_danijel_pecek.ui.shows.ShowsActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    var cancelNavigation = false

    override fun onPause() {
        super.onPause()
        cancelNavigation = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        cancelNavigation = false

        val sharedPreferences =  getSharedPreferences(Constants.PREFERENCE_NAME,Context.MODE_PRIVATE)


            iconAnimation.doOnLayout {
                iconAnimation.animate()
                    .translationY(0f)
                    .setInterpolator(BounceInterpolator())
                    .setDuration(1200)
                    .start()

                textAnimation.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setInterpolator(OvershootInterpolator())
                    .setStartDelay(1300)
                    .setDuration(500)
                    .withEndAction {
                        Handler().postDelayed({
                            if(!cancelNavigation){
                                if (sharedPreferences.getBoolean(Constants.SKIP_LOGIN, false)) {
                                    startActivity(ShowsActivity.newInstance(this))
                                    finish()
                                } else {
                                    startActivity(LoginActivity.newInstance(this))
                                }
                            }

                        }, 2000)
                    }
                    .start()
            }

    }

    override fun onBackPressed() {
        super.onBackPressed()

    }


}

