package ru.nickb.kotlininst.screens.profilesettings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_profile_settings.*
import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.data.firebase.common.FirebaseHelper
import ru.nickb.kotlininst.data.firebase.common.auth
import ru.nickb.kotlininst.screens.common.BaseActivity
import ru.nickb.kotlininst.screens.common.setupAuthGuard

class ProfileSettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        setupAuthGuard {
            val mViewModel = initViewModel<ProfileSettingsViewModel>()
            sign_out_text.setOnClickListener { mViewModel.signOut()}
            back_image.setOnClickListener { finish() }
        }

    }
}
