package ru.nickb.kotlininst.screens.register

import android.app.Application
import androidx.lifecycle.ViewModel
import com.alexbezhan.instagram.common.SingleLiveEvent
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.data.UsersRepository
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.screens.common.CommonViewModel

class RegisterViewModel(
    private val commonViewModel: CommonViewModel,
    private val app: Application,
    private val usersRepo: UsersRepository
) : ViewModel() {
    private var email: String? = null
    private val _goToNamePassScreen = SingleLiveEvent<Unit>()
    private val _goToHomeScreen = SingleLiveEvent<Unit>()
    private val _goBackToEmailScreen = SingleLiveEvent<Unit>()
    val goToNamePassScreen = _goToNamePassScreen
    val goToHomeScreen = _goToHomeScreen
    val goBackToEmailScreen = _goBackToEmailScreen
    fun onEmailEntered(email: String) {
        if (email.isNotEmpty()) {
            this.email = email
            usersRepo.isUserExistsForEmail(email).addOnSuccessListener { exists ->
                if (!exists) {
                    _goToNamePassScreen.call()
                } else {
                    commonViewModel.setErrorMessage(app.getString(R.string.email_already_exists))
                }
            }
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.enter_email))
        }
    }

    fun onRegister(fullName: String, password: String) {
        if(fullName.isNotEmpty() && password.isNotEmpty()) {
            val localEmail = email
            if(localEmail != null) {
                usersRepo.createUser(mkUser(fullName, localEmail), password).addOnSuccessListener {
                    _goToHomeScreen.call()
                }

            } else {
                commonViewModel.setErrorMessage(app.getString(R.string.enter_fullname_or_pwd))
                _goBackToEmailScreen.call()
            }
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.enter_fullname_or_pwd))
        }
    }

    private fun mkUser(fullName: String, email: String): User {
        val username = mkUsername(fullName)
        return User(name = fullName, username = username, email = email)
    }

    private fun mkUsername(fullName: String) = fullName.toLowerCase().replace(" ", ".")

}