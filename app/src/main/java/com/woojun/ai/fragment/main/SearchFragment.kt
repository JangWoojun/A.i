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

class SearchFragment : Fragment(), FragmentInteractionListener {

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

    override fun searchAction(name: String) {
        binding.apply {
            searchTextList.layoutManager = LinearLayoutManager(requireContext().applicationContext)
            searchTextList.adapter = SearchAdapter(readStringListFromInternalStorage(requireContext(), "search_text"), this@SearchFragment)

            textView.visibility = View.GONE
            searchTextList.visibility = View.GONE

            val retrofitAPI = RetrofitClient.getInstance().create(RetrofitAPI::class.java)
            val call: Call<AiResultList> = retrofitAPI.getAiResult(BuildConfig.ESNTLID, BuildConfig.AUTHKEY, 10, name, null, null)

            call.enqueue(object : Callback<AiResultList> {
                override fun onResponse(call: Call<AiResultList>, response: Response<AiResultList>) {
                    if (response.isSuccessful) {
                        val aiResultList: AiResultList? = response.body()
                        aiResultList?.let {
                            val searchTextList = readStringListFromInternalStorage(requireContext(), "search_text")
                            searchTextList.add(name)
                            writeStringListToInternalStorage(requireContext(), "search_text", removeEmptyAndDuplicateStrings(searchTextList))
                            searchList.layoutManager = LinearLayoutManager(requireContext().applicationContext)
                            searchList.adapter = ChildrenInfoAdapter(aiResultList.list, ChildInfoType.SEARCH)
                        }
                    } else {
                        Log.d("확인1", "에러")
                    }
                }

                override fun onFailure(call: Call<AiResultList>, t: Throwable) {
                    Log.e("확인2", "API call failed: " + t.message);
                }
            })
        }


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