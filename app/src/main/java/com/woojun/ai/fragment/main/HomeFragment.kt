package com.woojun.ai.fragment.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.woojun.ai.MainActivity
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentHomeBinding
import com.woojun.ai.util.AiResultList
import com.woojun.ai.util.ChildInfoType
import com.woojun.ai.util.ChildrenInfoAdapter
import com.woojun.ai.util.UserInfo

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.reference
        auth = Firebase.auth

        binding.apply {
            val aiResultsList = arguments?.getString("item")
            if (aiResultsList != null) {
                val gson = Gson()
                val list = gson.fromJson(aiResultsList, AiResultList::class.java)
                childrenList.layoutManager = LinearLayoutManager(requireContext().applicationContext)
                childrenList.adapter = ChildrenInfoAdapter(list.list, ChildInfoType.NEW)
            }

            mainChildrenInfoButton.setOnClickListener {
                (activity as MainActivity).moveBottomNavigation(R.id.childrenInfo)
            }

            searchButton.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_searchFragment)
            }

            childrenInfoButton.setOnClickListener {
                (activity as MainActivity).moveBottomNavigation(R.id.childrenInfo)
            }

            childrenListButton.setOnClickListener {
                (activity as MainActivity).moveBottomNavigation(R.id.childrenList)
            }

            reportChildrenButton.setOnClickListener {
                startActivity(
                    Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.safe182.go.kr/cont/homeLogContents.do?contentsNm=report_info_182")
                    )
                )
            }

            myInfoButton.setOnClickListener {
                (activity as MainActivity).moveBottomNavigation(R.id.myInfo)
            }

            seeAllButton.setOnClickListener {
                (activity as MainActivity).moveBottomNavigation(R.id.childrenList)
            }
            database.child("users").child(auth.uid.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val value = snapshot.getValue(UserInfo::class.java)
                        helloUserText.text = "반갑습니다 ${value!!.name}님"
                        if (value.photo == "") {
                            Glide.with(requireContext())
                                .load(R.drawable.profile)
                                .apply(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
                                .into(profile)
                        } else {
                            Glide.with(requireContext())
                                .load(value.photo)
                                .apply(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
                                .into(profile)
                        }
                    } else {
                        Glide.with(requireContext())
                            .load(R.drawable.profile)
                            .apply(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
                            .into(profile)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("확인", error.toString())
                }
            })



        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}