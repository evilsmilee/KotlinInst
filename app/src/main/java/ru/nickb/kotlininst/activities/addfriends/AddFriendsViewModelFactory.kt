package ru.nickb.kotlininst.activities.addfriends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class AddFriendsViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddFriendViewModel(FirebaseAddFriendsRepository()) as T
    }
}