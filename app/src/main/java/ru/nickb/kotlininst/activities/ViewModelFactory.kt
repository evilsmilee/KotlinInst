package ru.nickb.kotlininst.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.nickb.kotlininst.activities.addfriends.AddFriendViewModel
import ru.nickb.kotlininst.activities.addfriends.FirebaseAddFriendsRepository
import ru.nickb.kotlininst.activities.editprofile.EditProfileViewModel
import ru.nickb.kotlininst.activities.editprofile.FirebaseEditProfileRepository

@Suppress("UNCHECKED_CAST")
class ViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFriendViewModel::class.java)) {
            return AddFriendViewModel(FirebaseAddFriendsRepository()) as T
        }
        else if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(FirebaseEditProfileRepository()) as T
        }
        else {
            error("Unknown view model class $modelClass")
        }
    }
}