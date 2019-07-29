package infinum.academy2019.shows_danijel_pecek.ui.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.user.User
import infinum.academy2019.shows_danijel_pecek.ui.FragmentContainerActivity
import infinum.academy2019.shows_danijel_pecek.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.logInButton
import kotlinx.android.synthetic.main.activity_login.passwordEditText
import kotlinx.android.synthetic.main.activity_login.usernameEditText
import kotlinx.android.synthetic.main.activity_login.usernameInputLayout
import kotlinx.android.synthetic.main.activity_register.*



class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel

    companion object{
        fun newInstance(context: Context)= Intent(context, RegisterActivity::class.java)
        const val WARNING = "Please enter a valid email address"
        const val PASSWORDS_DONT_MATCH = "Passwords don't match!"
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setSupportActionBar(toolbarRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        viewModel.liveData.observe(this, Observer {
            registerProgressBar.visibility = View.GONE
            if(it == Constants.USER_REGISTRATION_SUCCESSFUL){
                Toast.makeText(applicationContext, "Registration Successful!", Toast.LENGTH_SHORT).show()
                startActivity(WelcomeActivity.newInstance(this, usernameEditTextRegister.text.toString().trim()))
                finish()
            }else{
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            }
        })

        usernameEditTextRegister.setText(viewModel.email)
        passwordEditTextRegister.setText(viewModel.password)
        repeatPasswordEditTextRegister.setText(viewModel.passwordRepeat)


        val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                with(viewModel){
                    email = usernameEditTextRegister.text.toString().trim()
                    password = passwordEditTextRegister.text.toString().trim()
                    passwordRepeat = repeatPasswordEditTextRegister.text.toString().trim()

                    registerButton.isEnabled = email.isNotEmpty() && password.isNotEmpty() && password.length > 7 && emailValid(email) && passwordsMatch(password, passwordRepeat)
                }

            }
        }
        usernameEditTextRegister.addTextChangedListener(textWatcher)
        passwordEditTextRegister.addTextChangedListener(textWatcher)
        repeatPasswordEditTextRegister.addTextChangedListener(textWatcher)


        registerButton.setOnClickListener {
            registerProgressBar.visibility = View.VISIBLE
            viewModel.registerUser(User(usernameEditTextRegister.text.toString(), passwordEditTextRegister.text.toString()))
        }
    }

    fun passwordsMatch(password: String, passwordRepeat: String): Boolean{
        return if(password == passwordRepeat){
            passwordRepeatLayout.error = null
            true
        }else{
            passwordRepeatLayout.error = PASSWORDS_DONT_MATCH
            false
        }
    }
    fun emailValid(email: String): Boolean {
        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameInputLayout.error = WARNING
            false
        }else{
            usernameInputLayout.error = null
            true
        }

    }
}
