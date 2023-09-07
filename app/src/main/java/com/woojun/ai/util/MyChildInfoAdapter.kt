package com.woojun.ai.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.woojun.ai.databinding.MyChildrenInfoItemBinding

class MyChildInfoAdapter(private val childInfo: ArrayList<ChildInfo>): RecyclerView.Adapter<MyChildInfoAdapter.MyChildInfoViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyChildInfoViewHolder {
        val binding = MyChildrenInfoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return MyChildInfoViewHolder(binding).also { handler ->
            binding.apply {

            }
        }

    }

    override fun onBindViewHolder(holder: MyChildInfoViewHolder, position: Int) {
        holder.bind(childInfo[position])
    }

    override fun getItemCount(): Int {
        return childInfo.size
    }


    class MyChildInfoViewHolder(private val binding: MyChildrenInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myChildInfo: ChildInfo) {
            binding.apply {

            }
        }
    }

}