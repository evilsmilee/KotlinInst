package ru.nickb.kotlininst.activities

import android.os.Bundle
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.views.setupBottomNavigation

class LikesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation(3)
    }

}
