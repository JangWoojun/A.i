package com.woojun.ai.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.woojun.ai.R
import com.woojun.ai.databinding.ChildrenInfoItemBinding
import com.woojun.ai.databinding.NewChildrenInfoItemBinding

class ChildrenInfoAdapter(private val children: ArrayList<ChildInfo>, private val type: ChildInfoType): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ChildInfoType.NEW.ordinal -> {
                val binding = NewChildrenInfoItemBinding.inflate(inflater, parent, false)
                NewChildrenInfoViewHolder(binding).also { handler ->
                    binding.apply {
                        newChildrenInfoItem.setOnClickListener {
                            val bundle = Bundle()
                            val item = children[handler.position]
                            bundle.putSerializable("child info", item)

                            parent.findNavController().navigate(R.id.action_home_to_childrenInfoInternalFragment2, bundle)
                        }
                    }
                }
            }
            ChildInfoType.DEFAULT.ordinal -> {
                val binding = ChildrenInfoItemBinding.inflate(inflater, parent, false)
                ChildrenInfoViewHolder(binding).also {
                    binding.apply {
                        viewDetailsButton.setOnClickListener {

                        }
                    }
                }
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }
    override fun getItemCount(): Int {
        return when (type) {
            ChildInfoType.NEW -> {
                3
            }

            ChildInfoType.DEFAULT -> {
                children.size
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return type.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val childInfo = children[position]
        when (holder) {
            is NewChildrenInfoViewHolder -> {
                holder.bind(childInfo)
            }
            is ChildrenInfoViewHolder -> {
                holder.bind(childInfo)
            }
        }
    }

    class NewChildrenInfoViewHolder(private val binding: NewChildrenInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(childrenInfo: ChildInfo) {
            binding.apply {
                name.text = childrenInfo.name
                image.setImageResource(R.drawable.profile)
                sexBirthday.text = "${childrenInfo.sex} · ${childrenInfo.birthday?.substringBefore("년")}년생"
                location.text = childrenInfo.location
                date.text = "일시: ${childrenInfo.date?.let { formatDate(it) }}"
                age.text = "나이: ${childrenInfo.age1}(${childrenInfo.age2})"
            }
        }
    }

    class ChildrenInfoViewHolder(private val binding: ChildrenInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(childrenInfo: ChildInfo) {
            binding.apply {
                name.text = childrenInfo.name
                image.setImageResource(R.drawable.profile)
                sexBirthday.text =
                    "${childrenInfo.sex} · ${childrenInfo.birthday?.substringBefore("년")}년생"
                location.text = childrenInfo.location
                date.text = "일시: ${childrenInfo.date?.let { formatDate(it) }}"
                age.text = "나이: ${childrenInfo.age1}(${childrenInfo.age2})"
            }
        }
    }
}

fun formatDate(inputDateStr: String): String {
    val regex = Regex("""(\d{4})년 (\d{1,2})월 (\d{1,2})일""")
    val matchResult = regex.find(inputDateStr)

    return if (matchResult != null) {
        val year = matchResult.groupValues[1]
        val month = matchResult.groupValues[2].padStart(2, '0')
        val day = matchResult.groupValues[3].padStart(2, '0')
        "$year. $month. $day"
    } else {
        "오류"
    }
}