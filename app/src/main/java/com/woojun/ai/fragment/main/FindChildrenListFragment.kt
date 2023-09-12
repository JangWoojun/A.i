package com.woojun.ai.fragment.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.woojun.ai.databinding.FragmentFindChildrenListBinding
import com.woojun.ai.util.ChildInfo
import com.woojun.ai.util.FindChildImageResult
import com.woojun.ai.util.MyChildAdapterType
import com.woojun.ai.util.MyChildInfoAdapter
import com.woojun.ai.util.ProgressUtil

class FindChildrenListFragment : Fragment() {

    private var _binding: FragmentFindChildrenListBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private val list = arrayListOf<ChildInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFindChildrenListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val bundle = arguments
            val similarDistanceUid = bundle?.getParcelable<FindChildImageResult>("children info")!!.similar_distance_uid.distinct()

            database = Firebase.database.reference

            val loadingDialog = ProgressUtil.createLoadingDialog(requireContext())
            loadingDialog.show()

            similarDistanceUid.forEach {
                database.child("children").child(it[0].replace("\"", "")).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val value = snapshot.getValue(ChildInfo::class.java)
                            value?.let { it1 -> list.add(it1) }
                            if (list.size == similarDistanceUid.size) {
                                loadingDialog.dismiss()
                                list.reverse()

                                findChildrenList.layoutManager = LinearLayoutManager(requireContext().applicationContext)
                                findChildrenList.adapter = MyChildInfoAdapter(list, MyChildAdapterType.Find)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        loadingDialog.dismiss()
                    }
                })
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}