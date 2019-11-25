package ru.nickb.kotlininst.screens

import android.app.Application
import ru.nickb.kotlininst.common.firebase.FirebaseAuthManager
import ru.nickb.kotlininst.data.FirebaseUsersRepository
import ru.nickb.kotlininst.data.firebase.FirebaseFeedPostsRepository
import ru.nickb.kotlininst.data.firebase.FirebaseNotificationsRepository
import ru.nickb.kotlininst.screens.notifications.NotificationsCreator

class InstagramApp: Application() {
    val usersRepo by lazy { FirebaseUsersRepository() }
    val feedPostsRepo by lazy { FirebaseFeedPostsRepository() }
    val notificationsRepo by lazy { FirebaseNotificationsRepository() }
    val authManager by lazy { FirebaseAuthManager() }

    override fun onCreate() {
        super.onCreate()
        NotificationsCreator(notificationsRepo, usersRepo, feedPostsRepo)
    }

}