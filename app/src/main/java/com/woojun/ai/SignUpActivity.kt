package com.woojun.ai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.woojun.ai.databinding.ActivitySignUpBinding


class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.apply {

            textInputLayoutPaddingSetting()

            moveLoginInButton.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            }

            signButton.setOnClickListener {
                nameInputLayout.isErrorEnabled = false
                emailInputLayout.isErrorEnabled = false
                phoneNumberInputLayout.isErrorEnabled = false
                passwordInputLayout.isErrorEnabled = false

                validationCheck(
                    nameArea.text.toString(),
                    emailArea.text.toString(),
                    phoneArea.text.toString(),
                    passwordArea.text.toString()
                )
            }
        }
    }

    private fun validationCheck(name: String, email: String, phoneNumber: String, password: String): Boolean {
        binding.apply {

            if (name.isEmpty()) {
                nameInputLayout.error = "이름을 입력해주세요"
                return false
            }

            if (email.isEmpty()) {
                emailInputLayout.error = "이메일을 입력해주세요"
                return false
            } else if (!isEmailValid(email)) {
                emailInputLayout.error = "유효한 이메일 형식이 아닙니다"
                return false
            }

            if (phoneNumber.isEmpty()) {
                phoneNumberInputLayout.error = "전화번호를 입력해주세요"
                return false
            } else if (!isPhoneNumberValid(phoneNumber)) {
                phoneNumberInputLayout.error = "-을 제외한 숫자로만 전체 번호를 입력해주세요"
                return false
            }

            if (password.isEmpty()) {
                passwordInputLayout.error = "비밀번호를 입력해주세요"
                return false
            } else if (!isPasswordValid(password)) {
                passwordInputLayout.error = "영문, 숫자, 특수문자 조합으로 8자 이상이 필요합니다"
                return false
            }

            nameInputLayout.error = ""
            emailInputLayout.error = ""
            phoneNumberInputLayout.error = ""
            passwordInputLayout.error = ""

            return true
        }
    }

    private fun textInputLayoutPaddingSetting() {
        binding.apply {

            nameInputLayout.apply {
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

            phoneNumberInputLayout.apply {
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

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val phoneNumberRegex = Regex("""^\d{3}\d{3,4}\d{4}$""")
        return phoneNumberRegex.matches(phoneNumber)
    }

}