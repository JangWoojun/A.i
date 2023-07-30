package com.woojun.ai.fragment.intro

import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.woojun.ai.MainActivity
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.apply {

            forgotPasswordButton.setOnClickListener {
                val dialog = Dialog(requireContext())
                dialog.setContentView(R.layout.password_forget_dialog)
                val windowWidth = resources.displayMetrics.widthPixels * 0.95
                val windowHeight = resources.displayMetrics.heightPixels * 0.35
                dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
                dialog.window?.setLayout(windowWidth.toInt(), windowHeight.toInt())

                dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

                val editText = dialog.findViewById<EditText>(R.id.email_area)
                val okButton = dialog.findViewById<CardView>(R.id.ok_button)
                val cancelButton = dialog.findViewById<CardView>(R.id.cancel_button)

                okButton.setOnClickListener {
                    val enteredText = editText.text.toString()
                    sendPasswordResetEmail(enteredText)
                    dialog.dismiss()
                }

                cancelButton.setOnClickListener {
                    dialog.dismiss()
                }

                dialog.show()
            }

            moveSignUpText.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            moveSignUpButton.setOnClickListener {
                view.findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
            }

            loginButton.setOnClickListener {
                emailInputLayout.isErrorEnabled = false
                passwordInputLayout.isErrorEnabled = false

                val loginCheck = validationCheck(
                    emailArea.text.toString().trim(),
                    passwordArea.text.toString().trim()
                )

                if (loginCheck) {
                    loginUser(emailArea.text.toString().trim(), passwordArea.text.toString().trim())
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.loginButton.isEnabled = false
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "로그인을 성공하셨습니다", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finishAffinity()
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: Exception) {
                        binding.emailInputLayout.error = "해당 이메일 또는 비밀번호가 유효하지 않습니다"
                    }
                }
                binding.loginButton.isEnabled = true
            }
    }

    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "비밀번호 재설정 이메일이 성공적으로 보내졌습니다", Toast.LENGTH_SHORT).show()
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthInvalidUserException) {
                        Toast.makeText(requireContext(), "이메일이 등록되지 않은 사용자입니다", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("확인", "에러")
                    }
                }
            }
    }
}