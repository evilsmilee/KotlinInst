package ru.nickb.kotlininst.screens

import android.os.Bundle
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.screens.common.BaseActivity
import ru.nickb.kotlininst.screens.common.setupBottomNavigation

class LikesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation(3)
    }

}
