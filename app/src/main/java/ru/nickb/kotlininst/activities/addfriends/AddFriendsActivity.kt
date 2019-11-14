package ru.nickb.kotlininst.activities.addfriends

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_add_friends.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.models.User

class AddFriendsActivity : AppCompatActivity(),
    FriendsAdapter.Listener {


    private lateinit var mViewModel: AddFriendViewModel
    private lateinit var mUser: User
    private lateinit var mUsers: List<User>
    private lateinit var mAdapter: FriendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)


        mAdapter = FriendsAdapter(this)

        mViewModel = ViewModelProviders.of(this, AddFriendsViewModelFactory())
            .get(AddFriendViewModel::class.java)

        back_image.setOnClickListener { finish() }

        add_friends_recycler.adapter = mAdapter
        add_friends_recycler.layoutManager = LinearLayoutManager(this)

        mViewModel.usersAndFriends.observe(this, Observer {
            it?.let { (user, otherUsers) ->
                mUser = user
                mUsers = otherUsers

                mAdapter.update(mUsers, mUser.follows)
            }
        })
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
            .addOnFailureListener { it.message!! }
    }
}