package infinum.academy2019.shows_danijel_pecek.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import infinum.academy2019.shows_danijel_pecek.Constants
import infinum.academy2019.shows_danijel_pecek.R
import infinum.academy2019.shows_danijel_pecek.data.model.user.User
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository
import infinum.academy2019.shows_danijel_pecek.ui.FragmentContainerActivity
import infinum.academy2019.shows_danijel_pecek.ui.register.RegisterActivity
import infinum.academy2019.shows_danijel_pecek.ui.shared.LoginRegisterViewModel
import infinum.academy2019.shows_danijel_pecek.ui.shared.onTextChange
import infinum.academy2019.shows_danijel_pecek.ui.welcome.WelcomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.usernameInputLayout


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginRegisterViewModel

    companion object{
        fun newInstance(context: Context) = Intent(context, LoginActivity::class.java)

        const val WARNING = "Please enter a valid email address"
        const val SKIP_LOGIN = "SKIP_LOGIN"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        var sharedPreferenceEditor: SharedPreferences.Editor

        if (sharedPreferences.getBoolean(SKIP_LOGIN, false)) {
            startActivity(FragmentContainerActivity.newInstance(this))
            finish()
        }

        viewModel = ViewModelProviders.of(this).get(LoginRegisterViewModel::class.java)
        viewModel.liveData.observe(this, Observer {
            loginProgressBar.visibility = View.GONE
            if(it){
                Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_SHORT).show()
                if (rememberMeCheckBox.isChecked) {
                    sharedPreferenceEditor = sharedPreferences.edit()
                    sharedPreferenceEditor.putBoolean(SKIP_LOGIN, true)
                    sharedPreferenceEditor.apply()
                    startActivity(WelcomeActivity.newInstance(this, usernameEditText.text.toString().trim()))
                    finishAffinity()
                }
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
            Repository.loginUser(User(usernameEditText.text.toString(), passwordEditText.text.toString()))
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


    fun emailValid(email: String): Boolean {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameInputLayout.error = WARNING
            return false
        }
        usernameInputLayout.error = null
        return true
    }
}