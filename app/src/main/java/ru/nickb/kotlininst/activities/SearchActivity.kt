package ru.nickb.kotlininst.activities

import android.os.Bundle
import ru.nickb.kotlininst.R

class SearchActivity : BaseActivity(1) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigation()
    }

}
