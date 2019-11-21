package ru.nickb.kotlininst.screens



import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alexbezhan.instagram.common.SingleLiveEvent
import com.google.android.gms.tasks.OnFailureListener
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.common.AuthManager
import ru.nickb.kotlininst.screens.common.CommonViewModel

class LoginViewModel(
    private val authManager: AuthManager,
    private val app: Application,
    private val commonViewModel: CommonViewModel,
    private val onFailureListener: OnFailureListener
) : ViewModel() {
    private val _goToHomeScreen = SingleLiveEvent<Unit>()
    val goToHomeScreen: LiveData<Unit> = _goToHomeScreen
    private val _goToRegisterScreen = SingleLiveEvent<Unit>()
    val goToRegisterScreen: LiveData<Unit> = _goToRegisterScreen
    fun onLoginClick(email: String, password: String) {
        if (validate(email, password)) {
            authManager.signIn(email, password).addOnCompleteListener {
                _goToHomeScreen.value = Unit
            }.addOnFailureListener(onFailureListener)
        } else {
            commonViewModel.setErrorMessage(app.getString(R.string.please_enter_pwd))

        }
    }

    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty()

    fun onRegisterClick() {
        _goToRegisterScreen.call()
    }
}