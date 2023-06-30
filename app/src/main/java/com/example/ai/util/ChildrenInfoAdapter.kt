package com.example.ai.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ai.R
import com.example.ai.databinding.NewChildrenInfoItemBinding

class ChildrenInfoAdapter(private val children: ArrayList<ChildInfo>): RecyclerView.Adapter<ChildrenInfoAdapter.ChildrenInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildrenInfoViewHolder {
        val binding = NewChildrenInfoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ChildrenInfoViewHolder(binding).also { handler ->
            binding.apply {

            }
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ChildrenInfoViewHolder, position: Int) {
        holder.bind(children[position])
    }

    class ChildrenInfoViewHolder(private val binding: NewChildrenInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(childrenInfo: ChildInfo) {
            binding.apply {
                name.text = childrenInfo.name
                image.setImageResource(R.drawable.profile)
                sexBirthday.text = "${childrenInfo.sex} · ${childrenInfo.birthday.substringBefore("년")}년생"
                location.text = childrenInfo.location
                date.text = "${childrenInfo.date.substringAfter("년 ")} 실종"
                age.text = "실종 나이 : ${childrenInfo.age}"
            }
        }
    }
}