package ru.nickb.kotlininst.screens.common

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnFailureListener
import ru.nickb.kotlininst.common.firebase.FirebaseAuthManager
import ru.nickb.kotlininst.data.FirebaseUsersRepository
import ru.nickb.kotlininst.data.firebase.FirebaseFeedPostsRepository
import ru.nickb.kotlininst.screens.LoginViewModel
import ru.nickb.kotlininst.screens.addfriends.AddFriendViewModel
import ru.nickb.kotlininst.screens.editprofile.EditProfileViewModel
import ru.nickb.kotlininst.screens.home.HomeViewModel
import ru.nickb.kotlininst.screens.profilesettings.ProfileSettingsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val app: Application,
    private val commonViewModel: CommonViewModel,
    private val onFailureListener: OnFailureListener
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val usersRepo by lazy { FirebaseUsersRepository() }
        val feedPostsRepo by lazy { FirebaseFeedPostsRepository() }
        val authManager by lazy { FirebaseAuthManager() }

        if (modelClass.isAssignableFrom(AddFriendViewModel::class.java)) {
            return AddFriendViewModel(onFailureListener, usersRepo, feedPostsRepo) as T
        } else if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(onFailureListener, usersRepo) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(onFailureListener, feedPostsRepo) as T
        } else if (modelClass.isAssignableFrom(ProfileSettingsViewModel::class.java)) {
            return ProfileSettingsViewModel(authManager) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authManager, app, commonViewModel, onFailureListener) as T
            } else {
                error("Unknown view model class $modelClass")
            }
    }
}