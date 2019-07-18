package infinum.academy2019.shows_danijel_pecek.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_login.*

const val WARNING = "Please enter a valid email address"

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val usernameInput = usernameEditText.text.toString().trim()
                val passwordInput = passwordEditText.text.toString().trim()

                logInButton.isEnabled = usernameInput.isNotEmpty() && passwordInput.isNotEmpty() && passwordInput.length > 7 && emailValid(
                    usernameInput
                )
            }
        }
        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)


        logInButton.setOnClickListener {
            startActivity(
                WelcomeActivity.newInstance(
                    this,
                    usernameEditText.text.toString().trim()
                )
            )
            finish()
        }
    }

    fun emailValid(email: String): Boolean {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameInputLayout.error = WARNING
            return false
        }
        usernameInputLayout.error = null
        return true
    }
}
