package ru.nickb.kotlininst.screens.common


import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders


abstract class BaseActivity : AppCompatActivity() {

    lateinit var commonViewModel: CommonViewModel

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)
        commonViewModel.errorMessage.observe(this, Observer { it.let {
                showToast(it)
            }
        })
    }

    inline fun <reified T : ViewModel> initViewModel(): T = ViewModelProviders.of(this,
        ViewModelFactory(commonViewModel)
    ).get(T::class.java)

}