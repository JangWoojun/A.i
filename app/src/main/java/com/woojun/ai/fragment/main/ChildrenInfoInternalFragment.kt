package com.woojun.ai.fragment.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentChildrenInfoInternalBinding
import com.woojun.ai.util.AiResult
import com.woojun.ai.util.ResultSearchKeyword
import com.woojun.ai.util.RetrofitAPI
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat


class ChildrenInfoInternalFragment : Fragment() {

    private var _binding: FragmentChildrenInfoInternalBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChildrenInfoInternalBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val mapView = MapView(requireContext())
            binding.mapView.addView(mapView)

            mapView.setOnTouchListener { view, motionEvent ->
                val action = motionEvent.action
                when (action) {
                    MotionEvent.ACTION_DOWN -> scrollView.requestDisallowInterceptTouchEvent(
                        true
                    )

                    MotionEvent.ACTION_UP -> scrollView.requestDisallowInterceptTouchEvent(true)
                    MotionEvent.ACTION_MOVE -> scrollView.requestDisallowInterceptTouchEvent(
                        true
                    )
                }
                false
            }

            val bundle = arguments
            val item = bundle?.getParcelable<AiResult>("child info")

            if (item != null) {
                name.text = "${item.nm}"
                age1.text = "${item.age}세"
                age2.text = "${item.ageNow}세"
                sex.text = "${item.sexdstnDscd}"
                date.text = "${item.occrde?.let { formatDate(it) }}"
                location.text = "${item.occrAdres}"
                locationShort.text = "${item.occrAdres?.take(2)}"
                dress.text = item.alldressingDscd ?: "불명"
                type.text = "${item.writngTrgetDscd?.let { getStatusDescription(it) }}"
                characteristics.text = "${item.etcSpfeatr}"

                searchKeyword(location.text.toString()) {
                    val mapPoint = MapPoint.mapPointWithGeoCoord(it.documents[0].y.toDouble(), it.documents[0].x.toDouble())

                    mapView.setMapCenterPoint(mapPoint, true)
                    mapView.setZoomLevel(1, true)

                    val marker = MapPOIItem()
                    marker.itemName = it.documents[0].address_name
                    marker.mapPoint = mapPoint
                    marker.markerType = MapPOIItem.MarkerType.CustomImage
                    marker.customImageResourceId = R.drawable.marker
                    marker.selectedMarkerType = MapPOIItem.MarkerType.CustomImage
                    marker.customSelectedImageResourceId = R.drawable.marker

                    mapView.addPOIItem(marker)
                }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchKeyword(keyword: String, onResponse: (ResultSearchKeyword) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(RetrofitAPI::class.java)
        val call = api.getSearchKeyword(com.woojun.ai.BuildConfig.RESTAPIKEY, keyword)

        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(
                call: Call<ResultSearchKeyword>,
                response: Response<ResultSearchKeyword>
            ) {
                Log.d("확인", "키워드: $keyword")
                Log.d("확인", "Raw: ${response.raw()}")
                Log.d("확인", "Body: ${response.body()}")
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                Log.w("확인", "통신 실패: ${t.message}")
            }
        })
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyyMMdd")
        val outputFormat = SimpleDateFormat("yy. MM. dd")

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

    private fun getStatusDescription(code: String): String {
        return when (code) {
            "010" -> "정상아동"
            "020" -> "가출인"
            "040" -> "시설보호무연고자"
            "060" -> "지적장애인"
            "061" -> "아동\n지적장애인"
            "062" -> "성인\n지적장애인"
            "070" -> "치매질환자"
            "080" -> "불상(기타)"
            else -> "불명"
        }
    }

}