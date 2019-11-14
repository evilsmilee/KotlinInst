package ru.nickb.kotlininst.activities

import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference


import ru.nickb.kotlininst.R
import ru.nickb.kotlininst.models.FeedPost
import ru.nickb.kotlininst.models.User
import ru.nickb.kotlininst.utils.GlideApp


fun ImageView.loadUserPhoto(photoUrl: String?) =
    ifNotDestroyed {
    GlideApp.with(this).load(photoUrl).fallback(R.drawable.person).into(this)
    }



fun ImageView.loadImage(image: String) =
    ifNotDestroyed {
        GlideApp.with(this).load(image).centerCrop().into(this)
    }


private fun View.ifNotDestroyed(block: () -> Unit) {
    if (!(context as AppCompatActivity).isDestroyed) {
        block()
    }
}


fun <T> task(block: (TaskCompletionSource<T>) -> Unit): Task<T> {
    val taskSource = TaskCompletionSource<T>()
    block(taskSource)
    return taskSource.task
}

fun DataSnapshot.asUser(): User? =
    key?.let { getValue(User::class.java)?.copy(uid = it) }

fun DataSnapshot.asFeedPost(): FeedPost? =
    key?.let { getValue(FeedPost::class.java)?.copy(id = it) }

fun DatabaseReference.setValueTrueOrRemove(value: Boolean) =
    if (value) setValue(true) else removeValue()

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Editable.toStringOrNull(): String? {
    val str = toString()
    return if (str.isEmpty()) null else str
}


fun <A, B> LiveData<A>.map(f: (A) -> B): LiveData<B> =
    Transformations.map(this, f)

fun coordinateBtnAndInputs(btn: Button, vararg inputs: EditText) {
    val watcher = object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            btn.isEnabled = inputs.all{it.text.isNotEmpty()}}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    }

    inputs.forEach { it.addTextChangedListener(watcher) }
    btn.isEnabled = inputs.all{it.text.isNotEmpty()}

    class TaskSourceOnCompleteListener<T>(private val taskSource: TaskCompletionSource<T>) :
        OnCompleteListener<T> {
        override fun onComplete(task: Task<T>) {
            if (task.isSuccessful) {
                taskSource.setResult(task.result)
            } else {
                taskSource.setException(task.exception!!)
            }

        }
    }

}