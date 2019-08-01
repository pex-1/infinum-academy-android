package infinum.academy2019.shows_danijel_pecek.ui.splash

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.*
import androidx.core.view.doOnLayout
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.ui.FragmentContainerActivity
import infinum.academy2019.shows_danijel_pecek.ui.login.LoginActivity
import infinum.academy2019.shows_danijel_pecek.ui.login.LoginActivity.Companion.SKIP_LOGIN
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)



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
                    .start()
            }

        Handler().postDelayed({
            if (sharedPreferences.getBoolean(SKIP_LOGIN, false)) {
                startActivity(FragmentContainerActivity.newInstance(this))
                finish()
            } else {
                startActivity(LoginActivity.newInstance(this))
            }
        }, 3800)
    }


}

