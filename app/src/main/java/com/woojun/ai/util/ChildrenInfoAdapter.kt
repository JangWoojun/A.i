package com.woojun.ai.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.woojun.ai.R
import com.woojun.ai.databinding.ChildrenInfoItemBinding
import com.woojun.ai.databinding.NewChildrenInfoItemBinding
import java.util.Calendar

class ChildrenInfoAdapter(private val children: AiResultList, private val type: ChildInfoType): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
                            bundle.putParcelable("child info", item)

                            parent.findNavController().navigate(R.id.action_home_to_childrenInfoInternalFragment2, bundle)
                        }
                    }
                }
            }
            ChildInfoType.DEFAULT.ordinal -> {
                val binding = ChildrenInfoItemBinding.inflate(inflater, parent, false)
                ChildrenInfoViewHolder(binding).also { handler ->
                    binding.apply {
                        viewDetailsButton.setOnClickListener {
                            val bundle = Bundle()
                            val item = children[handler.position]
                            bundle.putParcelable("child info", item)

                            parent.findNavController().navigate(R.id.action_childrenList_to_childrenInfoInternalFragment2, bundle)
                        }
                    }
                }
            }
            ChildInfoType.SEARCH.ordinal -> {
                val binding = ChildrenInfoItemBinding.inflate(inflater, parent, false)
                ChildrenInfoViewHolder(binding).also { handler ->
                    binding.apply {
                        viewDetailsButton.setOnClickListener {
                            val bundle = Bundle()
                            val item = children[handler.position]
                            bundle.putParcelable("child info", item)

                            parent.findNavController().navigate(R.id.action_searchFragment_to_childrenInfoInternalFragment, bundle)
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

            ChildInfoType.SEARCH -> {
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
        fun bind(childrenInfo: AiResult) {
            binding.apply {
                name.text = childrenInfo.nm
                image.setImageResource(R.drawable.profile)
                sexBirthday.text = "${childrenInfo.sexdstnDscd} · ${(Calendar.getInstance().get(
                    Calendar.YEAR)) - childrenInfo.ageNow!!
                }년생"
                location.text = "${childrenInfo.occrAdres!!.take(2)}"
                date.text = "일시: ${childrenInfo.occrde?.let { formatDate(it) }}"
                age.text = "나이: ${childrenInfo.ageNow}세 (${childrenInfo.age}세)"
            }
        }
    }

    class ChildrenInfoViewHolder(private val binding: ChildrenInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(childrenInfo: AiResult) {
            binding.apply {
                name.text = childrenInfo.nm
                image.setImageResource(R.drawable.profile)
                sexBirthday.text = "${childrenInfo.sexdstnDscd} · ${(Calendar.getInstance().get(
                    Calendar.YEAR)) - childrenInfo.ageNow!!
                }년생"
                location.text = "${childrenInfo.occrAdres!!.take(2)}"
                date.text = "일시: ${childrenInfo.occrde?.let { formatDate(it) }}"
                age.text = "나이: ${childrenInfo.ageNow}세 (${childrenInfo.age}세)"
            }
        }
    }
}

fun formatDate(inputDateStr: String): String {
    val regex = Regex("""(\d{4})(\d{2})(\d{2})""")
    val matchResult = regex.find(inputDateStr)

    return if (matchResult != null) {
        val year = matchResult.groupValues[1]
        val month = matchResult.groupValues[2]
        val day = matchResult.groupValues[3]
        "$year. $month. $day"
    } else {
        "오류"
    }
}