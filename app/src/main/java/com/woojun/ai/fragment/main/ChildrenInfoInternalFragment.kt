package com.woojun.ai.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.woojun.ai.databinding.FragmentChildrenInfoInternalBinding
import com.woojun.ai.util.AiResult
import net.daum.mf.map.api.MapView
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
            val mapView = MapView(requireContext())
            binding.mapView.addView(mapView)

            val bundle = arguments
            val item = bundle?.getParcelable<AiResult>("child info")

            if (item != null) {
                name.text = "${item.nm}"
                age1.text = "${item.age}세"
                age2.text = "${item.ageNow}세"
                sex.text = "${item.sexdstnDscd}"
                date.text = "${item.occrde?.let { formatDate(it) }}"
                location.text = "${item.occrAdres}"
                dress.text = item.alldressingDscd ?: "불명"
                type.text = "${item.writngTrgetDscd?.let { getStatusDescription(it) }}"
                characteristics.text = "${item.etcSpfeatr}"
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

    private fun getStatusDescription(code: String): String {
        return when (code) {
            "010" -> "정상아동"
            "020" -> "가출인"
            "040" -> "시설보호무연고자"
            "060" -> "지적장애인"
            "061" -> "아동\n지적장애인"
            "062" -> "성인\n지적장애인"
            "070" -> "치매질환자"
            "080" -> "불상(기타)"
            else -> "불명"
        }
    }

}