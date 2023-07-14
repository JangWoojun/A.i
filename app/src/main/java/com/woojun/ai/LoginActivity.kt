package com.woojun.ai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.woojun.ai.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        textInputLayoutPaddingSetting()

        binding.apply {
            moveSignInButton.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }

            loginButton.setOnClickListener {
                emailInputLayout.isErrorEnabled = false
                passwordInputLayout.isErrorEnabled = false

                val loginCheck = validationCheck(emailArea.text.toString(), passwordArea.text.toString())

                if (loginCheck) {
                    loginUser(emailArea.text.toString(), passwordArea.text.toString())
                }
            }
        }
    }

    private fun validationCheck(email: String, password: String): Boolean {
        binding.apply {

            if (email.isEmpty()) {
                emailInputLayout.error = "이메일을 입력해주세요"
                return false
            } else if (!isEmailValid(email)) {
                emailInputLayout.error = "유효한 이메일 형식이 아닙니다"
                return false
            }

            if (password.isEmpty()) {
                passwordInputLayout.error = "비밀번호를 입력해주세요"
                return false
            } else if (!isPasswordValid(password)) {
                passwordInputLayout.error = "영문, 숫자, 특수문자 조합으로 8자 이상이 필요합니다"
                return false
            }

            emailInputLayout.error = ""
            passwordInputLayout.error = ""

            return true
        }
    }

    private fun textInputLayoutPaddingSetting() {
        binding.apply {

            emailInputLayout.apply {
                viewTreeObserver.addOnGlobalLayoutListener {
                    if (childCount > 1) {
                        getChildAt(1)?.setPadding(
                            8,
                            20,
                            0,
                            0
                        )
                    }
                }
            }

            passwordInputLayout.apply {
                viewTreeObserver.addOnGlobalLayoutListener {
                    if (childCount > 1) {
                        getChildAt(1)?.setPadding(
                            8,
                            20,
                            0,
                            0
                        )
                    }
                }
            }

        }
    }


    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""")
        return emailRegex.matches(email)
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+\$).{8,}\$")
        return passwordRegex.matches(password)
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "로그인을 성공하셨습니다", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finishAffinity()
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: Exception) {
                        binding.emailInputLayout.error = "해당 이메일 또는 비밀번호가 유효하지 않습니다"
                    }
                }
            }
    }
}