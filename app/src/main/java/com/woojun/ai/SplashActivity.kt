package com.woojun.ai

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = Firebase.auth
        Handler().postDelayed({
            if (auth.currentUser == null) {
                writeStringListToInternalStorage(baseContext, "search_text", arrayListOf())
                startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                finishAffinity()
            } else {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finishAffinity()
            }
        }, 2000)
    }

    private fun writeStringListToInternalStorage(context: Context, filename: String, stringList: ArrayList<String>) {
        try {
            val outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            val fileContent = stringList.joinToString("\n")
            outputStream.write(fileContent.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}