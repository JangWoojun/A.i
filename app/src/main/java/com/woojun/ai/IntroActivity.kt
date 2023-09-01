package com.woojun.ai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.woojun.ai.databinding.ActivityIntroBinding
import com.woojun.ai.util.AppDatabase
import com.woojun.ai.util.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun loginMove() {
        val auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        Toast.makeText(this, "로그인을 성공하셨습니다", Toast.LENGTH_SHORT).show()

        database.child("users").child(auth.uid.toString()).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val value = snapshot.getValue(UserInfo::class.java)

                    CoroutineScope(Dispatchers.IO).launch {
                        val db = AppDatabase.getDatabase(this@IntroActivity)
                        val user = db!!.userInfoDao()

                        user.insertUser(value!!)

                        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                        finishAffinity()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun signupMove(userInfo: UserInfo) {
        val auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        Toast.makeText(this, "회원가입을 성공하셨습니다", Toast.LENGTH_SHORT).show()

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(this@IntroActivity)
            val userDao = db!!.userInfoDao()

            userDao.insertUser(UserInfo(userInfo.name, userInfo.email, userInfo.phoneNumber, userInfo.check, userInfo.children))
        }

        database.child("users").child("${auth.uid}").setValue(userInfo)

        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }
}