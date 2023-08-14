package com.woojun.ai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.woojun.ai.databinding.ActivityIntroBinding
import com.woojun.ai.util.UserInfo

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun loginMove() {
        Toast.makeText(this, "로그인을 성공하셨습니다", Toast.LENGTH_SHORT).show()
        val intent1 = Intent(this, MainActivity::class.java)
        intent1.putExtra("item", intent.getStringExtra("item"))
        startActivity(intent1)
        finishAffinity()
    }

    fun signupMove(userInfo: UserInfo) {
        val auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        database.child("users").child("${auth.uid}").setValue(userInfo)
        Toast.makeText(this, "회원가입을 성공하셨습니다", Toast.LENGTH_SHORT).show()
        val intent2 = Intent(this, MainActivity::class.java)
        intent2.putExtra("item", intent.getStringExtra("item"))
        startActivity(intent2)
        finishAffinity()
    }
}