package com.woojun.ai.util

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize

class TypeConverter {
    @TypeConverter
    fun fromChildInfoList(childInfoList: ArrayList<ChildInfo>): String {
        return Gson().toJson(childInfoList)
    }

    @TypeConverter
    fun toChildInfoList(childInfoListString: String): ArrayList<ChildInfo> {
        val listType = object : TypeToken<ArrayList<ChildInfo>>() {}.type
        return Gson().fromJson(childInfoListString, listType)
    }
}


data class PagerItem(
    val image: Int,
    val title: String,
    val subject: String
)

data class ResultSearchKeyword(
    var documents: List<Place>
)

data class Place(
    var place_name: String,
    var address_name: String,
    var road_address_name: String,
    var x: String,
    var y: String,
)

@Entity
data class UserInfo(
    @PrimaryKey
    var name: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var check: Boolean = false,
    var children: ArrayList<ChildInfo> = arrayListOf()
)

@Parcelize
data class ChildInfo(
    val id: String,
    val name: String,
    val birthDate: String,
    val sex: String,
    val characteristics: String,
    var photo: String,
    val lastIdentityDate: String
): Parcelable

@Parcelize
data class AiResult(
    val age: Int?,
    val ageNow: Int?,
    val alldressingDscd: String?,
    val etcSpfeatr: String?,
    val msspsnIdntfccd: Int?,
    val nm: String?,
    val occrAdres: String?,
    val occrde: String?,
    val rnum: Int?,
    val sexdstnDscd: String?,
    val tknphotoFile: String?,
    val tknphotolength: Int?,
    val writngTrgetDscd: String?
): Parcelable

class AiResultList: ArrayList<AiResult>()

enum class ChildInfoType {
    NEW, DEFAULT, SEARCH
}

enum class CameraType{
    ChildRegister, Find
}