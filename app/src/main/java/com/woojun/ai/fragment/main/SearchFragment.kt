package com.woojun.ai.fragment.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.ai.BuildConfig
import com.woojun.ai.databinding.FragmentSearchBinding
import com.woojun.ai.util.AiResultList
import com.woojun.ai.util.ChildInfoType
import com.woojun.ai.util.ChildrenInfoAdapter
import com.woojun.ai.util.FragmentInteractionListener
import com.woojun.ai.util.RetrofitAPI
import com.woojun.ai.util.RetrofitClient
import com.woojun.ai.util.SearchAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            apiArea.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    apiInputLayout.hint = null
                } else {
                    apiInputLayout.hint = "실종아동 이름을 입력해주세요"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    fun removeEmptyAndDuplicateStrings(inputList: List<String>): ArrayList<String> {
        val uniqueNonEmptyStrings = HashSet<String>()
        val result = arrayListOf<String>()

        for (item in inputList) {
            if (item.isNotBlank() && !uniqueNonEmptyStrings.contains(item)) {
                uniqueNonEmptyStrings.add(item)
                result.add(item)
            }
        }

        return result
    }

}