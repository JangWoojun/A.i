package com.woojun.ai.fragment.main

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.ai.databinding.FragmentChildrenListBinding
import com.woojun.ai.util.ChildInfoType
import com.woojun.ai.util.ChildrenInfoAdapter
import com.woojun.ai.util.ViewModel


class ChildrenListFragment : Fragment() {

    private var _binding: FragmentChildrenListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChildrenListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonScrollView.post {
                val scrollWidth = buttonScrollView.getChildAt(0).width
                val viewWidth = buttonScrollView.width
                val middle = (scrollWidth - viewWidth) / 2
                buttonScrollView.smoothScrollTo(middle, 0)
            }

            newChildrenButton.setOnClickListener {

                newChildrenText.setTextColor(Color.parseColor("#4894fe"))
                newChildrenButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EBF5FF"))

                allChildrenText.setTextColor(Color.parseColor("#8696BB"))
                allChildrenButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FAFAFA"))

                longChildrenText.setTextColor(Color.parseColor("#8696BB"))
                longChildrenButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FAFAFA"))

                buttonScrollView.post {
                    val animator = ValueAnimator.ofInt(buttonScrollView.scrollX, 0)
                    animator.duration = 500

                    animator.addUpdateListener { animation ->
                        val animatedValue = animation.animatedValue as Int
                        buttonScrollView.scrollTo(animatedValue, 0)
                    }

                    animator.start()
                }
            }

            allChildrenButton.setOnClickListener {

                newChildrenText.setTextColor(Color.parseColor("#8696BB"))
                newChildrenButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FAFAFA"))

                allChildrenText.setTextColor(Color.parseColor("#4894fe"))
                allChildrenButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EBF5FF"))

                longChildrenText.setTextColor(Color.parseColor("#8696BB"))
                longChildrenButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FAFAFA"))

                val scrollWidth = buttonScrollView.getChildAt(0).width
                val viewWidth = buttonScrollView.width
                val middle = (scrollWidth - viewWidth) / 2

                val animator = ValueAnimator.ofInt(buttonScrollView.scrollX, middle)
                animator.duration = 500

                animator.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Int
                    buttonScrollView.scrollTo(animatedValue, 0)
                }

                animator.start()

            }

            longChildrenButton.setOnClickListener {

                newChildrenText.setTextColor(Color.parseColor("#8696BB"))
                newChildrenButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FAFAFA"))

                allChildrenText.setTextColor(Color.parseColor("#8696BB"))
                allChildrenButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FAFAFA"))

                longChildrenText.setTextColor(Color.parseColor("#4894fe"))
                longChildrenButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EBF5FF"))


                buttonScrollView.post {
                    val maxScrollAmount = buttonScrollView.getChildAt(0).width - buttonScrollView.width
                    val animator = ValueAnimator.ofInt(buttonScrollView.scrollX, maxScrollAmount)
                    animator.duration = 500

                    animator.addUpdateListener { animation ->
                        val animatedValue = animation.animatedValue as Int
                        buttonScrollView.scrollTo(animatedValue, 0)
                    }

                    animator.start()
                }
            }

            viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

            viewModel.getApiData().observe(viewLifecycleOwner) { apiData ->
                childrenList.layoutManager = LinearLayoutManager(requireContext().applicationContext)
                childrenList.adapter = ChildrenInfoAdapter(apiData.subList(0, 10), ChildInfoType.DEFAULT)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}