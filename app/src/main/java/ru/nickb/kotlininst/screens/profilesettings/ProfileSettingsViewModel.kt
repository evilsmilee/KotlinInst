package ru.nickb.kotlininst.screens.profilesettings

import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import ru.nickb.kotlininst.common.AuthManager
import ru.nickb.kotlininst.screens.common.BaseViewModel

class ProfileSettingsViewModel(private val authManager: AuthManager, onFailureListener: OnFailureListener): BaseViewModel(onFailureListener),
    AuthManager by authManager


