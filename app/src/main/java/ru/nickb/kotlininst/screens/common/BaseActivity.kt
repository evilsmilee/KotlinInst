package ru.nickb.kotlininst.screens.common


import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import ru.nickb.kotlininst.screens.InstagramApp
import ru.nickb.kotlininst.screens.login.LoginActivity


abstract class BaseActivity : AppCompatActivity() {

   protected lateinit var commonViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        commonViewModel.errorMessage.observe(this, Observer { it.let {
                showToast(it)
            }
        })
    }

    protected inline fun <reified T : BaseViewModel> initViewModel(): T =
        ViewModelProviders.of(this, ViewModelFactory(
            application as InstagramApp,
            commonViewModel,
            commonViewModel)
    ).get(T::class.java)

    fun goToLogin() {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
    }

}