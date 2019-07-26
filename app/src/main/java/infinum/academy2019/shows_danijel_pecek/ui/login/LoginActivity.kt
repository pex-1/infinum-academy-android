package infinum.academy2019.shows_danijel_pecek.ui.login

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.ui.FragmentContainerActivity
import infinum.academy2019.shows_danijel_pecek.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_login.*

const val WARNING = "Please enter a valid email address"
const val SKIP_LOGIN = "SKIP_LOGIN"

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        var sharedPreferenceEditor: SharedPreferences.Editor

        if (sharedPreferences.getBoolean(SKIP_LOGIN, false)) {
            startActivity(FragmentContainerActivity.newInstance(this))
            finish()
        }


        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val usernameInput = usernameEditText.text.toString().trim()
                val passwordInput = passwordEditText.text.toString().trim()

                logInButton.isEnabled = usernameInput.isNotEmpty() && passwordInput.isNotEmpty() && passwordInput.length > 7 && emailValid(usernameInput)
            }
        }
        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)


        logInButton.setOnClickListener {
            if (rememberMeCheckBox.isChecked) {
                sharedPreferenceEditor = sharedPreferences.edit()
                sharedPreferenceEditor.putBoolean(SKIP_LOGIN, true)
                sharedPreferenceEditor.apply()
            }

            startActivity(WelcomeActivity.newInstance(this, usernameEditText.text.toString().trim()))
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