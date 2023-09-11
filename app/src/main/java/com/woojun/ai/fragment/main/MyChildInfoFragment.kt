package com.woojun.ai.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentMyChildInfoBinding
import com.woojun.ai.util.AppDatabase
import com.woojun.ai.util.MyChildAdapterType
import com.woojun.ai.util.MyChildInfoAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyChildInfoFragment : Fragment() {

    private var _binding: FragmentMyChildInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyChildInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            childrenInfoRegistrationButton.setOnClickListener {
                view.findNavController().navigate(R.id.action_childrenInfo_to_myChildInfoRegisterFragment)
            }

            CoroutineScope(Dispatchers.IO).launch {
                val db = AppDatabase.getDatabase(requireContext())
                val user = db!!.userInfoDao().getUser()

                withContext(Dispatchers.Main) {
                    myChildrenList.layoutManager = LinearLayoutManager(requireContext().applicationContext)
                    myChildrenList.adapter = MyChildInfoAdapter(user.children, MyChildAdapterType.DEFAULT)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}