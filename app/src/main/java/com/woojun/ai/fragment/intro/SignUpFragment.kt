package com.woojun.ai.fragment.intro

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.woojun.ai.IntroActivity
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentSignUpBinding
import com.woojun.ai.util.UserInfo

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private lateinit var database: DatabaseReference
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            textInputLayoutPaddingSetting()

            passwordArea.setOnEditorActionListener(getEditorActionListener(signButton))

            moveLoginText.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            moveLoginInButton.setOnClickListener {
                view.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
            }

            signButton.setOnClickListener {
                nameInputLayout.isErrorEnabled = false
                emailInputLayout.isErrorEnabled = false
                phoneNumberInputLayout.isErrorEnabled = false
                passwordInputLayout.isErrorEnabled = false
                checkInputLayout.isErrorEnabled = false

                val userInfo = UserInfo(
                    "",
                    nameArea.text.toString().trim(),
                    emailArea.text.toString().trim(),
                    phoneArea.text.toString().trim(),
                    checkArea.text.toString() == "jR7pK9",
                    arrayListOf()
                )

                val signCheck = validationCheck(userInfo, passwordArea.text.toString().trim())

                if (signCheck) {
                    signUpUser(userInfo, passwordArea.text.toString().trim())
                }

            }

            appleLoginButton.setOnClickListener {
                Toast.makeText(requireContext(), "다음 업데이트에서 추가 예정입니다", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getEditorActionListener(view: View): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.callOnClick()
            }
            false
        }
    }

    private fun validationCheck(userInfo: UserInfo, password: String): Boolean {
        binding.apply {

            if (userInfo.name.isEmpty()) {
                nameInputLayout.error = "이름을 입력해주세요"
                return false
            }

            if (userInfo.email.isEmpty()) {
                emailInputLayout.error = "이메일을 입력해주세요"
                return false
            } else if (!isEmailValid(userInfo.email)) {
                emailInputLayout.error = "유효한 이메일 형식이 아닙니다"
                return false
            }

            if (userInfo.phoneNumber.isEmpty()) {
                phoneNumberInputLayout.error = "전화번호를 입력해주세요"
                return false
            } else if (!isPhoneNumberValid(userInfo.phoneNumber)) {
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

    private fun signUpUser(userInfo: UserInfo, password: String) {
        binding.signButton.isEnabled = false
        val auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference

        auth.createUserWithEmailAndPassword(userInfo.email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    (activity as IntroActivity).signupMove(userInfo)
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        binding.emailInputLayout.error = "이미 등록된 이메일 주소입니다"
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                binding.signButton.isEnabled = true
            }
    }
}