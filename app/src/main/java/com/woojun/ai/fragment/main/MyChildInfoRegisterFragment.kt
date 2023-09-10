package com.woojun.ai.fragment.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.woojun.ai.MainActivity
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentMyChildInfoRegisterBinding
import com.woojun.ai.util.AppDatabase
import com.woojun.ai.util.CameraType
import com.woojun.ai.util.ChildInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyChildInfoRegisterFragment : Fragment() {

    private var _binding: FragmentMyChildInfoRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    private var sex = "남성"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyChildInfoRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            auth = Firebase.auth

            val mainActivity = activity as MainActivity
            mainActivity.hideBottomNavigation(true)

            selectManButton.setOnClickListener {
                manBackground.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4894FE"))
                manIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                manText.setTextColor(Color.parseColor("#4894FE"))

                womanBackground.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                womanIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                womanText.setTextColor(Color.parseColor("#000000"))

                sex = "남성"
            }

            selectWomanButton.setOnClickListener {
                manBackground.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                manIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
                manText.setTextColor(Color.parseColor("#000000"))

                womanBackground.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4894FE"))
                womanIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                womanText.setTextColor(Color.parseColor("#4894FE"))

                sex = "여성"
            }

            yearAreaField.setOnClickListener {
                yearArea.requestFocus()
            }

            monthAreaField.setOnClickListener {
                monthArea.requestFocus()
            }

            dateAreaField.setOnClickListener {
                dateArea.requestFocus()
            }

            characteristicsAreaField.setOnClickListener {
                characteristicsArea.requestFocus()
            }

            cameraButton.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(requireContext())
                    val user = db!!.userInfoDao().getUser()

                    val childInfo = ChildInfo(
                        id = auth.uid+user.children.size,
                        name = nameArea.text.toString(),
                        birthDate = "${yearArea.text}${monthArea.text}${dateArea.text}",
                        sex = sex,
                        characteristics = characteristicsArea.text.toString(),
                        photo = "null",
                        lastIdentityDate = getToday()
                    )

                    withContext(Dispatchers.Main) {
                        if (validationCheck(childInfo)) {
                            val bundle = Bundle()
                            bundle.putParcelable("camera type", CameraType.ChildRegister)
                            bundle.putParcelable("child info", childInfo)

                            view.findNavController().navigate(R.id.action_myChildInfoRegisterFragment_to_cameraFragment, bundle)
                        }
                    }
                }
            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getToday(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    private fun validationCheck(childInfo: ChildInfo): Boolean {
        binding.apply {

            if (childInfo.name.isEmpty()) {
                Toast.makeText(requireContext(), "이름을 입력해주세요", Toast.LENGTH_SHORT).show()
                return false
            }

            if (childInfo.birthDate.length != 8) {
                Toast.makeText(requireContext(), "생년월일을 정확히 모두 입력해주세요", Toast.LENGTH_SHORT).show()
                return false
            }

            return true
        }
    }
}