package com.woojun.ai.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.woojun.ai.MainActivity
import com.woojun.ai.databinding.FragmentChildrenInfoInternalBinding
import com.woojun.ai.util.ChildInfo

class ChildrenInfoInternalFragment : Fragment() {

    private var _binding: FragmentChildrenInfoInternalBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChildrenInfoInternalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val bundle = arguments
            val item = bundle?.getSerializable("child info") as ChildInfo

            name.text = "이름: ${item.name}"
            age1.text = "실종 나이: ${item.age1}"
            age2.text = "현재 나이: ${item.age2}"
            birthday.text = "생일: ${item.birthday}"
            location.text = "지역: ${item.location}"
            height.text = "키: ${item.height}"
            weight.text = "몸무게: ${item.weight}"
            frame.text = "체격: ${item.frame}"
            faceType.text = "얼굴 특징: ${item.faceType}"
            hairColor.text = "머리 색: ${item.hairColor}"
            hairType.text = "머리 특징: ${item.hairType}"
            dress.text = "실종 당시 복장: ${item.dress}"
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}