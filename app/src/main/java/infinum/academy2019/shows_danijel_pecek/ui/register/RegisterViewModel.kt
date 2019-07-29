package infinum.academy2019.shows_danijel_pecek.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import infinum.academy2019.shows_danijel_pecek.data.model.user.User
import infinum.academy2019.shows_danijel_pecek.data.repository.Repository

class RegisterViewModel : ViewModel(), Observer<String> {

    var email = ""
    var password = ""
    var passwordRepeat = ""

    var loginEmail = ""
    var loginPassword = ""

    private val registrationSuccessful = MutableLiveData<String>()

    init {
        Repository.registrationSuccessful().observeForever(this)
    }

    val liveData: LiveData<String>
        get() {
            return registrationSuccessful
        }

    override fun onChanged(registration: String?) {
        registrationSuccessful.value = registration
    }

    fun registerUser(user: User){
        Repository.registerUser(user)
    }
}