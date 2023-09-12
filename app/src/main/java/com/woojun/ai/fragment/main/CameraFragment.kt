package com.woojun.ai.fragment.main

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.woojun.ai.MainActivity
import com.woojun.ai.R
import com.woojun.ai.databinding.FragmentCameraBinding
import com.woojun.ai.util.AppDatabase
import com.woojun.ai.util.CameraType
import com.woojun.ai.util.ChildInfo
import com.woojun.ai.util.FindChildImageResult
import com.woojun.ai.util.ProgressUtil.createDialog
import com.woojun.ai.util.ProgressUtil.createLoadingDialog
import com.woojun.ai.util.RetrofitAPI
import com.woojun.ai.util.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var check = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        binding.apply {

            val bundle = arguments
            val type = bundle?.getParcelable<CameraType>("camera type")
            val childInfo = bundle?.getParcelable<ChildInfo>("child info")

            if (type == CameraType.Find) {
                val mainActivity = activity as MainActivity
                mainActivity.hideBottomNavigation(true)
            }

            camera.setLifecycleOwner(this@CameraFragment)

            camera.addCameraListener(object : CameraListener() {
                override fun onPictureTaken(result: PictureResult) {
                    if (check) {
                        result.toBitmap { bitmap ->
                            if (bitmap != null) {

                                camera.visibility = View.INVISIBLE
                                captureBtn.visibility = View.INVISIBLE
                                image.visibility = View.INVISIBLE

                                val loadingDialog = createLoadingDialog(requireContext())
                                loadingDialog.show()

                                CoroutineScope(Dispatchers.IO).launch {
                                    val file = File(
                                        context?.filesDir,
                                        "${childInfo?.name ?: "${System.currentTimeMillis()}"}.jpg"
                                    )

                                    try {
                                        withContext(Dispatchers.IO) {
                                            val fileOutputStream = FileOutputStream(file)
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)

                                            fileOutputStream.close()
                                        }

                                        if (type == CameraType.ChildRegister) {
                                            withContext(Dispatchers.Main) {
                                                val storageRef = FirebaseStorage.getInstance().reference
                                                val imageRef = storageRef.child("child_images/${childInfo!!.name}.jpg")

                                                imageRef.putBytes(bitmapToByteArray(bitmap))
                                                    .addOnCompleteListener {
                                                        if (it.isSuccessful) {
                                                            imageRef.downloadUrl.addOnSuccessListener { uri ->

                                                                childInfo.photo = uri.toString()

                                                                val retrofit = RetrofitClient.getInstance()
                                                                val apiService = retrofit.create(RetrofitAPI::class.java)

                                                                val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                                                                val multipartBody = MultipartBody.Part.createFormData("FixImage", file.name, requestFile)

                                                                val call = apiService.setChildImage(multipartBody, childInfo.id)

                                                                call.enqueue(object : Callback<String> {
                                                                    override fun onResponse(call: Call<String>, response: Response<String>) {
                                                                        loadingDialog.dismiss()
                                                                        if (response.isSuccessful && response.body() == "success") {
                                                                            updateUserChildInfo(childInfo)
                                                                        } else {
                                                                            showFailDialog(CameraType.ChildRegister)
                                                                        }
                                                                    }

                                                                    override fun onFailure(call: Call<String>, t: Throwable) {
                                                                        loadingDialog.dismiss()
                                                                        showFailDialog(CameraType.ChildRegister)
                                                                    }
                                                                })

                                                            }.addOnFailureListener {
                                                                loadingDialog.dismiss()
                                                                showFailDialog(CameraType.ChildRegister)
                                                            }
                                                        } else {
                                                            loadingDialog.dismiss()
                                                            showFailDialog(CameraType.ChildRegister)
                                                        }
                                                    }

                                            }
                                        } else {
                                            withContext(Dispatchers.Main) {
                                                val retrofit = RetrofitClient.getInstance()
                                                val apiService = retrofit.create(RetrofitAPI::class.java)

                                                val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                                                val multipartBody = MultipartBody.Part.createFormData("FixImage", file.name, requestFile)

                                                val call = apiService.findChildImage(multipartBody)

                                                call.enqueue(object : Callback<FindChildImageResult> {
                                                    override fun onResponse(call: Call<FindChildImageResult>, response: Response<FindChildImageResult>) {
                                                        loadingDialog.dismiss()
                                                        if (response.isSuccessful) {
                                                            val item = Bundle()
                                                            item.putParcelable("children info", response.body())

                                                            view.findNavController().navigate(R.id.action_cameraFragment_to_findChildrenListFragment, item)
                                                        } else {
                                                            showFailDialog(CameraType.Find)
                                                        }
                                                    }

                                                    override fun onFailure(call: Call<FindChildImageResult>, t: Throwable) {
                                                        loadingDialog.dismiss()
                                                        showFailDialog(CameraType.Find)
                                                    }
                                                })
                                            }
                                        }
                                    } catch (e: IOException) {
                                        withContext(Dispatchers.Main) {
                                            loadingDialog.dismiss()
                                            if (type == CameraType.ChildRegister) {
                                                showFailDialog(CameraType.ChildRegister)
                                            } else {
                                                showFailDialog(CameraType.Find)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        check = false
                    }
                }

                override fun onVideoTaken(result: VideoResult) {
                }

                override fun onVideoRecordingEnd() {
                }

                override fun onPictureShutter() {
                }

                override fun onVideoRecordingStart() {
                }
            })

            captureBtn.setOnClickListener {
                camera.takePicture()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun updateUserChildInfo(childInfo: ChildInfo) {
        database = Firebase.database.reference

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(requireContext())
            val userDao = db!!.userInfoDao()
            val user = userDao.getUser()

            var check = true

            user.children.forEachIndexed { index, it ->
                if (it.id == childInfo.id) {
                    check = false
                    user.children[index] = childInfo
                }
            }

            if (check) {
                user.children.add(childInfo)
            }

            userDao.updateUser(user)
            database.child("users").child("${auth.uid}").setValue(user)
            database.child("children").child(childInfo.id).setValue(childInfo)
        }

        createDialog(
            requireContext(),
            true,
            "우리 아이 신원등록 성공",
            "우리 아이의 신원을 성공적으로 등록했습니다\n" +
                    "아이 인적사항에 변경사항이 생겼다면\n" +
                    "수정 탭에서 수정을 눌러주세요"
        ) {
            val mainActivity = activity as MainActivity
            mainActivity.hideBottomNavigation(false)
            view?.findNavController()?.navigate(R.id.action_cameraFragment_to_home)
        }

    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    fun showFailDialog(type: CameraType) {
        if (type == CameraType.Find) {
            createDialog(
                requireContext(),
                false,
                "실종아동 신원조회 실패",
                "실종아동 신원조회를 실패했습니다\n" +
                        "다시 얼굴을 정확히 가이드라인에 맞춰 시도해주세요\n" +
                        "만약 계속해서 오류가 발생한다면 제보 부탁드립니다"
            ) {
                val mainActivity = activity as MainActivity
                mainActivity.hideBottomNavigation(false)
                view?.findNavController()?.navigate(R.id.action_cameraFragment_to_home)
            }
        } else {
            createDialog(
                requireContext(),
                false,
                "우리 아이 신원등록 실패",
                "우리 아이의 신원등록을 실패했습니다\n" +
                        "다시 얼굴을 정확히 가이드라인에 맞춰 시도해주세요\n" +
                        "만약 계속해서 오류가 발생한다면 제보 부탁드립니다"
            ) {
                val mainActivity = activity as MainActivity
                mainActivity.hideBottomNavigation(false)
                view?.findNavController()?.navigate(R.id.action_cameraFragment_to_home)
            }
        }
    }

}