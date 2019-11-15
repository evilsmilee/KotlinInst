package ru.nickb.kotlininst.activities.editprofile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_edit_profile.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.activities.ViewModelFactory
import ru.nickb.kotlininst.activities.loadUserPhoto
import ru.nickb.kotlininst.activities.showToast
import ru.nickb.kotlininst.activities.toStringOrNull
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.utils.CameraHelper
import ru.nickb.kotlininst.views.PasswordDialog

class EditProfileActivity : AppCompatActivity(), PasswordDialog.Listener {


    private lateinit var mUser: User
    private lateinit var mPendingUser: User
    private lateinit var mCamera: CameraHelper
    private lateinit var mViewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        mCamera = CameraHelper(this)

        close_image.setOnClickListener { finish() }
        save_image.setOnClickListener { updateProfile() }
        change_photo_text.setOnClickListener { mCamera.takeCameraPicture() }


        mViewModel = ViewModelProviders.of(this, ViewModelFactory()).get(EditProfileViewModel::class.java)

        mViewModel.user.observe(this, Observer {
            it?.let {
                mUser = it
                name_input.setText(mUser.name)
                username_input.setText(mUser.name)
                website_input.setText(mUser.website)
                bio_input.setText(mUser.bio)
                email_input.setText(mUser.email)
                phone_input.setText(mUser.phone?.toString())
                profile_edit_image.loadUserPhoto(photoUrl = mUser.photo)
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mCamera.REQUEST_CODE && resultCode == RESULT_OK) {

            mViewModel.uploadAndSetUserPhoto(mCamera.imageUri!!).addOnFailureListener {
                showToast(it.message)
            }

        }
    }


    private fun updateProfile() {
        //get user from input
        mPendingUser = readInputs()
        val error = validate(mPendingUser)
        if (error == null) {
            if (mPendingUser.email == mUser.email) {
                updateUser(mPendingUser)
            } else {
                PasswordDialog().show(supportFragmentManager, "password_dialog")

            }
        } else {
            showToast(error)
        }
    }

    private fun readInputs(): User {

        return User(
            email = email_input.text.toString(),
            name = name_input.text.toString(),
            username = username_input.text.toString(),
            website = website_input.text.toStringOrNull(),
            bio = bio_input.text.toStringOrNull(),
            phone = phone_input.text.toString().toLongOrNull()
        )
    }


    override fun onPasswordConfirm(password: String) {
        if (password.isNotEmpty()) {
            mViewModel.updateEmail(
                currentEmail = mUser.email,
                newEmail = mPendingUser.email,
                password = password
            )
                .addOnFailureListener { showToast(it.message) }
                .addOnSuccessListener { updateUser(mPendingUser) }
        } else {
            showToast(getString(R.string.enter_your_password))
        }
    }

    private fun updateUser(user: User) {
        mViewModel.updateUserProfile(currentUser = mUser, newUser = user)
            .addOnFailureListener {
                showToast(it.message)
            }
            .addOnSuccessListener {
                showToast(getString(R.string.profile_saved))
                finish()
            }
    }

    private fun validate(user: User): String? =
        when {
            user.name.isEmpty() -> getString(R.string.enter_name)
            user.username.isEmpty() -> getString(R.string.enter_username)
            user.email.isEmpty() -> getString(R.string.enter_email)
            else -> null
        }

}

