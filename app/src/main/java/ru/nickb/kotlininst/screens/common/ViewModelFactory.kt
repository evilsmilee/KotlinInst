package ru.nickb.kotlininst.screens.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnFailureListener
import ru.nickb.kotlininst.screens.InstagramApp
import ru.nickb.kotlininst.screens.addfriends.AddFriendViewModel
import ru.nickb.kotlininst.screens.comments.CommentsViewModel
import ru.nickb.kotlininst.screens.editprofile.EditProfileViewModel
import ru.nickb.kotlininst.screens.home.HomeViewModel
import ru.nickb.kotlininst.screens.login.LoginViewModel
import ru.nickb.kotlininst.screens.notifications.NotificationsViewModel
import ru.nickb.kotlininst.screens.profile.ProfileViewModel
import ru.nickb.kotlininst.screens.profilesettings.ProfileSettingsViewModel
import ru.nickb.kotlininst.screens.register.RegisterViewModel
import ru.nickb.kotlininst.screens.search.SearchViewModel
import ru.nickb.kotlininst.screens.share.ShareViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val app: InstagramApp,
    private val commonViewModel: CommonViewModel,
    private val onFailureListener: OnFailureListener
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val usersRepo = app.usersRepo
        val feedPostsRepo = app.feedPostsRepo
        val authManager = app.authManager
        val notificationsRepo = app.notificationsRepo
        val searchRepo = app.searchRepo
        if (modelClass.isAssignableFrom(AddFriendViewModel::class.java)) {
            return AddFriendViewModel(onFailureListener, usersRepo, feedPostsRepo) as T
        } else if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(onFailureListener, usersRepo) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(onFailureListener, feedPostsRepo) as T
        } else if (modelClass.isAssignableFrom(ProfileSettingsViewModel::class.java)) {
            return ProfileSettingsViewModel(authManager, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                authManager,
                app,
                commonViewModel,
                onFailureListener
            ) as T
        } else if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(usersRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(
                commonViewModel,
                app,
                usersRepo,
                onFailureListener
            ) as T
        } else if (modelClass.isAssignableFrom(ShareViewModel::class.java)) {
            return ShareViewModel(
                usersRepo,
                feedPostsRepo,
                onFailureListener
            ) as T
        } else if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
             return CommentsViewModel(feedPostsRepo, onFailureListener, usersRepo) as T
        } else if (modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
              return NotificationsViewModel(notificationsRepo, onFailureListener) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(searchRepo, onFailureListener) as T
        } else
         {
            error("Unknown view model class $modelClass")
        }
    }
}