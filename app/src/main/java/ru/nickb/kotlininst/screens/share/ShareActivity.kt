package ru.nickb.kotlininst.screens.share

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_share.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.data.firebase.common.FirebaseHelper
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.screens.common.BaseActivity
import ru.nickb.kotlininst.screens.common.CameraHelper
import ru.nickb.kotlininst.screens.common.loadImage
import ru.nickb.kotlininst.screens.common.setupAuthGuard
import ru.nickb.kotlininst.screens.profile.ProfileActivity

class ShareActivity : BaseActivity() {

    private lateinit var mCamera: CameraHelper
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mUser: User
    private lateinit var mViewModel: ShareViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        setupAuthGuard {
            mViewModel = initViewModel()

            mFirebase = FirebaseHelper(this)

            mCamera = CameraHelper(this)
            mCamera.takeCameraPicture()

            back_image.setOnClickListener { finish() }
            share_text.setOnClickListener { share() }

            mViewModel.user.observe(this, Observer{it?.let {
                mUser = it
            }})
            mViewModel.shareCompletedEvent.observe(this, Observer { it?.let {
                finish()
            } })
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCamera.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                post_image.loadImage(mCamera.imageUri?.toString())
            } else {
                finish()
            }
        }
    }
    private fun share() {
        mViewModel.share(mUser, mCamera.imageUri,caption_input.text.toString())
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
    }
}

