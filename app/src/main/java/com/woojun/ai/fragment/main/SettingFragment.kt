package com.woojun.ai.fragment.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.woojun.ai.IntroActivity
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentSettingBinding

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
                startActivity(Intent(requireContext(), IntroActivity::class.java))
                finishAffinity(requireActivity())
            }

            withdrawalButton.setOnClickListener {
                val user = Firebase.auth.currentUser!!

                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            database.child("users").child(user.uid).setValue(null)
                            startActivity(Intent(requireContext(), IntroActivity::class.java))
                            finishAffinity(requireActivity())
                        } else {
                            Toast.makeText(requireContext(), "회원탈퇴를 실패하였습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            rePersonalInformationButton.setOnClickListener{
                findNavController().navigate(R.id.action_home_to_searchFragment)
            }

            privacyPolicyButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sch10719.neocities.org/codingvocaprivacypolicy"))
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}