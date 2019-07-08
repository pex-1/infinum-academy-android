package infinum.academy2019.homework2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern

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
            //using a local variable "userInput"?
            startActivity(WelcomeActivity.newInstance(this, usernameEditText.text.toString().trim()))

            //Toast.makeText(this,"button working",Toast.LENGTH_SHORT).show()
        }
    }

    fun emailValid(email: String): Boolean{
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            warningTextView.text = WARNING
            warningTextView.visibility = View.VISIBLE
            return false
        }
        warningTextView.visibility = View.GONE
        return true
    }
}
