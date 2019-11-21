package ru.nickb.kotlininst.common.firebase

import com.google.android.gms.tasks.Task
import ru.nickb.kotlininst.common.AuthManager
import ru.nickb.kotlininst.common.toUnit
import ru.nickb.kotlininst.data.firebase.common.auth

class FirebaseAuthManager: AuthManager {
    override fun signOut() {
        auth.signOut()
    }

    override fun signIn(email: String, password: String): Task<Unit> =
        auth.signInWithEmailAndPassword(email, password).toUnit()

}