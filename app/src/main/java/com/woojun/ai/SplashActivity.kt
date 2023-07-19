package com.woojun.ai

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
        auth.signOut()

        Handler().postDelayed({
            if (auth.currentUser == null) {
                startActivity(Intent(this, IntroActivity::class.java))
                finishAffinity()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
        },1500)
    }
}