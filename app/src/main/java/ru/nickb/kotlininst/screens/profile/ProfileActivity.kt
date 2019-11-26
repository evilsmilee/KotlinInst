package ru.nickb.kotlininst.screens.profile

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_profile.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.screens.addfriends.AddFriendsActivity
import ru.nickb.kotlininst.screens.editprofile.EditProfileActivity
import ru.nickb.kotlininst.screens.common.*
import ru.nickb.kotlininst.screens.profilesettings.ProfileSettingsActivity


class ProfileActivity : BaseActivity() {


    private lateinit var mAdapter: ImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setupBottomNavigation(4)

        edit_profile_button.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        settings_image.setOnClickListener {
            val intent = Intent(this, ProfileSettingsActivity::class.java)
            startActivity(intent)
        }
        add_friends_image.setOnClickListener {
            val intent = Intent(this, AddFriendsActivity::class.java)
            startActivity(intent)
        }

        images_recycler.layoutManager = GridLayoutManager(this, 3)
        mAdapter = ImagesAdapter()
        images_recycler.adapter = mAdapter

        setupAuthGuard {uid ->
            val viewModel = initViewModel<ProfileViewModel>()
            viewModel.init(uid)
            viewModel.user.observe(this, Observer {it?.let {
                profile_image.loadUserPhoto(it.photo)
                username_text.text = it.username
                followers_count_text.text = it.followers.size.toString()
                following_count_text.text = it.follows.size.toString()

            } })
            viewModel.images.observe(this, Observer { it?.let {images ->
                mAdapter.updateImages(images)
                posts_count_text.text = images.size.toString()
            } })
        }
    }

}


