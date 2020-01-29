package infinum.academy2019.shows_danijel_pecek.ui.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.Utils
import infinum.academy2019.shows_danijel_pecek.data.model.user.User
import infinum.academy2019.shows_danijel_pecek.ui.shared.onTextChange
import infinum.academy2019.shows_danijel_pecek.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_login.usernameInputLayout
import kotlinx.android.synthetic.main.activity_register.*


private const val passwordLength = 5

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
        viewModel.resetApiError()
        viewModel.apiError()
        viewModel.apiErrorLiveData?.observe(this, Observer {
            if(it != null){
                if(Utils.networkAvailable(this)){
                    createSnackbar(it)
                }else{
                    createSnackbar(Constants.NO_INTERNET)
                }

            }
            registerProgressBar.visibility = View.GONE
        })

        viewModel.liveDataRegister.observe(this, Observer {
            registerProgressBar.visibility = View.GONE
            if(it){
                viewModel.loginUser(User(usernameEditTextRegister.text.toString(), passwordEditTextRegister.text.toString()))
                loginUser()

            }else{
                Toast.makeText(applicationContext, "Registration not successful!", Toast.LENGTH_SHORT).show()
            }
        })


        usernameEditTextRegister.setText(viewModel.email)
        passwordEditTextRegister.setText(viewModel.password)
        repeatPasswordEditTextRegister.setText(viewModel.passwordRepeat)


        usernameEditTextRegister.onTextChange { textChangeValidation() }
        passwordEditTextRegister.onTextChange { textChangeValidation() }
        repeatPasswordEditTextRegister.onTextChange { textChangeValidation() }


        registerButton.setOnClickListener {
            registerProgressBar.visibility = View.VISIBLE
            viewModel.registerUser(User(usernameEditTextRegister.text.toString(), passwordEditTextRegister.text.toString()))
        }
    }

    private fun loginUser(){
        viewModel.loginLiveData?.observe(this, Observer {
            if(it != null && it.userLogin.token.isNotEmpty()){
                viewModel.removeObserver()
                val sharedPreferences =  getSharedPreferences(Constants.PREFERENCE_NAME,Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor
                editor = sharedPreferences.edit()
                editor.putString(Constants.TOKEN, it.userLogin.token)
                editor.apply()

                startActivity(WelcomeActivity.newInstance(this, usernameEditTextRegister.text.toString().trim()))

                finishAffinity()
            }
        })
    }
    private fun createSnackbar(message: String) {
        val snackbar = Snackbar.make(registerCoordinatorLayoutSnackbar, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color))
        snackbar.show()
    }

    private fun textChangeValidation() {
        with(viewModel) {
            email = usernameEditTextRegister.text.toString().trim()
            password = passwordEditTextRegister.text.toString().trim()
            passwordRepeat = repeatPasswordEditTextRegister.text.toString().trim()

            registerButton.isEnabled =
                email.isNotEmpty() && password.isNotEmpty() && password.length > passwordLength && emailValid(email) && passwordsMatch(
                    password,
                    passwordRepeat
                )
        }
    }

    private fun passwordsMatch(password: String, passwordRepeat: String): Boolean{
        return if(password == passwordRepeat){
            passwordRepeatLayout.error = null
            true
        }else{
            passwordRepeatLayout.error = PASSWORDS_DONT_MATCH
            false
        }
    }
    private fun emailValid(email: String): Boolean {
        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameInputLayout.error = WARNING
            false
        }else{
            usernameInputLayout.error = null
            true
        }

    }
}