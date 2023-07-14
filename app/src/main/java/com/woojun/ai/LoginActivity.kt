package com.woojun.ai

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.woojun.ai.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            moveSignInButton.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }

            loginButton.setOnClickListener {
                validationCheck(emailArea.text.toString(), passwordArea.text.toString())
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

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$""")
        return emailRegex.matches(email)
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+\$).{8,}\$")
        return passwordRegex.matches(password)
    }
}