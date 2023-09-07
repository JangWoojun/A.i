package com.woojun.ai.fragment.main

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.woojun.ai.MainActivity
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentMyChildInfoRegisterBinding

class MyChildInfoRegisterFragment : Fragment() {

    private var _binding: FragmentMyChildInfoRegisterBinding? = null
    private val binding get() = _binding!!

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
            val mainActivity = activity as MainActivity
            mainActivity.hideBottomNavigation(true)

            cameraButton.setOnClickListener {
                view.findNavController().navigate(R.id.action_myChildInfoRegisterFragment_to_cameraFragment)
            }

            selectManButton.setOnClickListener {
                manBackground.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EFF7FF"))
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

                womanBackground.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EFF7FF"))
                womanIcon.imageTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                womanText.setTextColor(Color.parseColor("#4894FE"))

                sex = "여성"
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}