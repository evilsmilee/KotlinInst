package ru.nickb.kotlininst.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.bottom_navigation_view.*
import ru.nickb.kotlininst.R


abstract class BaseActivity(private val navNumber: Int) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    fun setupBottomNavigation() {
        bottom_navigation_view.setTextVisibility(false)
        bottom_navigation_view.enableItemShiftingMode(false)
        bottom_navigation_view.enableShiftingMode(false)
        bottom_navigation_view.enableAnimation(false)
        bottom_navigation_view.setIconSize(29f, 29f)
        for(i in 0 until  bottom_navigation_view.menu.size()) {
            bottom_navigation_view.setIconTintList(i, null)
        }
        bottom_navigation_view.setOnNavigationItemSelectedListener {
            val nextActivity =
                when(it.itemId) {
                    R.id.nav_item_home -> HomeActivity::class.java
                    R.id.nav_item_search -> SearchActivity::class.java
                    R.id.nav_item_share -> ShareActivity::class.java
                    R.id.nav_item_likes -> LikesActivity::class.java
                    R.id.nav_item_profile -> ProfileActivity::class.java
                    else -> {
                        Log.e("unkown", "error $it")
                        null
                    }
                }
            if(nextActivity != null) {
                val intent = Intent(this, nextActivity)
                intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(intent)
                overridePendingTransition(0,0)
                true
            } else {
                false
            }

        }

    }

    override fun onResume() {
        super.onResume()
        if(bottom_navigation_view != null) {
            bottom_navigation_view.menu.getItem(navNumber).isChecked = true
        }

    }

}