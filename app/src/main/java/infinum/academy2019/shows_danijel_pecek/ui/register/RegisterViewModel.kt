package infinum.academy2019.shows_danijel_pecek.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.user.User
import infinum.academy2019.shows_danijel_pecek.data.model.user.UserLoginResponse
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository

class RegisterViewModel : ViewModel(), Observer<Boolean> {

    var apiErrorLiveData: LiveData<String>? = null

    fun apiError(){
        apiErrorLiveData = Repository.apiErrorLiveData()
    }

    fun resetApiError(){
        Repository.resetErrorLiveData()
    }

    override fun onChanged(registration: Boolean?) {
        registrationSuccessful.value = registration
    }

    override fun onCleared() {
        Repository.registrationResponse().removeObserver(this)
    }

    var email = ""
    var password = ""
    var passwordRepeat = ""


    var loginLiveData : LiveData<UserLoginResponse>? = null



    init {
        Repository.registrationResponse().observeForever(this)
    }

    fun removeObserver(){
        onCleared()
    }

    fun loginUser(user: User){
        Repository.loginUser(user)
        loginLiveData = Repository.loginResponse()
    }

    private val registrationSuccessful = MutableLiveData<Boolean>()

    val liveDataRegister: LiveData<Boolean>
        get() {
            return registrationSuccessful
        }

    fun registerUser(user: User){
        Repository.registerUser(user)
    }
}