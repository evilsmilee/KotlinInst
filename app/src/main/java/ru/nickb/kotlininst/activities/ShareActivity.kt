package ru.nickb.kotlininst.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_share.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.models.FeedPost
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.utils.CameraHelper
import ru.nickb.kotlininst.utils.FirebaseHelper
import ru.nickb.kotlininst.utils.GlideApp
import ru.nickb.kotlininst.utils.ValueEventListenerAdapter

class ShareActivity : BaseActivity(2) {

    private lateinit var mCamera: CameraHelper
    private lateinit var mFirebase: FirebaseHelper
    private lateinit var mUser: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)
        mFirebase = FirebaseHelper(this)
        mCamera = CameraHelper(this)
        mCamera.takeCameraPicture()
        back_image.setOnClickListener { finish() }
        share_text.setOnClickListener { share() }

        mFirebase.currentUserReference().addValueEventListener(ValueEventListenerAdapter{
           mUser =  it.asUser()!!
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCamera.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                GlideApp.with(this).load(mCamera.imageUri).centerCrop().into(post_image)
            } else {
                finish()
            }
        }
    }

    private fun share() {
        val imageUri = mCamera.imageUri
        if (imageUri != null) {
            val uid = mFirebase.currentUid()!!
            mFirebase.storage.child("users").child(uid).child("images")
                .child(imageUri.lastPathSegment!!).putFile(imageUri).addOnCompleteListener {
                    if (it.isSuccessful) {
                        mFirebase.database.child("images").child(uid).push()
                            .setValue(imageUri.toString())
                        if (it.isSuccessful) {
                            mFirebase.database.child("feed-posts").child(uid)
                                .push().setValue(mkFeedPost(uid, imageUri))
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        startActivity(Intent(this, ProfileActivity::class.java))
                                        finish()
                                    }
                                }
                        } else {
                            showToast(it.exception!!.message!!)
                        }
                    } else {
                        showToast(it.exception!!.message!!)
                    }
                }
        }

    }

    private fun mkFeedPost(uid: String, imageUri: Uri): FeedPost {
        return FeedPost(
            uid = uid,
            username = mUser.username,
            image = imageUri.toString(),
            caption = caption_input.text.toString(),
            photo = mUser.photo
        )
    }


}

