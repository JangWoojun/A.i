package com.woojun.ai.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.woojun.ai.databinding.FragmentChildrenInfoInternalBinding
import com.woojun.ai.util.AiResult
import java.text.SimpleDateFormat

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
            val item = bundle?.getParcelable<AiResult>("child info")

            if (item != null) {
                name.text = "이름: ${item!!.nm}"
                age1.text = "실종 나이: ${item.age}"
                age2.text = "현재 나이: ${item.ageNow}"
                sex.text = "성별: ${item.sexdstnDscd}"
                date.text = "발생 일시: ${item.occrde}"
                location.text = "지역: ${item.occrAdres}"
                dress.text = "실종 당시 복장: ${item.alldressingDscd}"
                type.text = "타입: ${item.writngTrgetDscd}"
                characteristics.text = "특징: ${item.etcSpfeatr}"
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyyMMdd")
        val outputFormat = SimpleDateFormat("yy. MM. dd")

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }



}