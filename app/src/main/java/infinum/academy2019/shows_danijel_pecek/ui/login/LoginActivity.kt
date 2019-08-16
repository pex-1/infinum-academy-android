package infinum.academy2019.shows_danijel_pecek.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import infinum.academy2019.shows_danijel_pecek.ui.register.RegisterActivity
import infinum.academy2019.shows_danijel_pecek.ui.shared.onTextChange
import infinum.academy2019.shows_danijel_pecek.ui.shows.ShowsActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.usernameInputLayout


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    companion object{
        fun newInstance(context: Context) = Intent(context, LoginActivity::class.java)

        const val WARNING = "Please enter a valid email address"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        

        val sharedPreferences =  getSharedPreferences(Constants.PREFERENCE_NAME,Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor



        if (sharedPreferences.getBoolean(Constants.SKIP_LOGIN, false)) {
            startActivity(ShowsActivity.newInstance(this))
            finish()
        }

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
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
            loginProgressBar.visibility = View.GONE
        })

        viewModel.liveData.observe(this, Observer {
            loginProgressBar.visibility = View.GONE
            if(it != null){
                if (rememberMeCheckBox.isChecked) {
                    editor = sharedPreferences.edit()
                    editor.putBoolean(Constants.SKIP_LOGIN, true)
                    editor.apply()
                }
                editor = sharedPreferences.edit()
                editor.putString(Constants.TOKEN, it.userLogin.token)
                editor.putString(Constants.USER_NAME, Utils.getUserName(usernameEditText.text.toString().trim()))
                editor.apply()


                startActivity(ShowsActivity.newInstance(this))
                finishAffinity()
            }else{
                Toast.makeText(applicationContext, "Login not successful!", Toast.LENGTH_SHORT).show()
            }
        })
        usernameEditText.setText(viewModel.loginEmail)
        passwordEditText.setText(viewModel.loginPassword)


        usernameEditText.onTextChange {
            textChangeValidation()
        }
        passwordEditText.onTextChange {
            textChangeValidation()
        }


        registerTextView.setOnClickListener {
            startActivity(RegisterActivity.newInstance(this))
        }

        logInButton.setOnClickListener {
            loginProgressBar.visibility = View.VISIBLE
            viewModel.loginUser(User(usernameEditText.text.toString(), passwordEditText.text.toString()))
        }
    }

    private fun textChangeValidation() {
        with(viewModel) {
            loginEmail = usernameEditText.text.toString().trim()
            loginPassword = passwordEditText.text.toString().trim()

            logInButton.isEnabled =
                loginEmail.isNotEmpty() && loginPassword.isNotEmpty() && loginPassword.length > 5 && emailValid(
                    loginEmail
                )
        }
    }


    private fun createSnackbar(message: String) {
        val snackbar = Snackbar.make(loginConstraintLayoutSnackbarl, message, Snackbar.LENGTH_LONG)
        snackbar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color))
        snackbar.show()
    }

    private fun emailValid(email: String): Boolean {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameInputLayout.error = WARNING
            return false
        }
        usernameInputLayout.error = null
        return true
    }
}