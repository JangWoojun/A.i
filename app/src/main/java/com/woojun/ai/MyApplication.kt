package com.woojun.ai

import android.app.Application
import android.database.CursorWindow
import java.lang.reflect.Field

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 4963798)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
