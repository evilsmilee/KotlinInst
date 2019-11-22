package ru.nickb.kotlininst

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import ru.nickb.kotlininst.common.formatRelativeTimestamp
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TextUtilsTest {
    private val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, 2019)
        set(Calendar.MONTH, 0)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
    }

    @Test
    fun shouldFormatRelativeTime() {
        val time = calendar.time
        val tenSeconds = calendar.change { add(Calendar.SECOND, 10) }.time
        val tenMinutes = calendar.change { add(Calendar.MINUTE, 10) }.time
        val tenHours = calendar.change { add(Calendar.HOUR_OF_DAY, 10) }.time
        val oneDay = calendar.change { add(Calendar.DAY_OF_MONTH, 10) }.time
        assertEquals("10 sec", formatRelativeTimestamp(time, tenSeconds))
        assertEquals("10 min", formatRelativeTimestamp(time, tenMinutes))
        assertEquals("1 hr", formatRelativeTimestamp(time, tenHours))
        assertEquals("Yesterday", formatRelativeTimestamp(time, oneDay))
    }

    private fun Calendar.change(f: Calendar.() -> Unit): Calendar {
        val newCalendar = clone() as Calendar
        f(newCalendar)
        return newCalendar
    }

}
