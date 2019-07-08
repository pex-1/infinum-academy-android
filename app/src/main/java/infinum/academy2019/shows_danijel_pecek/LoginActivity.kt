package infinum.academy2019.shows_danijel_pecek

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object{
        const val WARNING = "Please enter a valid email address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val textWatcher: TextWatcher = object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val usernameInput = usernameEditText.text.toString().trim()
                val passwordInput = passwordEditText.text.toString().trim()

                if(usernameInput.isNotEmpty() && passwordInput.isNotEmpty() && passwordInput.length > 2 && emailValid(usernameInput)){
                    logInButton.isEnabled = true
                    logInButton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
                }else{
                    logInButton.isEnabled = false
                    logInButton.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.button_disabled))
                }
            }
        }
        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)


        logInButton.setOnClickListener {
            startActivity(WelcomeActivity.newInstance(this, usernameEditText.text.toString().trim()))
            finish()
        }
    }

    fun emailValid(email: String): Boolean{
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            usernameInputLayout.error = WARNING
            return false
        }
        usernameInputLayout.error = null
        return true
    }

}
