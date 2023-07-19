package com.woojun.ai.fragment.main

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.woojun.ai.databinding.FragmentChildrenListBinding
import com.woojun.ai.util.ChildInfo
import com.woojun.ai.util.ChildInfoType
import com.woojun.ai.util.ChildrenInfoAdapter


class ChildrenListFragment : Fragment() {

    private val list: ArrayList<ChildInfo> = arrayListOf(
        ChildInfo(
            name = "이동진",
            image = null,
            age = "8세",
            birthday = "2015년 11월 12일",
            sex = "남자",
            date = "2023년 6월 28일",
            location = "경북",
            height = 118,
            weight = 24,
            frame = "보통",
            faceType = "날카로운 얼굴",
            hairColor = "검은색",
            hairType = "짧은 머리",
            dress = "오버올과 티셔츠"
        ),
        ChildInfo(
            name = "김지현",
            image = null,
            age = "3세",
            birthday = "2020년 1월 1일",
            sex = "여자",
            date = "2023년 6월 28일",
            location = "서울",
            height = 100,
            weight = 15,
            frame = "마름",
            faceType = "동그란 얼굴",
            hairColor = "갈색",
            hairType = "짧은 머리",
            dress = "플로럴 패턴 드레스"
        ),
        ChildInfo(
            name = "김준호",
            image = null,
            age = "4세",
            birthday = "2019년 3월 15일",
            sex = "남자",
            date = "2023년 6월 28일",
            location = "부산",
            height = 110,
            weight = 20,
            frame = "보통",
            faceType = "날카로운 얼굴",
            hairColor = "검은색",
            hairType = "긴 머리",
            dress = "티셔츠와 청바지"
        ),
        ChildInfo(
            name = "최준영",
            image = null,
            age = "7세",
            birthday = "2016년 7월 17일",
            sex = "남자",
            date = "2023년 6월 28일",
            location = "충남",
            height = 109,
            weight = 21,
            frame = "마름",
            faceType = "날카로운 얼굴",
            hairColor = "검은색",
            hairType = "짧은 머리",
            dress = "후드티와 스웨트팬츠"
        ),
        ChildInfo(
            name = "이서연",
            image = null,
            age = "7세",
            birthday = "2016년 2월 7일",
            sex = "여자",
            date = "2023년 6월 28일",
            location = "경기",
            height = 102,
            weight = 16,
            frame = "마름",
            faceType = "날카로운 얼굴",
            hairColor = "갈색",
            hairType = "긴 머리",
            dress = "점프수트와 스니커즈"
        ),
        ChildInfo(
            name = "정성민",
            image = null,
            age = "6세",
            birthday = "2017년 12월 20일",
            sex = "남자",
            date = "2023년 6월 28일",
            location = "경남",
            height = 113,
            weight = 19,
            frame = "보통",
            faceType = "날카로운 얼굴",
            hairColor = "검은색",
            hairType = "짧은 머리",
            dress = "카디건과 면바지"
        ),
        ChildInfo(
            name = "정민지",
            image = null,
            age = "4세",
            birthday = "2019년 8월 3일",
            sex = "여자",
            date = "2023년 6월 28일",
            location = "전남",
            height = 97,
            weight = 14,
            frame = "마름",
            faceType = "동그란 얼굴",
            hairColor = "블론드",
            hairType = "긴 머리",
            dress = "카디건과 레깅스"
        ),
        ChildInfo(
            name = "박현우",
            image = null,
            age = "6세",
            birthday = "2017년 9월 25일",
            sex = "남자",
            date = "2023년 6월 28일",
            location = "인천",
            height = 115,
            weight = 22,
            frame = "보통",
            faceType = "날카로운 얼굴",
            hairColor = "검은색",
            hairType = "긴 머리",
            dress = "셔츠와 슬랙스"
        ),
        ChildInfo(
            name = "박가온",
            image = null,
            age = "5세",
            birthday = "2018년 6월 10일",
            sex = "여자",
            date = "2023년 6월 28일",
            location = "대구",
            height = 95,
            weight = 13,
            frame = "마름",
            faceType = "둥근 얼굴",
            hairColor = "브라운",
            hairType = "짧은 머리",
            dress = "티셔츠와 스커트"
        ),
        ChildInfo(
            name = "최지은",
            image = null,
            age = "5세",
            birthday = "2018년 4월 5일",
            sex = "여자",
            date = "2023년 6월 28일",
            location = "충북",
            height = 101,
            weight = 18,
            frame = "보통",
            faceType = "동그란 얼굴",
            hairColor = "갈색",
            hairType = "긴 머리",
            dress = "롱 코트와 부츠"
        )
    )

    private var _binding: FragmentChildrenListBinding? = null
    private val binding get() = _binding!!

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
                    animator.duration = 300

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
                animator.duration = 300

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
                    animator.duration = 300

                    animator.addUpdateListener { animation ->
                        val animatedValue = animation.animatedValue as Int
                        buttonScrollView.scrollTo(animatedValue, 0)
                    }

                    animator.start()
                }
            }

            childrenList.layoutManager = LinearLayoutManager(requireContext().applicationContext)
            childrenList.adapter = ChildrenInfoAdapter(list, ChildInfoType.DEFAULT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}