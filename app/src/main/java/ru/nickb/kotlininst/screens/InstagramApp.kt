package ru.nickb.kotlininst.screens

import android.app.Application
import ru.nickb.kotlininst.common.firebase.FirebaseAuthManager
import ru.nickb.kotlininst.data.FirebaseUsersRepository
import ru.nickb.kotlininst.data.firebase.FirebaseFeedPostsRepository
import ru.nickb.kotlininst.data.firebase.FirebaseNotificationsRepository
import ru.nickb.kotlininst.data.firebase.common.FirebaseSearchRepository
import ru.nickb.kotlininst.screens.notifications.NotificationsCreator
import ru.nickb.kotlininst.screens.search.SearchPostCreator

class InstagramApp: Application() {
    val usersRepo by lazy { FirebaseUsersRepository() }
    val feedPostsRepo by lazy { FirebaseFeedPostsRepository() }
    val notificationsRepo by lazy { FirebaseNotificationsRepository() }
    val searchRepo by lazy {FirebaseSearchRepository()}
    val authManager by lazy { FirebaseAuthManager() }


    override fun onCreate() {
        super.onCreate()
        NotificationsCreator(notificationsRepo, usersRepo, feedPostsRepo)
        SearchPostCreator(searchRepo)
    }

}