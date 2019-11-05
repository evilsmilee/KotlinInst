package ru.nickb.kotlininst.utils

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ValueEventListenerAdapter(val handler: (DataSnapshot) -> Unit) :
    ValueEventListener {
    override fun onCancelled(error: DatabaseError) {
        Log.e("Canceled", error.message)
    }

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }

}