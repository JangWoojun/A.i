package com.woojun.ai.fragment.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.woojun.ai.MainActivity
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentHomeBinding
import com.woojun.ai.util.AiResultList
import com.woojun.ai.util.ChildInfoType
import com.woojun.ai.util.ChildrenInfoAdapter

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}