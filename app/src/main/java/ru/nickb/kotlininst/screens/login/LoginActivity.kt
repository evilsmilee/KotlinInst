package ru.nickb.kotlininst.screens.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.screens.register.RegisterActivity
import ru.nickb.kotlininst.screens.common.BaseActivity
import ru.nickb.kotlininst.screens.common.CommonViewModel
import ru.nickb.kotlininst.screens.common.coordinateBtnAndInputs
import ru.nickb.kotlininst.screens.common.setupAuthGuard
import ru.nickb.kotlininst.screens.home.HomeActivity

class LoginActivity : BaseActivity(),   View.OnClickListener, KeyboardVisibilityEventListener {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var mViewModel: LoginViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        KeyboardVisibilityEvent.setEventListener(this, this)
        coordinateBtnAndInputs(login_btn, email_input, password_input)
        login_btn.setOnClickListener(this)
        create_account_text.setOnClickListener(this)
        /*setupAuthGuard {*/
            mViewModel = initViewModel()
            mViewModel.goToHomeScreen.observe(this, Observer {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
             })
            mViewModel.goToRegisterScreen.observe(this, Observer {
                startActivity(Intent(this, RegisterActivity::class.java))
            })
            mAuth = FirebaseAuth.getInstance()
        /*}*/
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.login_btn -> mViewModel.onLoginClick(email = email_input.text.toString(), password = password_input.text.toString())
            R.id.create_account_text -> mViewModel.onRegisterClick()
        }

    }

    override fun onVisibilityChanged(isKeyboardOpen: Boolean) {
        if(isKeyboardOpen) {
            create_account_text.visibility = View.GONE
        } else {
            create_account_text.visibility = View.VISIBLE
        }
    }
}
