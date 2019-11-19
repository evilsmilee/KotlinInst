package ru.nickb.kotlininst.common.firebase

import ru.nickb.kotlininst.common.AuthManager
import ru.nickb.kotlininst.data.firebase.common.auth

class FirebaseAuthManager: AuthManager {
    override fun signOut() {
        auth.signOut()
    }
}