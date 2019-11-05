package ru.nickb.kotlininst.activities

import android.os.Bundle
import ru.nickb.kotlininst.R

class LikesActivity : BaseActivity(3) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()
    }

}
