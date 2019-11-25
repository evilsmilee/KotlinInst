package ru.nickb.kotlininst.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import ru.nickb.kotlininst.data.firebase.common.database
import ru.nickb.kotlininst.models.Notification

interface NotificationsRepository {
    fun createNotification(uid: String, notification: Notification): Task<Unit>

    fun getNotifications(uid: String): LiveData<List<Notification>>

    fun setNotificationsRead(uid: String, ids: List<String?>, read: Boolean): Task<Unit>

}