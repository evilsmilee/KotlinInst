package ru.nickb.kotlininst.screens.notifications

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_notifications.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.screens.common.BaseActivity
import ru.nickb.kotlininst.screens.common.setupAuthGuard
import ru.nickb.kotlininst.screens.common.setupBottomNavigation


class NotificationsActivity : BaseActivity() {

    private lateinit var mAdapter: NotificationsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)


        setupAuthGuard {uid ->
            setupBottomNavigation(uid,3)
            mAdapter = NotificationsAdapter()
            notifications_recycler.layoutManager = LinearLayoutManager(this)
            notifications_recycler.adapter = mAdapter

            val viewModel = initViewModel<NotificationsViewModel>()
            viewModel.init(uid)
            viewModel.notifications.observe(this, Observer {it?.let {
                    mAdapter.updateNotifications(it)
                    viewModel.setNotificationRead(it)
            }

            })
        }
    }

}
