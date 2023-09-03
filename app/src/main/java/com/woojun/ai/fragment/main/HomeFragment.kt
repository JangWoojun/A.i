package com.woojun.ai.fragment.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.woojun.ai.MainActivity
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentHomeBinding
import com.woojun.ai.util.AppDatabase
import com.woojun.ai.util.ChildInfoType
import com.woojun.ai.util.ChildrenInfoAdapter
import com.woojun.ai.util.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var viewModel: ViewModel

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

            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(requireContext())
                val user = db!!.userInfoDao().getUser()

                if (user.check) {
                    childrenIdentificationButton.visibility = View.VISIBLE
                } else {
                    childrenIdentificationButton.visibility = View.GONE
                }
                helloUserText.text = "반갑습니다 ${user.name}님"
            }

            viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

            viewModel.getApiData().observe(viewLifecycleOwner) { apiData ->
                childrenList.layoutManager = LinearLayoutManager(requireContext().applicationContext)
                childrenList.adapter = ChildrenInfoAdapter(apiData.subList(0, 3), ChildInfoType.NEW)
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

            settingButton.setOnClickListener {
                (activity as MainActivity).moveBottomNavigation(R.id.setting)
            }

            seeAllButton.setOnClickListener {
                (activity as MainActivity).moveBottomNavigation(R.id.childrenList)
            }

            Glide.with(requireContext())
                .load(R.drawable.profile)
                .apply(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
                .into(profile)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}