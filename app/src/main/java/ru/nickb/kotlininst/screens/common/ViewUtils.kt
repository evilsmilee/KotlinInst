package ru.nickb.kotlininst.screens.common


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import ru.nickb.kotlininst.R


fun ImageView.loadUserPhoto(photoUrl: String?) =
    ifNotDestroyed {
    GlideApp.with(this).load(photoUrl).fallback(R.drawable.person).into(this)
    }



fun ImageView.loadImage(image: String?) =
    ifNotDestroyed {
        GlideApp.with(this).load(image).centerCrop().into(this)
    }


private fun View.ifNotDestroyed(block: () -> Unit) {
    if (!(context as AppCompatActivity).isDestroyed) {
        block()
    }
}

fun Context.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    text?.let { Toast.makeText(this, it, duration).show()  }
}

fun Editable.toStringOrNull(): String? {
    val str = toString()
    return if (str.isEmpty()) null else str
}


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