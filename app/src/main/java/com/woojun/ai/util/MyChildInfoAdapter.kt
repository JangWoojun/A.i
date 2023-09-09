package com.woojun.ai.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.woojun.ai.R
import com.woojun.ai.databinding.MyChildrenInfoItemBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

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
                name.text = myChildInfo.name
                sexBirthday.text = "${myChildInfo.sex} · ${myChildInfo.birthDate.substring(0 until 4)}년생"
                date.text = "등록 일시: ${dateFormat(myChildInfo.lastIdentityDate)}"
                age.text = "현재 나이: ${calculateAge(myChildInfo.birthDate)}세"
                Glide.with(binding.root.context)
                    .load(myChildInfo.photo)
                    .error(R.drawable.child)
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888))
                    .format(DecodeFormat.PREFER_RGB_565)
                    .thumbnail(0.5f)
                    .into(profile)
            }
        }
    }

}

fun dateFormat(inputDate: String): String {
    val inputFormat = SimpleDateFormat("yyyyMMdd")
    val outputFormat = SimpleDateFormat("yyyy. MM. dd")

    val date = inputFormat.parse(inputDate)
    return outputFormat.format(date)
}

fun calculateAge(birthdateStr: String): Int {
    try {
        val birthdateDateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val birthdate: Date = birthdateDateFormat.parse(birthdateStr) ?: return -1

        val currentDate = Calendar.getInstance().time

        val birthdateCalendar = Calendar.getInstance()
        birthdateCalendar.time = birthdate
        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = currentDate

        return currentCalendar.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR)
    } catch (e: Exception) {
        e.printStackTrace()
        return -1 // 오류 발생 시 -1을 반환합니다.
    }
}