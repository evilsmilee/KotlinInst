package ru.nickb.kotlininst.screens.addfriends

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add_friends.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.screens.common.BaseActivity
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.screens.common.setupAuthGuard

class AddFriendsActivity : BaseActivity(), FriendsAdapter.Listener {
    private lateinit var mViewModel: AddFriendViewModel
    private lateinit var mUser: User
    private lateinit var mAdapter: FriendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)


        mAdapter = FriendsAdapter(this)

        setupAuthGuard {
            mViewModel = initViewModel()

            back_image.setOnClickListener { finish() }

            add_friends_recycler.adapter = mAdapter
            add_friends_recycler.layoutManager = LinearLayoutManager(this)

            mViewModel.usersAndFriends.observe(this, Observer {
                it?.let { (user, otherUsers) ->
                    mUser = user

                    mAdapter.update(otherUsers, mUser.follows)
                }
            })
        }
    }


    override fun follow(uid: String) {
        setFollow(uid, true) {
            mAdapter.followed(uid)
        }
    }

    override fun unfollow(uid: String) {
        setFollow(uid, false) {
            mAdapter.unfollowed(uid)
        }
    }

    private fun setFollow(uid: String, follow: Boolean, onSuccess: () -> Unit) {
        mViewModel.setFollow(mUser.uid, uid, follow)
            .addOnSuccessListener { onSuccess() }
    }
}