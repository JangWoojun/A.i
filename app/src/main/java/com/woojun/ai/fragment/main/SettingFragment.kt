package com.woojun.ai.fragment.main

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.woojun.ai.IntroActivity
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentSettingBinding
import com.woojun.ai.util.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.apply {

            logoutButton.setOnClickListener {
                auth.signOut()
                CoroutineScope(Dispatchers.IO).launch {
                    val db = AppDatabase.getDatabase(requireContext())
                    val userDao = db!!.userInfoDao()
                    val findChildDao = db.findChildDao()

                    userDao.deleteUser(userDao.getUser())
                    findChildDao.deleteFindChild(findChildDao.getFindChild())
                }
                startActivity(Intent(requireContext(), IntroActivity::class.java))
                finishAffinity(requireActivity())
            }

            withdrawalButton.setOnClickListener {

                showPasswordDialog { password ->
                    val user = Firebase.auth.currentUser!!
                    val credential = EmailAuthProvider.getCredential(user.email!!, password)

                    user.reauthenticate(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user.delete()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            val db = AppDatabase.getDatabase(requireContext())
                                            val userDao = db!!.userInfoDao()
                                            val findChildDao = db.findChildDao()

                                            val userChildrenList = userDao.getUser().children

                                            userDao.deleteUser(userDao.getUser())
                                            findChildDao.deleteFindChild(findChildDao.getFindChild())

                                            withContext(Dispatchers.Main) {
                                                database.child("users").child(user.uid).setValue(null)
                                                userChildrenList.forEach {
                                                    database.child("children").child(it.id).setValue(null)
                                                    if (!deleteImageFromFirebaseStorage(it.photo)){
                                                        Toast.makeText(requireContext(), "오류 발생 개발자에게 문의해주세요", Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                                Toast.makeText(requireContext(), "회원탈퇴 완료", Toast.LENGTH_SHORT).show()
                                                startActivity(Intent(requireContext(), IntroActivity::class.java))
                                                finishAffinity(requireActivity())
                                            }
                                        }
                                    } else {
                                        Toast.makeText(requireContext(), "회원탈퇴를 실패하였습니다", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(requireContext(), "비밀번호가 잘못되었습니다", Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }

            rePersonalInformationButton.setOnClickListener{
                findNavController().navigate(R.id.action_setting_to_rePersonalInformationFragment)
            }

            privacyPolicyButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yummy-slime-07b.notion.site/A-2a68a3b16e824ea3b91821695b611150?pvs=4"))
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteImageFromFirebaseStorage(imageUrl: String): Boolean {
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReferenceFromUrl(imageUrl)

        var isOk = true

        storageReference.delete()
            .addOnSuccessListener {
            }
            .addOnFailureListener { _ ->
                isOk = false
            }

        return isOk
    }

    private fun showPasswordDialog(function: (password: String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.password_request_item, null)
        val passwordEditText = dialogView.findViewById<TextInputEditText>(R.id.password_area)
        val submitButton = dialogView.findViewById<CardView>(R.id.finish_button)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(true)
            .create()

        dialog.show()

        val layoutParams = WindowManager.LayoutParams()
        val window = dialog.window
        layoutParams.copyFrom(window?.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        window?.attributes = layoutParams

        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        submitButton.setOnClickListener {
            val password = passwordEditText.text.toString()
            if (password.isNotEmpty()) {
                function(password)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

    }

}