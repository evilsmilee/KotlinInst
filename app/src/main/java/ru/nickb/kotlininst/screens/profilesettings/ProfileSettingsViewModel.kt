package ru.nickb.kotlininst.screens.profilesettings

import androidx.lifecycle.ViewModel
import ru.nickb.kotlininst.common.AuthManager

class ProfileSettingsViewModel(private val authManager: AuthManager): ViewModel(),
    AuthManager by authManager


