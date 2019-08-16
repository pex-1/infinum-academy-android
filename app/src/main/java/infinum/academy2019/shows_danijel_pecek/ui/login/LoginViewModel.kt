package infinum.academy2019.shows_danijel_pecek.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.user.User
import infinum.academy2019.shows_danijel_pecek.data.model.user.UserLoginResponse
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository

class LoginViewModel : ViewModel(), Observer<UserLoginResponse> {

    var apiErrorLiveData: LiveData<String>? = null

    fun apiError(){
        apiErrorLiveData = Repository.apiErrorLiveData()
    }

    fun resetApiError(){
        Repository.resetErrorLiveData()
    }


    override fun onChanged(t: UserLoginResponse?) {
        loginLiveData.value = t
    }

    init {
        Repository.loginResponse().observeForever(this)
    }

    var loginEmail = ""
    var loginPassword = ""

    val liveData: LiveData<UserLoginResponse>

    get(){
        return loginLiveData
    }

    private val loginLiveData = MutableLiveData<UserLoginResponse>()


    fun loginUser(user: User){
        Repository.loginUser(user)
    }

    override fun onCleared() {
        Repository.loginResponse().removeObserver(this)
    }



}