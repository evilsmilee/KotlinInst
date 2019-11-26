package ru.nickb.kotlininst.screens.notifications

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.OnFailureListener
import ru.nickb.kotlininst.data.NotificationsRepository
import ru.nickb.kotlininst.models.Notification
import ru.nickb.kotlininst.screens.common.BaseViewModel

class NotificationsViewModel(private val notificationsRepo: NotificationsRepository,
                             onFailureListener: OnFailureListener)
    : BaseViewModel(onFailureListener) {

    lateinit var notifications: LiveData<List<Notification>>
    private lateinit var uid: String

    fun init(uid: String) {
        if (!this::uid.isInitialized) {
            this.uid = uid
            notifications = notificationsRepo.getNotifications(uid)
        }
    }

    fun setNotificationRead(notifications: List<Notification>) {
        val ids = notifications.filter { !it.read }.map { it.id }
        if (ids.isNotEmpty()) {
            notificationsRepo.setNotificationsRead(uid, ids, true)
                .addOnFailureListener(onFailureListener)
        }
    }


}
